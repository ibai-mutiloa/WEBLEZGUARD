package edu.mondragon.we2.crud_rest_db.model;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class PredictionTest {

    private Prediction prediction;

    @Before
    public void setUp() {
        prediction = new Prediction();
    }

    @Test
    public void testGetAndSetId() {
        prediction.setId(1L);
        assertEquals(Long.valueOf(1), prediction.getId());
    }

    @Test
    public void testGetAndSetDate() {
        LocalDate date = LocalDate.of(2025, 1, 10);
        prediction.setDate(date);
        assertEquals(date, prediction.getDate());
    }

    @Test
    public void testGetAndSetPredZbe() {
        prediction.setPredZbe(100.5);
        assertEquals(Double.valueOf(100.5), prediction.getPredZbe());
    }

    @Test
    public void testGetAndSetPredCo2() {
        prediction.setPredCo2(200.75);
        assertEquals(Double.valueOf(200.75), prediction.getPredCo2());
    }

    @Test
    public void testPredictionInitialization() {
        LocalDate date = LocalDate.of(2025, 1, 10);
        prediction.setId(1L);
        prediction.setDate(date);
        prediction.setPredZbe(120.0);
        prediction.setPredCo2(300.0);

        assertEquals(Long.valueOf(1), prediction.getId());
        assertEquals(date, prediction.getDate());
        assertEquals(Double.valueOf(120.0), prediction.getPredZbe());
        assertEquals(Double.valueOf(300.0), prediction.getPredCo2());
    }
}
