package edu.mondragon.we2.crud_rest_db.model;

import jakarta.persistence.*;

@Entity
public class MunicipalityLocation {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    
    @Column(name = "municipality_id")
    private Integer municipalityId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(Integer municipalityId) {
        this.municipalityId = municipalityId;
    }
}
