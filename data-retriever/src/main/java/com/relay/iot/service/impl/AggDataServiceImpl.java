package com.relay.iot.service.impl;

import com.relay.iot.exception.InvalidDataException;
import com.relay.iot.model.Constant;
import com.relay.iot.model.Field;
import com.relay.iot.model.dto.AggDataRequest;
import com.relay.iot.service.DataAccessService;
import com.relay.iot.service.AggDataService;
import com.relay.iot.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AggDataServiceImpl implements AggDataService {
    @Value("${influx.db.bucket}")
    private String bucket;

    @Override
    public String calculateAggVal(AggDataRequest request) {
        validate(request);
        return DataAccessService.calculateMeasurementValue(bucket, request.getOperation(), request.getStartTime(),
                request.getEndTime(), request.getEventType(), getFilters(request.getClusterId()) );
    }

    protected boolean validate(AggDataRequest request)
    {
        if (request == null || request.getStartTime() == null || StringUtils.isEmpty(request.getStartTime())) {
            throw new InvalidDataException(100, "start.date.empty");
        }
        if (request.getEndTime() == null || StringUtils.isEmpty(request.getEndTime())) {
            throw new InvalidDataException(101, "end.date.empty");
        }
        if (DateUtil.isValid(request.getStartTime())) {
            throw new InvalidDataException(102, "start.date.invalid");
        }
        if (DateUtil.isValid(request.getEndTime())) {
            throw new InvalidDataException(103, "end.date.invalid");
        }
        if (DateUtil.toOffsetDateTime(request.getStartTime()).isAfter( DateUtil.toOffsetDateTime(request.getEndTime()))) {
            throw new InvalidDataException(104, "start.greater.than.end");
        }
        if (request.getOperation() == null || StringUtils.isEmpty(request.getOperation())) {
            throw new InvalidDataException(105, "operation.empty");
        }
        if (Arrays.stream(Constant.OPERATION.values()).noneMatch((t) -> t.name().equalsIgnoreCase(request.getOperation()))) {
            throw new InvalidDataException(106, "operation.invalid");
        }
        return true;
    }

    protected List<Field> getFilters(Long clusterId)
    {
        List<Field> filters = new ArrayList<>();
        if(clusterId != null)
        {
            filters.add(new Field("cluster_id", Field.FieldType.LONG, clusterId));
        }
        return filters;
    }
}
