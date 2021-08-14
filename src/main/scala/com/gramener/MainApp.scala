package com.gramener

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.gramener.CustomRejectionHandler.myRejectionHandler
import com.gramener.JsonFormat.{ApiResponse, FinalResp, InValidReq, InvalidResp, ReqReturnType}
import com.gramener.JsonFormat.WeatherFormat._
import com.typesafe.config.ConfigFactory


import scala.concurrent.{ExecutionContextExecutor, Future}

object MainApp {

  implicit val actorSystem = ActorSystem(Behaviors.empty, "weather-update")
  implicit val executionContext: ExecutionContextExecutor = actorSystem.executionContext
  val config = ConfigFactory.load()

  def fetchWeatherDetails(inputStr:String, api_key:String):Future[Option[(Double, Double, String)]] =  {
    val uri = Uri("http://api.openweathermap.org/data/2.5/weather")
      .withQuery(Uri.Query(
        "q" -> inputStr, "appid" -> api_key, "units" -> "metric"
      ))

    val responseFuture: Future[HttpResponse] = Http().singleRequest(RequestBuilding.Get(uri))
    val entityFuture:Future[ReqReturnType] =
      responseFuture.flatMap(resp => Unmarshal(resp.entity).to[ApiResponse]).fallbackTo{
        responseFuture.flatMap(resp => Unmarshal(resp.entity).to[InvalidResp])
      }
    entityFuture.map{
      case s:InvalidResp => Some(0.toDouble,0.toDouble,"error")
      case e:ApiResponse =>   Some(e.main.temp,e.main.pressure,e.weather(0).main)
    }
  }

  val route = handleRejections(myRejectionHandler) {
    path("current") {
      get {
        parameters(Symbol("location").as[String]) {
          case str if str.last == ',' | str.split(',').length > 2 =>
            complete(InValidReq(msg = StatusCodes.Forbidden.toString()))
          case str =>
            val f: Future[Option[(Double, Double, String)]] =
              fetchWeatherDetails(str, config.getString("api-details.api_key"))

            onSuccess(f) {
              case Some(item) if (item._3 == "error") => complete(InValidReq(msg = StatusCodes.BadRequest.toString()))
              case Some(item) => complete(FinalResp(temp = item._1,
                pressure = item._2,
                umbrella = item._3 match {
                  case "Rain" | "Drizzle" | "Thunderstorm" => true
                  case _ => false
                }))
            }
        }
      }
    }
  }
  def main(args: Array[String]): Unit = {

    val bindingFuture = Http().newServerAt(
      config.getString("server-config.host"),
      config.getString("server-config.port").toInt
    ).bind(route)

  }
}
