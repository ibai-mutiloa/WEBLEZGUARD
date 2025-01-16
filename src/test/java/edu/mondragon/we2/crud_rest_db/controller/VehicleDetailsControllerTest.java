package edu.mondragon.we2.crud_rest_db.controller;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.mondragon.we2.crud_rest_db.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RunWith(JUnit4.class)
public class VehicleDetailsControllerTest {

    private VehicleDetailsController controller;
    private VehicleDetailsRepository vehicleDetailsRepository;
    private LocationRepository locationRepository;
    private MunicipalityRepository municipalityRepository;
    private VehicleDateRelationshipRepository vehicleDateRelationshipRepository;
    private PredictionRepository predictionRepository;
    
    private VehicleDetails testVehicle;
    private Location testLocation;

    @Before
    public void setUp() {
        // Create mocks
        vehicleDetailsRepository = createMock(VehicleDetailsRepository.class);
        locationRepository = createMock(LocationRepository.class);
        municipalityRepository = createMock(MunicipalityRepository.class);
        vehicleDateRelationshipRepository = createMock(VehicleDateRelationshipRepository.class);
        predictionRepository = createMock(PredictionRepository.class);

        // Create controller and inject mocks
        controller = new VehicleDetailsController();
        setField(controller, "vehicleDetailsRepository", vehicleDetailsRepository);
        setField(controller, "locationRepository", locationRepository);
        setField(controller, "municipalityRepository", municipalityRepository);
        setField(controller, "vehicleDateRelationshipRepository", vehicleDateRelationshipRepository);
        setField(controller, "predictionRepository", predictionRepository);

        // Initialize test data
        testLocation = new Location();
        testLocation.setId(1);
        testLocation.setLocationName("Test Location");
        testLocation.setCoordinates("43.0626,-2.4897");
        testLocation.setZoneType("Urban");
        testLocation.setMunicipalities(new HashSet<>());
        testLocation.setVehicles(new HashSet<>());

        testVehicle = new VehicleDetails();
        testVehicle.setLicensePlate("1234ABC");
        testVehicle.setTagType("ECO");
        testVehicle.setVehicleType("Car");
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Prius");
        testVehicle.setYear(2020);
        testVehicle.setFuel("Hybrid");
        testVehicle.setEngineType("HEV");
        testVehicle.setEmissionsGPerKM(95.0f);
        testVehicle.setColor("Blue");
        testVehicle.setLocation(testLocation);
    }

    @Test
    public void testGetAllVehicles() {
        List<VehicleDetails> expectedVehicles = Arrays.asList(testVehicle);
        expect(vehicleDetailsRepository.findAll()).andReturn(expectedVehicles);
        replay(vehicleDetailsRepository);

        List<VehicleDetails> actualVehicles = controller.getAllVehicles();
        
        verify(vehicleDetailsRepository);
        assertEquals(expectedVehicles, actualVehicles);
    }

     

