package com.socgen.riskweb.Model;

import java.util.List;

public class ResponseInternal {
    private List<InternalRegistrations> internalRegistrations;

    public List<InternalRegistrations> getInternalRegistrations() {
        return internalRegistrations;
    }

    public void setInternalRegistrations(List<InternalRegistrations> internalRegistrations) {
        this.internalRegistrations = internalRegistrations;
    }
}
package com.socgen.riskweb.Model;

import org.springframework.data.annotation.Id;
import java.util.List;

public class InternalRegistrations {
    @Id
    private String bdrId;
    private List<SubBookingEntity> subBookings;

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

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubBookingEntity {
    @JsonProperty("subBookingEntityId")
    private String subBookingId;
    @JsonProperty("subBookingEntityName")
    private String subBookingName;
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
