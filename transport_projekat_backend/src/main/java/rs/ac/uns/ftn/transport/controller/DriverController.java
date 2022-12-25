package rs.ac.uns.ftn.transport.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.*;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.DocumentDTOMapper;
import rs.ac.uns.ftn.transport.mapper.DriverDTOMapper;
import rs.ac.uns.ftn.transport.mapper.VehicleDTOMapper;
import rs.ac.uns.ftn.transport.mapper.WorkingHoursDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;
import rs.ac.uns.ftn.transport.service.interfaces.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value="api/driver")
public class DriverController {

    private final IDriverService driverService;
    private final IDocumentService documentService;
    private final IVehicleService vehicleService;
    private final ILocationService locationService;
    private final IWorkingHoursService workingHoursService;
    private final IRideService rideService;


    public DriverController(IDriverService driverService,
                            IDocumentService documentService,
                            IVehicleService vehicleService,
                            ILocationService locationService,
                            IWorkingHoursService workingHoursService,
                            IRideService rideService) {
        this.driverService = driverService;
        this.documentService = documentService;
        this.vehicleService = vehicleService;
        this.locationService = locationService;
        this.workingHoursService = workingHoursService;
        this.rideService = rideService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Integer id) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(DriverDTOMapper.fromDrivertoDTO(driver), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<DriverPageDTO> getDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);

