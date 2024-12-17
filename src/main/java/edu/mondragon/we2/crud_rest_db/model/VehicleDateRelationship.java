package edu.mondragon.we2.crud_rest_db.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle_date_relationship")
public class VehicleDateRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Vehicle_Date_ID")
    private Integer id;

    @Column(name = "License_Plate")  // Cambiado a una columna simple
    private String licensePlate; 

    @Column(name = "Date_Passed")
    private LocalDate datePassed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDate getDatePassed() {
        return datePassed;
    }

    public void setDatePassed(LocalDate datePassed) {
        this.datePassed = datePassed;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    
    // Getters and Setters
}
