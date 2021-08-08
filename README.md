# WeatherUpdate

## Dependencies
* Java 8
* Scala 2.13.6

## Application Configuration
Go to `src/main/resources/application.conf` and add the api key
```json
api-details {
    api_key: ""
}
```
## Test application in terminal
* sbt test

## Run application in terminal
* sbt stage
* ./target/universal/stage/bin/weatherupdate

## Run application in docker
* sbt docker:publishLocal
* docker run -p 8080:8080 -d --rm weatherupdate
