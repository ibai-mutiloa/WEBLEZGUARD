package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VehicleDetailsTest {

    private VehicleDetails vehicleDetails;
    private Location testLocation;

    @Before
    public void setUp() {
        vehicleDetails = new VehicleDetails();
        testLocation = new Location();
        testLocation.setId(1);
        testLocation.setLocationName("Test Location");
        testLocation.setCoordinates("43.2620,-2.9350");
        testLocation.setZoneType("Urban");
    }

    @Test
    public void testGetAndSetLicensePlate() {
        String licensePlate = "1234ABC";
        vehicleDetails.setLicensePlate(licensePlate);
        assertEquals(licensePlate, vehicleDetails.getLicensePlate());
    }

    @Test
    public void testGetAndSetTagType() {
        String tagType = "ECO";
        vehicleDetails.setTagType(tagType);
        assertEquals(tagType, vehicleDetails.getTagType());
    }

    @Test
    public void testGetAndSetVehicleType() {
        String vehicleType = "Car";
        vehicleDetails.setVehicleType(vehicleType);
        assertEquals(vehicleType, vehicleDetails.getVehicleType());
    }

    @Test
    public void testGetAndSetBrand() {
        String brand = "Toyota";
        vehicleDetails.setBrand(brand);
        assertEquals(brand, vehicleDetails.getBrand());
    }

    @Test
    public void testGetAndSetModel() {
        String model = "Prius";
        vehicleDetails.setModel(model);
        assertEquals(model, vehicleDetails.getModel());
    }

    @Test
    public void testGetAndSetYear() {
        Integer year = 2020;
        vehicleDetails.setYear(year);
        assertEquals(year, vehicleDetails.getYear());
    }

    @Test
    public void testGetAndSetFuel() {
        String fuel = "Hybrid";
        vehicleDetails.setFuel(fuel);
        assertEquals(fuel, vehicleDetails.getFuel());
    }

    @Test
    public void testGetAndSetEngineType() {
        String engineType = "HEV";
        vehicleDetails.setEngineType(engineType);
        assertEquals(engineType, vehicleDetails.getEngineType());
    }

    @Test
    public void testGetAndSetEmissionsGPerKM() {
        Float emissionsGPerKM = 95.0f;
        vehicleDetails.setEmissionsGPerKM(emissionsGPerKM);
        assertEquals(emissionsGPerKM, vehicleDetails.getEmissionsGPerKM());
    }

    @Test
    public void testGetAndSetColor() {
        String color = "Blue";
        vehicleDetails.setColor(color);
        assertEquals(color, vehicleDetails.getColor());
    }

    @Test
    public void testGetAndSetLocation() {
        vehicleDetails.setLocation(testLocation);
        assertEquals(testLocation, vehicleDetails.getLocation());
    }

    @Test
    public void testVehicleDetailsInitialization() {
        vehicleDetails.setLicensePlate("5678DEF");
        vehicleDetails.setTagType("Zero");
        vehicleDetails.setVehicleType("Truck");
        vehicleDetails.setBrand("Tesla");
        vehicleDetails.setModel("Cybertruck");
        vehicleDetails.setYear(2023);
        vehicleDetails.setFuel("Electric");
        vehicleDetails.setEngineType("Electric Motor");
        vehicleDetails.setEmissionsGPerKM(0.0f);
        vehicleDetails.setColor("Silver");
        vehicleDetails.setLocation(testLocation);

        assertEquals("5678DEF", vehicleDetails.getLicensePlate());
        assertEquals("Zero", vehicleDetails.getTagType());
        assertEquals("Truck", vehicleDetails.getVehicleType());
        assertEquals("Tesla", vehicleDetails.getBrand());
        assertEquals("Cybertruck", vehicleDetails.getModel());
        assertEquals(Integer.valueOf(2023), vehicleDetails.getYear());
        assertEquals("Electric", vehicleDetails.getFuel());
        assertEquals("Electric Motor", vehicleDetails.getEngineType());
        assertEquals(Float.valueOf(0.0f), vehicleDetails.getEmissionsGPerKM());
        assertEquals("Silver", vehicleDetails.getColor());
        assertEquals(testLocation, vehicleDetails.getLocation());
    }
}
