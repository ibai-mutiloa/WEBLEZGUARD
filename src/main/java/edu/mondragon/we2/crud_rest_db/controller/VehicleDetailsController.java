package edu.mondragon.we2.crud_rest_db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.mondragon.we2.crud_rest_db.model.Location;
import edu.mondragon.we2.crud_rest_db.model.LocationRepository;
import edu.mondragon.we2.crud_rest_db.model.Municipality;
import edu.mondragon.we2.crud_rest_db.model.MunicipalityRepository;
import edu.mondragon.we2.crud_rest_db.model.Prediction;
import edu.mondragon.we2.crud_rest_db.model.PredictionRepository;
import edu.mondragon.we2.crud_rest_db.model.VehicleDateRelationshipRepository;
import edu.mondragon.we2.crud_rest_db.model.VehicleDateRelationship;
import edu.mondragon.we2.crud_rest_db.model.VehicleDetails;
import edu.mondragon.we2.crud_rest_db.model.VehicleDetailsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleDetailsController {

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private VehicleDetailsRepository vehicleDetailsRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private VehicleDateRelationshipRepository vehicleDateRelationshipRepository;

    // Get all vehicle details
    @GetMapping
    public List<VehicleDetails> getAllVehicles() {
        return vehicleDetailsRepository.findAll();
    }

    // Método para obtener todos los registros de `vehicle_date_relationship`
    @GetMapping("/vehicle-date-relationships")
    public List<VehicleDateRelationship> getAllVehicleDateRelationships() {
        return vehicleDateRelationshipRepository.findAll();
    }

    // Get vehicle details by license plate
    @GetMapping("/{licensePlate}")
    public ResponseEntity<VehicleDetails> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        Optional<VehicleDetails> vehicle = vehicleDetailsRepository.findById(licensePlate);
        return vehicle.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Add new vehicle
    @PostMapping(consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<VehicleDetails> addVehicle(@RequestBody VehicleDetails vehicleDetails) {
        try {
            // Validate and save the location
            if (vehicleDetails.getLocation() != null) {
                if (vehicleDetails.getLocation().getId() == null) {
                    // Save new location
                    Location savedLocation = locationRepository.save(vehicleDetails.getLocation());
                    vehicleDetails.setLocation(savedLocation);
                } else {
                    // Verify existing location
                    Optional<Location> existingLocation = locationRepository.findById(vehicleDetails.getLocation().getId());
                    if (existingLocation.isEmpty()) {
                        return ResponseEntity.badRequest().build(); // 400 Bad Request if location is invalid
                    }
                    vehicleDetails.setLocation(existingLocation.get());
                }
            }

            // Save the vehicle details
            VehicleDetails savedVehicle = vehicleDetailsRepository.save(vehicleDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // Update vehicle details
    @PutMapping("/{licensePlate}")
    public ResponseEntity<VehicleDetails> updateVehicle(@PathVariable String licensePlate, @RequestBody VehicleDetails updatedDetails) {
        Optional<VehicleDetails> existingVehicle = vehicleDetailsRepository.findById(licensePlate);

        if (existingVehicle.isPresent()) {
            VehicleDetails vehicle = existingVehicle.get();
            vehicle.setTagType(updatedDetails.getTagType());
            vehicle.setVehicleType(updatedDetails.getVehicleType());
            vehicle.setBrand(updatedDetails.getBrand());
            vehicle.setModel(updatedDetails.getModel());
            vehicle.setYear(updatedDetails.getYear());
            vehicle.setFuel(updatedDetails.getFuel());
            vehicle.setEngineType(updatedDetails.getEngineType());
            vehicle.setEmissionsGPerKM(updatedDetails.getEmissionsGPerKM());
            vehicle.setColor(updatedDetails.getColor());

            if (updatedDetails.getLocation() != null) {
                Optional<Location> existingLocation = locationRepository.findById(updatedDetails.getLocation().getId());
                existingLocation.ifPresent(vehicle::setLocation);
            }

            VehicleDetails savedVehicle = vehicleDetailsRepository.save(vehicle);
            return ResponseEntity.ok(savedVehicle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete vehicle by license plate
    @DeleteMapping("/{licensePlate}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String licensePlate) {
        if (vehicleDetailsRepository.existsById(licensePlate)) {
            vehicleDetailsRepository.deleteById(licensePlate);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get vehicles by municipality ID
    @GetMapping("/municipality/{municipalityId}")
    public ResponseEntity<List<VehicleDetails>> getVehiclesByMunicipality(@PathVariable Integer municipalityId) {
        Optional<Municipality> municipality = municipalityRepository.findById(municipalityId);
        if (municipality.isPresent()) {
            List<VehicleDetails> vehicles = vehicleDetailsRepository.findVehiclesByMunicipalityId(municipalityId);
            return ResponseEntity.ok(vehicles);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/matricula/detection")
    public ResponseEntity<String> registerVehicleDetection(@RequestBody String licensePlate) {
        try {
            // Verificar si el vehículo existe
            // Buscar la posición del valor "1336FLG"
            String prefix = "\"plate\":[\"";
            String suffix = "\"]";

            // Extraer la parte del valor usando índices
            int start = licensePlate.indexOf(prefix) + prefix.length();
            int end = licensePlate.indexOf(suffix, start);
            String plate = licensePlate.substring(start, end);
            Optional<VehicleDetails> vehicle = vehicleDetailsRepository.findByLicensePlate(plate);
            if (!vehicle.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El vehículo con la matrícula " + licensePlate + " no se encuentra en la base de datos.");
            }
    
            // Obtener la fecha actual
            LocalDate currentDate = LocalDate.now();
    
            // Crear una nueva relación con la matrícula y la fecha
            VehicleDateRelationship relationship = new VehicleDateRelationship();
            relationship.setLicensePlate(plate);  // Guardar solo la matrícula como texto
            relationship.setDatePassed(currentDate);
    
            // Guardar la relación en la base de datos
            vehicleDateRelationshipRepository.save(relationship);
    
            return ResponseEntity.status(HttpStatus.CREATED).body("Detección registrada para el vehículo con matrícula " + licensePlate + " en la fecha " + currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar la detección.");
        }
    }  
    @PostMapping("/prediction")
    public ResponseEntity<Prediction> insertPrediction(@RequestBody Prediction prediction) {
        try {
            // Validar que los datos sean correctos
            if (prediction.getDate() == null || prediction.getPredZbe() == null || prediction.getPredCo2() == null) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request si falta algún dato
            }

            // Guardar la predicción en la base de datos
            Prediction savedPrediction = predictionRepository.save(prediction);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPrediction); // 201 Created

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    
    @GetMapping("/vehicle_date_relation/filtered")
    public ResponseEntity<List<VehicleDateRelationship>> getVehicleDateRelationship(@RequestParam String licensePlate, @RequestParam String date) {
    try {
        // Convert the date string to LocalDate
        LocalDate parsedDate = LocalDate.parse(date);

        // Find the relationship between the vehicle and the date
        List<VehicleDateRelationship> relationships = vehicleDateRelationshipRepository
            .findByLicensePlateAndDatePassed(licensePlate, parsedDate);

        if (!relationships.isEmpty()) {
            return ResponseEntity.ok(relationships);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }
    @GetMapping("/getByDate")
    public ResponseEntity<List<Prediction>> getPredictionsByDate(@RequestParam("date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<Prediction> predictions = predictionRepository.findByDate(parsedDate);
        return ResponseEntity.ok(predictions);
    }
}

