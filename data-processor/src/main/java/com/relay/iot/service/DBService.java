package com.relay.iot.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.relay.iot.model.dto.Event;
import lombok.extern.slf4j.Slf4j;
/*import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Value;*/
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DBService {
    //@Value("${spring.influx.url}")
    private String url = "http://localhost:8086";

    //@Value("${spring.influx.username}")
    private String userName = "root";

    //@Value("${spring.influx.password}")
    private String password = "12345678";

    //@Value("${spring.influx.database}")
    private String database = "iot-data";

    public void write(Event event)
    {
       /* //InfluxDB influxDB = InfluxDBFactory.connect(url, userName, password);
        try {
            Pong response = influxDB.ping();
            if (response.getVersion().equalsIgnoreCase("unknown")) {
                log.error("Error pinging server.");
                return;
            }
            influxDB.createDatabase(database);
            influxDB.createRetentionPolicy(
                    "defaultPolicy", database, "30d", 1, true);
            Point point = Point.measurement("memory")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("name", "server1")
                    .addField("free", 4743656L)
                    .addField("used", 1015096L)
                    .addField("buffer", 1010467L)
                    .build();
            influxDB.write(point);
            influxDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            influxDB.setRetentionPolicy("autogen");
        }
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);*/

        char[] token = "YeVsxcB7msHN0PB_DB-m02iAlSZk9jQH6GqSJURz0tm4CUqVdWOV_s2zty1jwLN8wMdZxJD85seEg3iFPaGjpg==".toCharArray();
        String org = "relay";
        String bucket = "iot-data";

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(url, token, org, bucket);

        //
        // Write data
        //
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        //
        // Write by Data Point
        //
        Point point = Point.measurement("temperature")
                //.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("sensor-id", event.getId())
                .addField("sensor_name", event.getName())
                .addField("sensor_type", event.getType())
                .addField("cluster_id", event.getClusterId())
                .addField("value", event.getValue())
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
        writeApi.writePoint(point);

        //
        // Query data
        //
        String flux = "from(bucket:\"iot-data\") |> range(start: 0)";

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
            }
        }

        influxDBClient.close();
    }
}
