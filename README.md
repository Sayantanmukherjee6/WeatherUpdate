# WeatherUpdate

## Dependencies
* Java 8
* Scala 2.13.6

## Application configuration
Go to `src/main/resources/application.conf` and add the api key
```json
api-details {
    api_key: ""
}
```
## API routes

```json
api request: curl http://localhost:8080/current?location=Berlin
api response: {
                "pressure": 999.0,
                "temp": 19.38,
                "umbrella": false
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
