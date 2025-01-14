package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MunicipalityLocationTest {

    private MunicipalityLocation municipalityLocation;

    @Before
    public void setUp() {
        municipalityLocation = new MunicipalityLocation();
    }

    @Test
    public void testGetAndSetId() {
        municipalityLocation.setId(1);
        assertEquals(1, municipalityLocation.getId().intValue());
    }

    @Test
    public void testGetAndSetLocation() {
        Location location = new Location();
        location.setId(101);
        location.setLocationName("Test Location");
        municipalityLocation.setLocation(location);

        assertNotNull(municipalityLocation.getLocation());
        assertEquals(101, municipalityLocation.getLocation().getId().intValue());
        assertEquals("Test Location", municipalityLocation.getLocation().getLocationName());
    }

    @Test
    public void testGetAndSetMunicipalityId() {
        municipalityLocation.setMunicipalityId(202);
        assertEquals(202, municipalityLocation.getMunicipalityId().intValue());
    }
}
