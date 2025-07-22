package com.socgen.riskweb.enums;

public enum AppQueries {
    QRY_MAESTROPRC_TRUNCATE("TRUNCATE TABLE \"MAESTROPRC\""),
    QRY_SAVE_MAESTROPRC("INSERT INTO \"MAESTROPRC\" (\"bdrId\",\"subBookingId\",\"subBookingName\",\"country\",\"createdDate\") VALUES (?,?,?,?,?)");

    private final String value;

    AppQueries(String enumValue) {
        this.value = enumValue;
    }

    public String value() {
        return this.value;
    }
}
