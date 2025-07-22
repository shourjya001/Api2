package com.socgen.riskweb.enums;

public enum AppQueries {
    // Existing queries
    QRY_ACTIONCODE_TRUNCATE("TRUNCATE TABLE \"MAESNUMIPL\""),
    QRY_PRIMARYROLE_TRUNCATE("TRUNCATE TABLE \"TMAESNUMIPL\""),
    QRY_SAVE_PRIMARYROLE("INSERT INTO \"TMAESNUMIPL\" (\"entityId\",\"code\",\"subbookingId\") VALUES (?,?,?)"),
    QRY_SAVE_ACTIONCODE("INSERT INTO \"WK_MAESTRO_PRIMROLE_DBE\" (\"bdrId\",\"businessEntity\") VALUES (?,?)"),
    
    // Updated queries for external registration
    QRY_EXTERNAL_TRUNCATE("TRUNCATE TABLE \"TMAESNUMIPL_EXTERNAL\""),
    QRY_SAVE_EXTERNAL("INSERT INTO \"TMAESNUMIPL_EXTERNAL\" (\"bdrId\",\"subBookingId\",\"subBookingName\",\"country\",\"createdDate\") VALUES (?,?,?,?,?)");

    private final String value;

    AppQueries(String enumValue) {
        this.value = enumValue;
    }

    public String value() {
        return this.value;
    }
}
