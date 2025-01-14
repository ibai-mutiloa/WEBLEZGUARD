package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MunicipalityTest {

    private Municipality municipality;

    @Before
    public void setUp() {
        municipality = new Municipality();
    }

    @Test
    public void testGetAndSetId() {
        municipality.setId(1);
        assertEquals(1, municipality.getId().intValue());
    }

    @Test
    public void testGetAndSetName() {
        municipality.setName("Test Municipality");
        assertEquals("Test Municipality", municipality.getName());
    }

    @Test
    public void testGetAndSetAddress() {
        municipality.setAddress("123 Main Street");
        assertEquals("123 Main Street", municipality.getAddress());
    }

    @Test
    public void testGetAndSetContactPhone() {
        municipality.setContactPhone("123-456-7890");
        assertEquals("123-456-7890", municipality.getContactPhone());
    }

    @Test
    public void testGetAndSetLocations() {
        Set<Location> locations = new HashSet<>();
        Location location = new Location();
        location.setId(1);
        location.setLocationName("Test Location");
        locations.add(location);

        municipality.setLocations(locations);
        assertEquals(1, municipality.getLocations().size());
        assertTrue(municipality.getLocations().contains(location));
    }
}
