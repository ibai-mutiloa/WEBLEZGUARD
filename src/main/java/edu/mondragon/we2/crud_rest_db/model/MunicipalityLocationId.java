package edu.mondragon.we2.crud_rest_db.model;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class MunicipalityLocationId implements Serializable {
    private Integer municipalityId;
    private Integer locationId;
    
    public Integer getMunicipalityId() {
        return municipalityId;
    }
    public void setMunicipalityId(Integer municipalityId) {
        this.municipalityId = municipalityId;
    }
    public Integer getLocationId() {
        return locationId;
    }
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    
}