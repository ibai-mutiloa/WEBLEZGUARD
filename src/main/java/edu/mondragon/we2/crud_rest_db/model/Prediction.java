package edu.mondragon.we2.crud_rest_db.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "predicciones")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // o GenerationType.AUTO
    @Column(name = "id")
    private Long id;

    private LocalDate date;
    private Double predZbe;
    private Double predCo2;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPredZbe() {
        return predZbe;
    }

    public void setPredZbe(Double predZbe) {
        this.predZbe = predZbe;
    }

    public Double getPredCo2() {
        return predCo2;
    }

    public void setPredCo2(Double predCo2) {
        this.predCo2 = predCo2;
    }
}
