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
}