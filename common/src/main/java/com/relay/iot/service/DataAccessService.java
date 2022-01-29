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

    public static boolean save(String tableName, List<Field> fields) {
        InfluxDBClient client = getConnection();
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        Point point = Point.measurement(tableName);
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

    public static void dd()
    {
        String flux = "from(bucket:\"iot-data\") |> range(start: 0)";

        QueryApi queryApi = getConnection().getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                System.out.println(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
            }
        }
    }

   public static void closeConnection() {
      if(connection != null)
      {
         connection.close();
      }
   }
}
