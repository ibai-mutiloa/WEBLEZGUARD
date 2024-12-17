package edu.mondragon.we2.crud_rest_db.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleDateRelationshipRepository extends JpaRepository<VehicleDateRelationship, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
