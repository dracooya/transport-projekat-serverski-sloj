package rs.ac.uns.ftn.transport.service;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.dto.ride.IncomingRideSimulationDTO;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Rejection;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;
import rs.ac.uns.ftn.transport.repository.DriverRepository;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RideServiceImpl implements IRideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final MessageSource messageSource;

    public RideServiceImpl(RideRepository rideRepository,
                           DriverRepository driverRepository, MessageSource messageSource) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Ride save(Ride ride) {

        ride.setEstimatedTimeInMinutes(5);
        ride.setStartTime(LocalDateTime.now());
        ride.setEndTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
        Driver driver = new Driver();
        driver.setId(2);
        driver.setEmail("driver@mail.com");
        ride.setDriver(driver);
        Rejection r = new Rejection();
        r.setReason("Boba");
        r.setTimeOfRejection(LocalDateTime.now());
        r.setRide(ride);
        ride.setRejection(r);
        ride.setTotalCost(1234.0);
        ride.setStatus(RideStatus.ACTIVE);
        return rideRepository.save(ride);
    }

    @Override
    public Ride findActiveForDriver(Integer driverId) {
        Optional<Ride> active = rideRepository.findByDriver_IdAndStatus(driverId,RideStatus.ACTIVE);
        if(active.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return active.get();
    }

    @Override
    public Ride findActiveForPassenger(Integer passengerId) {
        Optional<Ride> active = rideRepository.findByPassengers_IdAndStatus(passengerId,RideStatus.ACTIVE);
        if(active.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return active.get();
    }

    @Override
    public Page<Ride> findPassenger(Integer passengerId, Pageable page) {
        return rideRepository.findPassenger(passengerId, page);
    }

    @Override
    public Ride findOne(Integer id) {

        Optional<Ride> active = rideRepository.findById(id);
        if(active.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return active.get();
    }

    public Page<Ride> findAllByDriver_Id(Integer id, Pageable page) {
        return rideRepository.findAllByDriver_Id(id, page);
    }

    public Page<Ride> findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, start, end, page);
    }

    public Page<Ride> findAllByDriver_IdAndStartTimeIsAfter(Integer id, LocalDateTime start, Pageable page) {
        return rideRepository.findAllByDriver_IdAndStartTimeIsAfter(id, start, page);
    }

    public Page<Ride> findAllByDriver_IdAndEndTimeIsBefore(Integer id, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByDriver_IdAndEndTimeIsBefore(id, end, page);
    }

    @Override
    public Page<Ride> findAllByPassenger_Id(Integer id, Pageable page) {
        return rideRepository.findAllByPassengers_Id(id, page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndStartTimeIsAfterAndEndTimeIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByPassengers_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, start, end, page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndStartTimeIsAfter(Integer id, LocalDateTime start, Pageable page) {
        return rideRepository.findAllByPassengers_IdAndStartTimeIsAfter(id, start,page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndEndTimeIsBefore(Integer id, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByPassengers_IdAndEndTimeIsBefore(id, end, page);
    }

    @Override
    public Ride cancelRide(Integer id) {
        Optional<Ride> toCancel = rideRepository.findById(id);
        if(toCancel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,messageSource.getMessage("ride.notFound", null, Locale.getDefault()));
        }
        Ride cancelled = toCancel.get();
        if(cancelled.getStatus() != RideStatus.PENDING && cancelled.getStatus() != RideStatus.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messageSource.getMessage("cancellation.invalidStatus", null, Locale.getDefault()));
        }
        cancelled.setStatus(RideStatus.CANCELLED);
        rideRepository.save(cancelled);
        return cancelled;
    }

    @Override
    public Ride acceptRide(Integer id) {
        Ride toAccept = rideRepository.findById(id).orElse(null);
        toAccept.setStatus(RideStatus.ACCEPTED);
        rideRepository.save(toAccept);
        return toAccept;
    }

    @Override
    public Ride endRide(Integer id) {
        Ride toEnd = rideRepository.findById(id).orElse(null);
        toEnd.setStatus(RideStatus.FINISHED);
        rideRepository.save(toEnd);
        return toEnd;
    }

    @Override
    public Ride cancelWithExplanation(Integer rideId, Rejection explanation) {
        Ride toReject = rideRepository.findById(rideId).orElseGet(null);
        toReject.setStatus(RideStatus.REJECTED);
        explanation.setRide(toReject);
        explanation.setTimeOfRejection(LocalDateTime.now());
        toReject.setRejection(explanation);
        rideRepository.save(toReject);
        return toReject;
    }

    public List<Ride> getActiveRides() {
        return rideRepository.findByStatus(RideStatus.ACTIVE);
    }

    public Ride saveForSimulation(IncomingRideSimulationDTO dto) {
        Ride ride = new Ride();
        ride.setEstimatedTimeInMinutes(5);
        ride.setStartTime(LocalDateTime.now());
        ride.setEndTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
        Driver driver = driverRepository.findById(dto.getDriverId()).orElseGet(null);
        driver.getVehicle().getCurrentLocation().setLatitude(dto.getLatitude());
        driver.getVehicle().getCurrentLocation().setLongitude(dto.getLongitude());
        ride.setDriver(driver);
        ride.setRejection(null);
        ride.setTotalCost(1234.0);
        ride.setStatus(RideStatus.ACTIVE);
        ride.setVehicleType(driver.getVehicle().getVehicleType());
        return rideRepository.save(ride);
    }
}
