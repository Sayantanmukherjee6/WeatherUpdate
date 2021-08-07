enablePlugins(JavaAppPackaging)

name := "WeatherUpdate"

organization := "com.gramener"

version := "1.0"

scalaVersion := "2.13.6"

lazy val akkaVersion = "2.6.15"
lazy val akkaHttpVersion = "10.2.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
)



