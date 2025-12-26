package com.sfl.core.web.rest.system.filter;


import java.util.Objects;

public class CriteriaKeyValuePair {

    public enum Operator {
        LIKE, EQUAL, GREATER_THAN, LESS_THAN, NOT_EQUAL, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, IN
    }
    private String key;
    private Object value;
    private DataType dataType;
    private Operator operator;
    private String enumName;

    public CriteriaKeyValuePair() {
    }

    public CriteriaKeyValuePair(String key, Object value, DataType dataType, Operator operator) {
        this.key = key;
        this.value = value;
        this.dataType = dataType;
        this.operator = operator;
    }

    public CriteriaKeyValuePair(String key, Object value, DataType dataType, String enumName, Operator operator) {
        this.key = key;
        this.value = value;
        this.dataType = dataType;
        this.enumName=enumName;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriteriaKeyValuePair that = (CriteriaKeyValuePair) o;
        return Objects.equals(key, that.key) &&
            Objects.equals(value, that.value) &&
            Objects.equals(dataType, that.dataType)
            && Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, dataType, operator);
    }
}
