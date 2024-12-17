package edu.mondragon.we2.crud_rest_db.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleDetailsRepository extends JpaRepository<VehicleDetails, String> {

    @Query("SELECT v FROM VehicleDetails v " +
           "JOIN v.location l " +
           "JOIN l.municipalities m " +
           "WHERE m.id = :municipalityId")
    List<VehicleDetails> findVehiclesByMunicipalityId(@Param("municipalityId") Integer municipalityId);

    // Método para buscar por matrícula
    Optional<VehicleDetails> findByLicensePlate(String licensePlate);
}

