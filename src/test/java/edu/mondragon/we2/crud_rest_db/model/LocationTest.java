package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class LocationTest {

    private Location location;

    @Before
    public void setUp() {
        location = new Location();
    }

    @Test
    public void testGetAndSetId() {
        location.setId(1);
        assertEquals(1, location.getId().intValue());
    }

    @Test
    public void testGetAndSetLocationName() {
        location.setLocationName("Test Location");
        assertEquals("Test Location", location.getLocationName());
    }

    @Test
    public void testGetAndSetCoordinates() {
        location.setCoordinates("43.0626,-2.4897");
        assertEquals("43.0626,-2.4897", location.getCoordinates());
    }

    @Test
    public void testGetAndSetZoneType() {
        location.setZoneType("Urban");
        assertEquals("Urban", location.getZoneType());
    }

    @Test
    public void testGetAndSetMunicipalities() {
        Set<Municipality> municipalities = new HashSet<>();
        Municipality municipality = new Municipality();
        municipality.setId(1);
        municipalities.add(municipality);

        location.setMunicipalities(municipalities);
        assertEquals(1, location.getMunicipalities().size());
        assertTrue(location.getMunicipalities().contains(municipality));
    }

    @Test
    public void testGetAndSetVehicles() {
        Set<VehicleDetails> vehicles = new HashSet<>();
        VehicleDetails vehicle = new VehicleDetails();
        vehicle.setLicensePlate("1234ABC");
        vehicles.add(vehicle);

        location.setVehicles(vehicles);
        assertEquals(1, location.getVehicles().size());
        assertTrue(location.getVehicles().contains(vehicle));
    }
}

