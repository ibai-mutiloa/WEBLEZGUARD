package edu.mondragon.we2.crud_rest_db.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
    // Define custom queries if needed
}
