package com.socgen.riskweb.Model;

import java.util.List;

public class ResponseInternal {
    private List<InternalRegistrations> internalRegistrations;
    private String registrationType; // To distinguish internal/external

    public List<InternalRegistrations> getInternalRegistrations() {
        return internalRegistrations;
    }

    public void setInternalRegistrations(List<InternalRegistrations> internalRegistrations) {
        this.internalRegistrations = internalRegistrations;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }
}


package com.socgen.riskweb.Model;

import org.springframework.data.annotation.Id;
import java.util.List;

public class InternalRegistrations {
    @Id
    private String bdrId; // Changed from entityId to bdrId
    private List<SubBookingEntity> subBookings; // Changed from registrations to subBookings

    public String getBdrId() {
        return bdrId;
    }

    public void setBdrId(String bdrId) {
        this.bdrId = bdrId;
    }

    public List<SubBookingEntity> getSubBookings() {
        return subBookings;
    }

    public void setSubBookings(List<SubBookingEntity> subBookings) {
        this.subBookings = subBookings;
    }

    @Override
    public String toString() {
        return "InternalRegistrations{" +
                "bdrId='" + bdrId + '\'' +
                ", subBookings=" + subBookings +
                '}';
    }
}


package com.socgen.riskweb.Model;

public class SubBookingEntity {
    private String subBookingId; // Maps to subBookingEntityId
    private String subBookingName; // Maps to subBookingEntityName
    private String country;
    private String createdDate;

    public String getSubBookingId() {
        return subBookingId;
    }

    public void setSubBookingId(String subBookingId) {
        this.subBookingId = subBookingId;
    }

    public String getSubBookingName() {
        return subBookingName;
    }

    public void setSubBookingName(String subBookingName) {
        this.subBookingName = subBookingName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "SubBookingEntity{" +
                "subBookingId='" + subBookingId + '\'' +
                ", subBookingName='" + subBookingName + '\'' +
                ", country='" + country + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
