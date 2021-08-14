package com.gramener
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration._
import com.gramener.MainApp.route

class MainAppSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {
  implicit val timeout = RouteTestTimeout(2.seconds)

  "A Router" should {
    "return a valid get request in json" in {
      Get("/current?location=Mumbai") ~> route ~> check {
        contentType should ===(ContentTypes.`application/json`)
      }
    }
  }

  "A Router" should {
    "return a incomplete get request with valid msg in json" in {
      Get("/current?location=Mumbai,") ~> route ~> check {
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"msg":"403 Forbidden"}""")
      }
    }
  }

  "A Router" should {
    "return a invalid get request in json" in {
      Get("/current?location=M") ~> route ~> check {
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"msg":"400 Bad Request"}""")
      }
    }
  }

  "A Router" should {
    "return a exception for get request with incorrect parameter in json" in {
      Get("/current?locat") ~> route ~> check {
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"msg":"406 Not Acceptable"}""")
      }
    }
  }

  "A Router" should {
    "return a exception for get request with incorrect handler in json" in {
      Get("/incorrect") ~> route ~> check {
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"msg":"404 Not Found"}""")
      }
    }
  }

}