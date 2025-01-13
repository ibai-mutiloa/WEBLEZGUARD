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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // o GenerationType.AUTO
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "pred_zbe")
    private Double predZbe;

    @Column(name = "pred_co2")
    private Double predCo2;

    @Column(name = "add_event") // Nuevo campo para el checkbox
    private Boolean addEvent;

    // Getters y Setters

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

    public Boolean getAddEvent() {
        return addEvent;
    }

    public void setAddEvent(Boolean addEvent) {
        this.addEvent = addEvent;
    }
}
