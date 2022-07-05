package com.marcluque.hydra.shared;

public class Measurement {

    private final String formatString;

    private final Long measuredTime;

    public Measurement(String formatString, Long measuredTime) {
        this.formatString = formatString;
        this.measuredTime = measuredTime;
    }

    public String getFormatString() {
        return formatString;
    }

    public Long getMeasuredTime() {
        return measuredTime;
    }
}
