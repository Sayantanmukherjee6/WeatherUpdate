package com.gramener

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{MissingQueryParamRejection, RejectionHandler}
import com.gramener.JsonFormat.InValidReq
import com.gramener.JsonFormat.WeatherFormat._


object CustomRejectionHandler {

  implicit def myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case MissingQueryParamRejection(param) =>
          val errorResponse = InValidReq(StatusCodes.NotAcceptable.toString())
          complete(errorResponse)
      }
    .handleNotFound {
      val errorResponse = InValidReq(StatusCodes.NotFound.toString())
      complete(errorResponse)
    }
      .result()

}