    @Test
    public void testGetVehicleByLicensePlate_ExistingVehicle() {
        expect(vehicleDetailsRepository.findById("1234ABC"))
            .andReturn(Optional.of(testVehicle));
        replay(vehicleDetailsRepository);

        ResponseEntity<VehicleDetails> response = controller.getVehicleByLicensePlate("1234ABC");
        
        verify(vehicleDetailsRepository);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testVehicle, response.getBody());
    }

    @Test
    public void testGetVehicleByLicensePlate_NonExistingVehicle() {
        expect(vehicleDetailsRepository.findById("NOTFOUND"))
            .andReturn(Optional.empty());
        replay(vehicleDetailsRepository);

        ResponseEntity<VehicleDetails> response = controller.getVehicleByLicensePlate("NOTFOUND");
        
        verify(vehicleDetailsRepository);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    

    @Test
    public void testAddVehicle_WithExistingLocation() {
        expect(locationRepository.findById(1)).andReturn(Optional.of(testLocation));
        expect(vehicleDetailsRepository.save(testVehicle)).andReturn(testVehicle);
        replay(locationRepository, vehicleDetailsRepository);

        ResponseEntity<VehicleDetails> response = controller.addVehicle(testVehicle);
        
        verify(locationRepository, vehicleDetailsRepository);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testVehicle, response.getBody());
    }


    @Test
    public void testDeleteVehicle_ExistingVehicle() {
        expect(vehicleDetailsRepository.existsById("1234ABC")).andReturn(true);
        vehicleDetailsRepository.deleteById("1234ABC");
        expectLastCall();
        replay(vehicleDetailsRepository);

        ResponseEntity<Void> response = controller.deleteVehicle("1234ABC");
        
        verify(vehicleDetailsRepository);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testRegisterVehicleDetection_Success() {
        expect(vehicleDetailsRepository.findByLicensePlate("1234ABC"))
            .andReturn(Optional.of(testVehicle));
        expect(vehicleDateRelationshipRepository.save(anyObject(VehicleDateRelationship.class)))
            .andReturn(new VehicleDateRelationship());
        replay(vehicleDetailsRepository, vehicleDateRelationshipRepository);

        ResponseEntity<String> response = controller.registerVehicleDetection("1234ABC");
        
        verify(vehicleDetailsRepository, vehicleDateRelationshipRepository);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetPredictionsByDate() {
        LocalDate testDate = LocalDate.of(2024, 1, 10);
        Prediction testPrediction = new Prediction();
        testPrediction.setDate(testDate);
        testPrediction.setPredZbe(100.0);
        testPrediction.setPredCo2(200.0);

        expect(predictionRepository.findByDate(testDate))
            .andReturn(Arrays.asList(testPrediction));
        replay(predictionRepository);

        ResponseEntity<List<Prediction>> response = controller.getPredictionsByDate("2024-01-10");
        
        verify(predictionRepository);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100, response.getBody().get(0).getPredZbe().intValue());
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
public void testGetAllVehicleDateRelationships() {
    List<VehicleDateRelationship> expectedRelationships = Arrays.asList(new VehicleDateRelationship());
    expect(vehicleDateRelationshipRepository.findAll()).andReturn(expectedRelationships);
    replay(vehicleDateRelationshipRepository);

    List<VehicleDateRelationship> actualRelationships = controller.getAllVehicleDateRelationships();
    
    verify(vehicleDateRelationshipRepository);
    assertEquals(expectedRelationships, actualRelationships);
}

@Test
public void testAddVehicle_WithNewLocation() {
    Location newLocation = new Location();
    newLocation.setId(null);
    testVehicle.setLocation(newLocation);
    
    expect(locationRepository.save(newLocation)).andReturn(testLocation);
    expect(vehicleDetailsRepository.save(testVehicle)).andReturn(testVehicle);
    replay(locationRepository, vehicleDetailsRepository);

    ResponseEntity<VehicleDetails> response = controller.addVehicle(testVehicle);
    
    verify(locationRepository, vehicleDetailsRepository);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(testVehicle, response.getBody());
}

@Test
public void testAddVehicle_WithInvalidLocation() {
    Location invalidLocation = new Location();
    invalidLocation.setId(999);
    testVehicle.setLocation(invalidLocation);
    
    expect(locationRepository.findById(999)).andReturn(Optional.empty());
    replay(locationRepository);

    ResponseEntity<VehicleDetails> response = controller.addVehicle(testVehicle);
    
    verify(locationRepository);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
}

@Test
public void testAddVehicle_WithException() {
    expect(locationRepository.findById(1)).andThrow(new RuntimeException("Database error"));
    replay(locationRepository);

    ResponseEntity<VehicleDetails> response = controller.addVehicle(testVehicle);
    
    verify(locationRepository);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
}

@Test
public void testUpdateVehicle_Success() {
    VehicleDetails updatedVehicle = new VehicleDetails();
    updatedVehicle.setLocation(testLocation);
    
    expect(vehicleDetailsRepository.findById("1234ABC")).andReturn(Optional.of(testVehicle));
    expect(locationRepository.findById(1)).andReturn(Optional.of(testLocation));
    expect(vehicleDetailsRepository.save(anyObject(VehicleDetails.class))).andReturn(updatedVehicle);
    replay(vehicleDetailsRepository, locationRepository);

    ResponseEntity<VehicleDetails> response = controller.updateVehicle("1234ABC", updatedVehicle);
    
    verify(vehicleDetailsRepository, locationRepository);
    assertEquals(HttpStatus.OK, response.getStatusCode());
}

@Test
public void testUpdateVehicle_NotFound() {
    expect(vehicleDetailsRepository.findById("NOTFOUND")).andReturn(Optional.empty());
    replay(vehicleDetailsRepository);

    ResponseEntity<VehicleDetails> response = controller.updateVehicle("NOTFOUND", testVehicle);
    
    verify(vehicleDetailsRepository);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
public void testDeleteVehicle_NonExistingVehicle() {
    expect(vehicleDetailsRepository.existsById("NOTFOUND")).andReturn(false);
    replay(vehicleDetailsRepository);

    ResponseEntity<Void> response = controller.deleteVehicle("NOTFOUND");
    
    verify(vehicleDetailsRepository);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
public void testGetVehiclesByMunicipality_Found() {
    Municipality municipality = new Municipality();
    municipality.setId(1);
    List<VehicleDetails> expectedVehicles = Arrays.asList(testVehicle);
    
    expect(municipalityRepository.findById(1)).andReturn(Optional.of(municipality));
    expect(vehicleDetailsRepository.findVehiclesByMunicipalityId(1)).andReturn(expectedVehicles);
    replay(municipalityRepository, vehicleDetailsRepository);

    ResponseEntity<List<VehicleDetails>> response = controller.getVehiclesByMunicipality(1);
    
    verify(municipalityRepository, vehicleDetailsRepository);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedVehicles, response.getBody());
}

@Test
public void testGetVehiclesByMunicipality_NotFound() {
    expect(municipalityRepository.findById(999)).andReturn(Optional.empty());
    replay(municipalityRepository);

    ResponseEntity<List<VehicleDetails>> response = controller.getVehiclesByMunicipality(999);
    
    verify(municipalityRepository);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
public void testRegisterVehicleDetection_VehicleNotFound() {
    String licensePlateJson = "{\"plate\":[\"1234ABC\"]}";
    expect(vehicleDetailsRepository.findByLicensePlate("1234ABC"))
        .andReturn(Optional.empty());
    replay(vehicleDetailsRepository);

    ResponseEntity<String> response = controller.registerVehicleDetection(licensePlateJson);
    
    verify(vehicleDetailsRepository);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
public void testInsertPrediction_Success() {
    Prediction prediction = new Prediction();
    prediction.setDate(LocalDate.now());
    prediction.setPredZbe(100.0);
    prediction.setPredCo2(200.0);
    
    expect(predictionRepository.save(prediction)).andReturn(prediction);
    replay(predictionRepository);

    ResponseEntity<Prediction> response = controller.insertPrediction(prediction);
    
    verify(predictionRepository);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(prediction, response.getBody());
}

@Test
public void testInsertPrediction_InvalidData() {
    Prediction invalidPrediction = new Prediction();
    
    ResponseEntity<Prediction> response = controller.insertPrediction(invalidPrediction);
    
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
}

@Test
public void testGetVehicleDateRelationship_Success() {
    String licensePlate = "1234ABC";
    LocalDate date = LocalDate.parse("2024-01-01");
    List<VehicleDateRelationship> expectedRelationships = Arrays.asList(new VehicleDateRelationship());
    
    expect(vehicleDateRelationshipRepository.findByLicensePlateAndDatePassed(licensePlate, date))
        .andReturn(expectedRelationships);
    replay(vehicleDateRelationshipRepository);

    ResponseEntity<List<VehicleDateRelationship>> response = 
        controller.getVehicleDateRelationship(licensePlate, "2024-01-01");
    
    verify(vehicleDateRelationshipRepository);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedRelationships, response.getBody());
}

@Test
public void testGetVehicleDateRelationship_NotFound() {
    String licensePlate = "1234ABC";
    LocalDate date = LocalDate.parse("2024-01-01");
    List<VehicleDateRelationship> emptyList = Arrays.asList();
    
    expect(vehicleDateRelationshipRepository.findByLicensePlateAndDatePassed(licensePlate, date))
        .andReturn(emptyList);
    replay(vehicleDateRelationshipRepository);

    ResponseEntity<List<VehicleDateRelationship>> response = 
        controller.getVehicleDateRelationship(licensePlate, "2024-01-01");
    
    verify(vehicleDateRelationshipRepository);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
}

@Test
public void testGetVehicleDateRelationship_InvalidDate() {
    String licensePlate = "1234ABC";
    
    ResponseEntity<List<VehicleDateRelationship>> response = 
        controller.getVehicleDateRelationship(licensePlate, "invalid-date");
    
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
}

@Test
public void testRegisterVehicleDetection_SuccessfulRegistration() {
    // Setup test data
    String licensePlateJson = "{\"plate\":[\"1234ABC\"]}";
    LocalDate currentDate = LocalDate.now();
    
    // Create the expected relationship object
    VehicleDateRelationship expectedRelationship = new VehicleDateRelationship();
    expectedRelationship.setLicensePlate("1234ABC");
    expectedRelationship.setDatePassed(currentDate);
    
    // Set up expectations
    expect(vehicleDetailsRepository.findByLicensePlate("1234ABC"))
        .andReturn(Optional.of(testVehicle));
    
    // Expect the save operation with a matcher that verifies the relationship properties
    expect(vehicleDateRelationshipRepository.save(anyObject(VehicleDateRelationship.class)))
        .andAnswer(() -> {
            VehicleDateRelationship savedRelationship = (VehicleDateRelationship) getCurrentArguments()[0];
            assertEquals("1234ABC", savedRelationship.getLicensePlate());
            assertEquals(currentDate, savedRelationship.getDatePassed());
            return savedRelationship;
        });
    
    replay(vehicleDetailsRepository, vehicleDateRelationshipRepository);

    // Execute the method
    ResponseEntity<String> response = controller.registerVehicleDetection(licensePlateJson);
    
    // Verify
    verify(vehicleDetailsRepository, vehicleDateRelationshipRepository);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(response.getBody().contains("1234ABC"));
    assertTrue(response.getBody().contains(currentDate.toString()));
    assertEquals("Detección registrada para el vehículo con matrícula {\"plate\":[\"1234ABC\"]} en la fecha " + currentDate, 
                 response.getBody());
}

@Test
public void testRegisterVehicleDetection_SaveFailure() {
    // Setup test data
    String licensePlateJson = "{\"plate\":[\"1234ABC\"]}";
    
    // Set up expectations
    expect(vehicleDetailsRepository.findByLicensePlate("1234ABC"))
        .andReturn(Optional.of(testVehicle));
    
    expect(vehicleDateRelationshipRepository.save(anyObject(VehicleDateRelationship.class)))
        .andThrow(new RuntimeException("Database error"));
    
    replay(vehicleDetailsRepository, vehicleDateRelationshipRepository);

    // Execute the method
    ResponseEntity<String> response = controller.registerVehicleDetection(licensePlateJson);
    
    // Verify
    verify(vehicleDetailsRepository, vehicleDateRelationshipRepository);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Error al registrar la detección.", response.getBody());
}

@Test
public void testRegisterVehicleDetection_InvalidJsonFormat() {
    // Setup test data with invalid JSON
    String invalidJson = "invalid-json-format";
    
    // Execute the method
    ResponseEntity<String> response = controller.registerVehicleDetection(invalidJson);
    
    // Verify
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Error al registrar la detección.", response.getBody());
}
}