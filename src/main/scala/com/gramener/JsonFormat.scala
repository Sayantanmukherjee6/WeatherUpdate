package com.gramener

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object JsonFormat {

  case class MainState(temp:Double,feels_like:Double,temp_min:Double,temp_max:Double,pressure:Double,humidity:Double)
  case class WeatherState(id: Long, main: String, description: String, icon: String)
  case class ApiResponse(weather:List[WeatherState], main:MainState)

  case class InValidReq(msg:String)
  case class FinalResp(temp:Double,pressure:Double,umbrella:Boolean)

  object WeatherFormat extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val mainState = jsonFormat6(MainState)
    implicit val weatherState = jsonFormat4(WeatherState)
    implicit val apiResponse = jsonFormat2(ApiResponse)

    implicit val invalidReqFormat = jsonFormat1(InValidReq)
    implicit val finalResp = jsonFormat3(FinalResp)
  }

}
