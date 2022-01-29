package com.relay.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {
    public enum FieldType { LONG, DOUBLE, BOOLEAN, STRING }
    private String name;
    private FieldType type;
    private Object value;
}
