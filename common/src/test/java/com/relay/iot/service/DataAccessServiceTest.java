package com.relay.iot.service;

import com.relay.iot.exception.InvalidDataException;
import com.relay.iot.model.Constant;
import com.relay.iot.model.Field;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessServiceTest {

    @Test
    void getFluxQuery() {
        //With bucket, start, end time, operation
        assertEquals("from(bucket:\"bkt\") |> range(start: 2022-01-01, stop: 2022-01-31)|> filter(fn: (r) => r[\"_field\"] == \"value\" )|> min()\n" +
                "|> yield()", DataAccessService.getFluxQuery("bkt", Constant.OPERATION.MIN.toString(), "2022-01-01", "2022-01-31", null, new ArrayList<Field>()));

        //additionally measurement
        assertEquals("from(bucket:\"bkt\") |> range(start: 2022-01-01, stop: 2022-01-31)|> filter(fn: (r) => r[\"_field\"] == \"value\" )|> filter(fn: (r) => r._measurement == \"temperature\" )|> min()\n" +
                "|> yield()", DataAccessService.getFluxQuery("bkt", Constant.OPERATION.MIN.toString(), "2022-01-01", "2022-01-31", Constant.SENSOR_TYPE.TEMPERATURE.toString(), new ArrayList<Field>()));

        //additionally fields
        List<Field> fields = new ArrayList<>(){{
           add(new Field("clusterId", Field.FieldType.LONG, 100L));
        }};
        assertEquals("from(bucket:\"bkt\") |> range(start: 2022-01-01, stop: 2022-01-31)|> filter(fn: (r) => r[\"_field\"] == \"value\" )|> filter(fn: (r) => r._measurement == \"temperature\" )|> min()\n" +
                "|> yield()", DataAccessService.getFluxQuery("bkt", Constant.OPERATION.MIN.toString(), "2022-01-01", "2022-01-31", Constant.SENSOR_TYPE.TEMPERATURE.toString(), fields));
    }

    @Test
    void getFunction() {
        assertEquals("min()", DataAccessService.getFunction(Constant.OPERATION.MIN.toString()));
        assertEquals("max()", DataAccessService.getFunction(Constant.OPERATION.MAX.toString()));
        assertEquals("mean()", DataAccessService.getFunction(Constant.OPERATION.AVERAGE.toString()));
        assertEquals("median()", DataAccessService.getFunction(Constant.OPERATION.MEDIAN.toString()));
        try
        {
            DataAccessService.getFunction("INVALID_OPERATION");
        }
        catch (InvalidDataException e){
            assertEquals(106, e.getCode());
            assertEquals("operation.invalid", e.getMessage());
        }
    }

    @Test
    void getResultantValue() {
        List<Double> values = Arrays.asList(10.0, 5.0, 20.0, 30.0, 15.0);
        assertEquals("5.0", DataAccessService.getResultantValue(values, Constant.OPERATION.MIN.toString()));
        assertEquals("30.0", DataAccessService.getResultantValue(values, Constant.OPERATION.MAX.toString()));
        assertEquals("16.0", DataAccessService.getResultantValue(values, Constant.OPERATION.AVERAGE.toString()));
        assertEquals("16.0", DataAccessService.getResultantValue(values, Constant.OPERATION.MEDIAN.toString()));
        try
        {
            DataAccessService.getResultantValue(values, "INVALID_OPERATION");
        }
        catch (InvalidDataException e){
            assertEquals(106, e.getCode());
            assertEquals("operation.invalid", e.getMessage());
        }
    }
}