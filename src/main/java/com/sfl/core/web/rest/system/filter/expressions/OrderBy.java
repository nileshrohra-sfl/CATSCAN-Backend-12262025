package com.sfl.core.web.rest.system.filter.expressions;

public class OrderBy {
    public String field;
    public boolean ascending;

    public OrderBy() {
    }

    public OrderBy(String field, boolean ascending) {
        this.field = field;
        this.ascending = ascending;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
