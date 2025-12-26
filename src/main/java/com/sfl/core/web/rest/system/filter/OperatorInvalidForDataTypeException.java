package com.sfl.core.web.rest.system.filter;

public class OperatorInvalidForDataTypeException extends RuntimeException {
    public OperatorInvalidForDataTypeException(DataType dataType, CriteriaKeyValuePair.Operator operator) {
        super("The operator " + operator.name() + " is not valid for datatype " + dataType.name());
    }
}
