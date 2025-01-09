package edu.mondragon.we2.crud_rest_db.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    List<Prediction> findByDate(LocalDate parsedDate);
    // Aquí puedes agregar métodos personalizados si es necesario
}
