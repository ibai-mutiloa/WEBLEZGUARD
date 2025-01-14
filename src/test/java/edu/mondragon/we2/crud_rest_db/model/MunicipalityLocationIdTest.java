package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MunicipalityLocationIdTest {

    private MunicipalityLocationId municipalityLocationId;

    @Before
    public void setUp() {
        municipalityLocationId = new MunicipalityLocationId();
    }

    @Test
    public void testGetAndSetMunicipalityId() {
        municipalityLocationId.setMunicipalityId(1);
        assertEquals(1, municipalityLocationId.getMunicipalityId().intValue());
    }

    @Test
    public void testGetAndSetLocationId() {
        municipalityLocationId.setLocationId(101);
        assertEquals(101, municipalityLocationId.getLocationId().intValue());
    }

   
}