        Set<DriverDTO> driverDTOs = drivers.stream()
                .map(DriverDTOMapper::fromDrivertoDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new DriverPageDTO(drivers.getTotalElements(), driverDTOs), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DriverDTO> saveDriver(@Valid @RequestBody Driver driver) throws ConstraintViolationException {
        try {
            driver = driverService.save(driver);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(DriverDTOMapper.fromDrivertoDTO(driver), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id, @Valid @RequestBody Driver driver) throws ConstraintViolationException {
        Driver driverToUpdate = driverService.findOne(id);

        driverToUpdate.setName(driver.getName());
        driverToUpdate.setSurname(driver.getSurname());
        driverToUpdate.setProfilePicture(driver.getProfilePicture());
        driverToUpdate.setTelephoneNumber(driver.getTelephoneNumber());
        driverToUpdate.setEmail(driver.getEmail());
        driverToUpdate.setAddress(driver.getAddress());

        try {
            driverToUpdate = driverService.save(driverToUpdate);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DriverDTOMapper.fromDrivertoDTO(driverToUpdate), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/documents", consumes = "application/json")
    public ResponseEntity<DocumentDTO> saveDocument(@PathVariable Integer id, @Valid @RequestBody DocumentDTO documentDTO) throws ConstraintViolationException {
        Driver driver = driverService.findOne(id);

        Document document = new Document(DocumentType.getEnum(documentDTO.getName()), documentDTO.getDocumentImage(), driver);

        document = documentService.save(document);
        return new ResponseEntity<>(DocumentDTOMapper.fromDocumenttoDTO(document), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<Set<DocumentDTO>> getDocuments(@PathVariable Integer id) {
        Set<Document> documents = documentService.findAllByDriver_Id(id);

        Set<DocumentDTO> documentDTOs = documents.stream()
                                    .map(DocumentDTOMapper::fromDocumenttoDTO)
                                    .collect(Collectors.toSet());
        return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
    }

    @DeleteMapping(value = "/document/{id}")
    @Transactional
    public ResponseEntity<String> deleteDocument(@PathVariable Integer id) {
        documentService.deleteById(id);
        return new ResponseEntity<>("Driver document deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{id}/vehicle", consumes = "application/json")
    public ResponseEntity<VehicleDTO> saveVehicle(@PathVariable Integer id, @Valid @RequestBody VehicleDTO vehicleDTO) throws ConstraintViolationException {
        Driver driver = driverService.findOne(id);

        Vehicle vehicle = VehicleDTOMapper.fromDTOtoVehicle(vehicleDTO);
        vehicle.setDriver(driver);

        if (vehicle.getCurrentLocation() != null) {
            Location location = vehicle.getCurrentLocation();
            locationService.save(location);
        }

        vehicle = vehicleService.save(vehicle);

        driver.setVehicle(vehicle);
        driverService.save(driver);
        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(vehicle), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.getVehicleByDriver_Id(id);

        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(vehicle), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/vehicle", consumes = "application/json")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @Valid @RequestBody VehicleDTO vehicleDTO) throws ConstraintViolationException {
        Vehicle oldVehicle = vehicleService.getVehicleByDriver_Id(id);

        if (oldVehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vehicle newVehicle = VehicleDTOMapper.fromDTOtoVehicle(vehicleDTO);

        oldVehicle.setVehicleType(newVehicle.getVehicleType());
        oldVehicle.setModel(newVehicle.getModel());
        oldVehicle.setLicenseNumber(newVehicle.getLicenseNumber());

        if (newVehicle.getCurrentLocation() != null) {
            oldVehicle.setCurrentLocation(newVehicle.getCurrentLocation());
            locationService.save(oldVehicle.getCurrentLocation());
        }

        oldVehicle.setPassengerSeats(newVehicle.getPassengerSeats());
        oldVehicle.setBabyTransport(newVehicle.getBabyTransport());
        oldVehicle.setPetTransport(newVehicle.getPetTransport());

        oldVehicle = vehicleService.save(oldVehicle);
        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(oldVehicle), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/working-hour", consumes = "application/json")
    public ResponseEntity<WorkingHoursDTO> saveWorkingHours(@PathVariable Integer id, @Valid @RequestBody WorkingHoursDTO workingHoursDTO) throws ConstraintViolationException {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        WorkingHours workingHours = WorkingHoursDTOMapper.fromDTOToWorkingHours(workingHoursDTO);
        workingHours.setDriver(driver);

        workingHours = workingHoursService.save(workingHours);
        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHoursDTO> getWorkingHour(@PathVariable Integer workingHourId) {
        WorkingHours workingHours = workingHoursService.findOne(workingHourId);

        if (workingHours == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.OK);
    }

    @PutMapping(value = "/working-hour/{workingHourId}", consumes = "application/json")
    public ResponseEntity<WorkingHoursDTO> updateWorkingHour(@PathVariable Integer workingHourId, @Valid @RequestBody WorkingHoursDTO workingHoursDTO) throws ConstraintViolationException {
        WorkingHours workingHours = workingHoursService.findOne(workingHourId);

        if (workingHours == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        workingHours.setStart(workingHoursDTO.getStart());
        if (workingHoursDTO.getEnd() != null) {
            workingHours.setEnd(workingHoursDTO.getEnd());
        }

        workingHours = workingHoursService.save(workingHours);
        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hour")
    public ResponseEntity<WorkingHoursPageDTO> getWorkingHours(Pageable page,
                                                               @PathVariable Integer id,
                                                               @RequestParam(value = "from", required = false) LocalDateTime from,
                                                               @RequestParam(value = "to", required = false) LocalDateTime to) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<WorkingHours> workingHours;

        if (from == null && to == null) {
            workingHours = workingHoursService.findAllByDriver_Id(id, page);
        } else if (from != null && to == null) {
            workingHours = workingHoursService.findAllByDriver_IdAndStartIsAfter(id, from, page);
        } else if (from == null) {
            workingHours = workingHoursService.findAllByDriver_IdAndEndIsBefore(id, to, page);
        } else {
            workingHours = workingHoursService.findAllByDriver_IdAndStartIsAfterAndEndIsBefore(id, from, to, page);
        }

        Set<WorkingHoursDTO> workingHoursDTOs = workingHours.stream()
                .map(WorkingHoursDTOMapper::fromWorkingHoursToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new WorkingHoursPageDTO(workingHours.getTotalElements(), workingHoursDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePageDTO> getRides(Pageable page,
                                                @PathVariable Integer id,
                                                @RequestParam(value = "from", required = false) LocalDateTime from,
                                                @RequestParam(value = "to", required = false) LocalDateTime to)
    {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<Ride> rides;

        if (from == null && to == null) {
            rides = rideService.findAllByDriver_Id(id, page);
        } else if (from != null && to == null) {
            rides = rideService.findAllByDriver_IdAndStartTimeIsAfter(id, from, page);
        } else if (from == null) {
            rides = rideService.findAllByDriver_IdAndEndTimeIsBefore(id, to, page);
        } else {
            rides = rideService.findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, from, to, page);
        }

        Set<RideCreatedDTO> rideDTOs = rides.stream()
                .map(RideCreatedDTOMapper::fromRideToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new RidePageDTO(rides.getTotalElements(), rideDTOs), HttpStatus.OK);
    }
}
