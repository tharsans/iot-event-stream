package com.relay.iot.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.relay.iot.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.List;

@Slf4j
public class DataAccessService {
  /* @Value("${db.influx.url}")
   private String url;

   @Value("${db.influx.username}")
   private String userName;

   @Value("${db.influx.password}")
   private String password;

   @Value("${db.influx.organization}")
   private String organization;

   @Value("${spring.influx.bucket}")
   private String bucket;

   @Value("${db.influx.database}")
   private String database;

   @Value("${db.influx.token}")
   private String token;*/
   private static final String url = "http://localhost:8086";
   private static final String organization = "relay";
   private static final String bucket = "iot-bucket";
   private static final String token = "YeVsxcB7msHN0PB_DB-m02iAlSZk9jQH6GqSJURz0tm4CUqVdWOV_s2zty1jwLN8wMdZxJD85seEg3iFPaGjpg==";

   private static InfluxDBClient connection = null;

   private DataAccessService()
   {

   }
   public static InfluxDBClient getConnection() {
      if(connection == null)
      {
         try {
             connection = InfluxDBClientFactory.create(url, token.toCharArray(), organization, bucket);
         }
         catch (Exception e){
            log.error("There are errors while getting db connection.", e);
         }

      }
      return connection;
   }

    public static boolean save(String measurement, List<Field> fields) {
        InfluxDBClient client = getConnection();
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        Point point = Point.measurement(measurement);
        for (Field field : fields)
        {
            if(field.getType() == Field.FieldType.LONG)
            {
                point.addField(field.getName(), Long.valueOf(field.getValue().toString()));
            }
            else if(field.getType() == Field.FieldType.DOUBLE)
            {
                point.addField(field.getName(), Double.valueOf(field.getValue().toString()));
            }
            else if(field.getType() == Field.FieldType.BOOLEAN)
            {
                point.addField(field.getName(), Boolean.valueOf(field.getValue().toString()));
            }
            else {
                point.addField(field.getName(), field.getValue().toString());
            }
        }
        point.time(Instant.now().toEpochMilli(), WritePrecision.MS);
        writeApi.writePoint(point);
        return true;
    }

    public static String calculateMeasurementValue(String bucket, String operation, String startTime, String endTime, String measurement, List<Field> filterFields)
    {
        String calculatedVal = null;
        QueryApi queryApi = getConnection().getQueryApi();
        List<FluxTable> tables = queryApi.query(getFluxQuery(bucket, operation, startTime, endTime, measurement, filterFields));
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
                //TODO: Fix
                calculatedVal = fluxRecord.getValueByKey("_value").toString();
            }
        }
        return calculatedVal;
    }

   public static void closeConnection() {
      if(connection != null)
      {
         connection.close();
      }
   }

   protected static String getFluxQuery(String bucket, String operation, String start, String end, String measurement, List<Field> filterFields)
   {
       String fluxQuery = "from(bucket:\"" + bucket + "\")" +
               " |> range(start: " + start + ", stop: " + end + ")" +
               "|> filter(fn: (r) => r[\"_field\"] == \"value\" )";
       if(StringUtils.isNotEmpty(measurement))
       {
           fluxQuery += "|> filter(fn: (r) => r._measurement == \"" + measurement +"\" )";
       }
       for (Field field : filterFields)
       {
           fluxQuery += "|> filter(fn: (r) => r._field == \"" + field.getName() + "\" and r._value == " + field.getValue() + " )";
       }
       fluxQuery += "|> " + getFunction(operation) + "\n" +
               "|> yield()";
       return fluxQuery;
   }

   protected static String getFunction(String operation)
   {
       String function = null;
        switch (operation){
            case "min":
                function = "min()";
                break;
            case "max":
                function = "min()";
                break;
            case "median":
                function = "median()";
                break;
            case "average":
                function = "mean()";
                break;
        }
        return function;
   }
}
