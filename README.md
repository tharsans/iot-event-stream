#IOT Sensor Event Stream
## Prerequisites
- JDK 11
- Apache kafka 2+
- Spring Boot 2+
- Spring security
- Influx DB 2.1.1
- Maven 3.6.x+
- Docker 20.10.12

### Microservices
- Data Producer - Simulating IoT events generation and publishes to Kafka topic
- Data Processor - Consumes events from Kafka topic and saves them to Influx DB
- Data Retriever - Exposes REST end point to filter time series data based on search criteria
  
Clone th project from [this](https://github.com/operations-relay42/iot-producer-simulator-api) repository

Go to the cloned directory 

Run Apache Kafka, Zookeeper in docker  
```sh
docker-compose -f "docker-compose.yml" up --build -d
```
Start Influx DB based on your operating system as per the given guidelines

Now clone [this](https://github.com/tharsans/iot-event-stream) project to your local file system

Open in any of your favourite IDE and do a maven build

```sh
mvn clean install
```
Run all three applications by going to each module. They will be opened on 8090, 8091, 8092 respectively.

### Sanity Test
To send 5 Temperature sensor send data

```sh
curl -X POST localhost:8090/api/v1/producer/events \
-H 'Content-Type: application/json' \
-d '{  "total" : "5", "type":"TEMPERATURE" }'
```

This will send 5 messages to the Kafka topic & the Data Processor Application will consume, process & persist it into the database.
You may notice similar message in that application's log as below

```sh
2022-01-30 17:06:20.185  INFO 12872 --- [ntainer#0-0-C-1] c.r.iot.service.KafkaListenerService     : Successfully saved that event: Event(id=1, value=35.9694628769, timestamp=1643542576764, type=TEMPERATURE, name=Sensor #1, clusterId=102)
2022-01-30 17:06:22.856  INFO 12872 --- [ntainer#0-0-C-1] c.r.iot.service.KafkaListenerService     : Received new Event: Event(id=2, value=27.5205356357, timestamp=1643542582474, type=TEMPERATURE, name=Sensor #2, clusterId=100)
```

Once the data is written to the Influx DB it can be queried either by REST end points of Data Retriever Application or Influx UI

You may query the by providing time range as shown below

```sh
curl -X POST localhost:8092/api/v1/sensor/aggVal \
-H 'Content-Type: application/json' \
-d '{   "startTime":"2022-01-01T00:00:00Z", "endTime":"2022-01-31T00:00:00Z", "operation": "average" }'
```

But as this end point is secured you may get an unauthorized error as response

You may get similar response as below.
```sh
{"code":0,"message":"success","data":"27.50196004166"}
```

Additionally if you want to filter only specific event types such as humidity, then pass it  as another parameter in your payload
```sh
curl -X POST localhost:8092/api/v1/sensor/aggVal \
-H 'Content-Type: application/json' \
-u 'root:123' \
-d '{ "startTime":"2022-01-01T00:00:00Z", "endTime":"2022-01-31T00:00:00Z", "operation": "average", "type": "HUMIDITY" }'
```

Also you can pass other parameters such as clusterId as well
```sh
curl -X POST localhost:8092/api/v1/sensor/aggVal \
-H 'Content-Type: application/json' \
-u 'root:123' \
-d '{ "startTime":"2022-01-01T00:00:00Z", "endTime":"2022-01-31T00:00:00Z", "operation": "average", "type": "HUMIDITY", "clusterId":"105" }'
```

In addition to that, if you want to get your error messages etc by localized, pass the locale as well along with your request header. If the given locale is not supported, then it will return it in English, which is the default.

```sh
curl -X POST localhost:8092/api/v1/sensor/aggVal \
-H 'Content-Type: application/json' \
-H 'Accept-Language: nl' \
-u 'root:123' \
-d '{ "startTime":"2022-02-01T00:00:00Z", "endTime":"2022-01-31T00:00:00Z", "operation": "average" }'
```
The response for the above request may look as below.

```sh
{"code":104,"message":"De opgegeven start is groter dan de einddatum. Start moet minder zijn dan einddatum","data":null}
```