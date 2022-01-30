package com.relay.iot.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.relay.iot.exception.InvalidDataException;
import com.relay.iot.model.Constant;
import com.relay.iot.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataAccessService {
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
        //The aggregations are calculated based on each measurement.
        //Hence get values of each measurement & then get overall measurement
        List<Double> valuesByMeasurement = new ArrayList<>();
        QueryApi queryApi = getConnection().getQueryApi();
        List<FluxTable> tables = queryApi.query(getFluxQuery(bucket, operation, startTime, endTime, measurement, filterFields));
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                log.info(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
                valuesByMeasurement.add(Double.valueOf(fluxRecord.getValueByKey("_value").toString()));
            }
        }
        return getResultantValue(valuesByMeasurement, operation);
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
           //TODO: Fix it when filter like clusterId is applied,
           //fluxQuery += "|> filter(fn: (r) => r._field == \"" + field.getName() + "\" and r._value == " + field.getValue() + " )";
       }
       fluxQuery += "|> " + getFunction(operation) + "\n" +
               "|> yield()";
       log.info("Flux query: {}", fluxQuery);
       return fluxQuery;
   }

   protected static String getFunction(String operation)
   {
       String function = null;
       if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MIN.toString()))
       {
           function = "min()";
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MAX.toString()))
       {
           function = "max()";
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.AVERAGE.toString()))
       {
           function = "mean()";
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MEDIAN.toString()))
       {
           function = "median()";
       }
       else {
           throw new InvalidDataException(106, "operation.invalid");
       }
        return function;
   }

   protected static String getResultantValue(List<Double> measurementValues, String operation){
       Double value = 0.0;
       if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MIN.toString()))
       {
           value = measurementValues.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MAX.toString()))
       {
           value = measurementValues.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.AVERAGE.toString()))
       {
           value = measurementValues.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
       }
       else if(StringUtils.equalsIgnoreCase(operation, Constant.OPERATION.MEDIAN.toString()))
       {
           value = measurementValues.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
       }
       else {
           throw new InvalidDataException(106, "operation.invalid");
       }
       return value.toString();
   }
}
