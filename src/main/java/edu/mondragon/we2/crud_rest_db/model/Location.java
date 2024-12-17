package edu.mondragon.we2.crud_rest_db.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "locations")
public class Location {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Location_ID")
    private Integer id;

    @Column(name = "Location_Name")
    private String locationName;

    @Column(name = "Coordinates")
    private String coordinates;

    @Column(name = "Zone_Type")
    private String zoneType;

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore  // Ignorar la serialización de las municipalidades
    private Set<Municipality> municipalities;

    @OneToMany(mappedBy = "location")
    @JsonIgnore  // Ignorar la serialización de los vehículos en Location
    private Set<VehicleDetails> vehicles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public Set<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(Set<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

    public Set<VehicleDetails> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<VehicleDetails> vehicles) {
        this.vehicles = vehicles;
    }

    // Getters and Setters
}
