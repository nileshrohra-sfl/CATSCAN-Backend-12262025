package com.sfl.core.domain.audit;

public class AuditData {

    private Object oldData;
    private Object newData;

    public AuditData(Object oldData, Object newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    public Object getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public Object getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    @Override
    public String toString() {
        return "AuditData{" +
            "oldData='" + oldData + '\'' +
            ", newData='" + newData + '\'' +
            '}';
    }
}
