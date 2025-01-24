
# How to run the project
1. Clone the project
2. Run the project using the following command
```bash
mvn spring-boot:run
```
3. The project will run on the following URL
```bash
http://localhost:8080
```
# Tech Used
1. Java 17
2. Spring Boot
3. Maven
4. Junit
5. Lombok


## POST: http://localhost:8080/api/tax/calculate

## Sample Requests 
```json
{
  "vehicle": "Car",
  "dates": [
    "2013-01-14T21:00:00",
    "2013-01-15T21:00:00",
    "2013-02-07T06:23:27",
    "2013-02-07T15:27:00",
    "2013-02-09T06:27:00",
    "2013-02-09T06:20:27",
    "2013-02-09T14:35:00",
    "2013-02-09T15:29:00",
    "2013-02-09T15:47:00",
    "2013-02-09T16:01:00",
    "2013-02-09T16:48:00",
    "2013-02-09T17:49:00",
    "2013-02-09T18:29:00",
    "2013-02-09T18:35:00",
    "2013-03-26T14:25:00",
    "2013-03-28T14:07:27"
  ]
}
```

