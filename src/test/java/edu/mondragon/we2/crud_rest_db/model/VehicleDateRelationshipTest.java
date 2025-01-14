package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class VehicleDateRelationshipTest {

    private VehicleDateRelationship vehicleDateRelationship;

    @Before
    public void setUp() {
        vehicleDateRelationship = new VehicleDateRelationship();
    }

    @Test
    public void testGetAndSetId() {
        vehicleDateRelationship.setId(1);
        assertEquals(Integer.valueOf(1), vehicleDateRelationship.getId());
    }

    @Test
    public void testGetAndSetLicensePlate() {
        String licensePlate = "1234ABC";
        vehicleDateRelationship.setLicensePlate(licensePlate);
        assertEquals(licensePlate, vehicleDateRelationship.getLicensePlate());
    }

    @Test
    public void testGetAndSetDatePassed() {
        LocalDate datePassed = LocalDate.of(2025, 1, 10);
        vehicleDateRelationship.setDatePassed(datePassed);
        assertEquals(datePassed, vehicleDateRelationship.getDatePassed());
    }

    @Test
    public void testVehicleDateRelationshipInitialization() {
        Integer id = 1;
        String licensePlate = "5678DEF";
        LocalDate datePassed = LocalDate.of(2023, 12, 15);

        vehicleDateRelationship.setId(id);
        vehicleDateRelationship.setLicensePlate(licensePlate);
        vehicleDateRelationship.setDatePassed(datePassed);

        assertEquals(id, vehicleDateRelationship.getId());
        assertEquals(licensePlate, vehicleDateRelationship.getLicensePlate());
        assertEquals(datePassed, vehicleDateRelationship.getDatePassed());
    }
}

