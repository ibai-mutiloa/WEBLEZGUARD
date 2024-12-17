package edu.mondragon.we2.crud_rest_db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "vehicle_details")
public class VehicleDetails {

    @Id
    @Column(name = "License_Plate")
    private String licensePlate;

    @Column(name = "Tag_Type")
    private String tagType;

    @Column(name = "Vehicle_Type")
    private String vehicleType;

    @Column(name = "Brand")
    private String brand;

    @Column(name = "Model")
    private String model;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Fuel")
    private String fuel;

    @Column(name = "Engine_Type")
    private String engineType;

    @Column(name = "Emissions_G_per_KM")
    private Float emissionsGPerKM;

    @Column(name = "Color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    // Getters y setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Float getEmissionsGPerKM() {
        return emissionsGPerKM;
    }

    public void setEmissionsGPerKM(Float emissionsGPerKM) {
        this.emissionsGPerKM = emissionsGPerKM;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
