package org.clearfuny.funnytest.interner.model;

public enum StepType {
    HTTP("http"), DBUNIT("dbUnit"), DEBUG("debug");

    private String value;

    StepType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static StepType getByValue(String value) {
        for (StepType v : StepType.values()){
            if (v.getValue().equalsIgnoreCase(value)) return v;
        }
        return null;
    }
}

