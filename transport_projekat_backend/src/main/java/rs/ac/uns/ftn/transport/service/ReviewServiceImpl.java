package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.dto.review.DriverReviewDTO;
import rs.ac.uns.ftn.transport.dto.review.ReviewRideDTO;
import rs.ac.uns.ftn.transport.dto.review.VehicleReviewDTO;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.repository.*;
import rs.ac.uns.ftn.transport.service.interfaces.IReviewService;

import java.util.Optional;
import java.util.Set;

@Service
public class ReviewServiceImpl implements IReviewService {
    private final VehicleReviewRepository vehicleReviewRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final DriverReviewRepository driverReviewRepository;
    private final RideRepository rideRepository;

    public ReviewServiceImpl(VehicleReviewRepository vehicleReviewRepository, DriverReviewRepository driverReviewRepository,
                             VehicleRepository vehicleRepository, DriverRepository driverRepository, RideRepository rideRepository){
        this.rideRepository = rideRepository;
        this.vehicleReviewRepository = vehicleReviewRepository;
        this.driverReviewRepository = driverReviewRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public VehicleReview saveVehicleReview(VehicleReview review) {
        return vehicleReviewRepository.save(review);
    }

    public Set<VehicleReview> getVehicleReviewsofVehicle(Integer id){
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if(!vehicleOptional.isPresent()){
            return null;
        }
        Vehicle vehicle = vehicleOptional.get();
        return vehicleReviewRepository.findByVehicle(vehicle);
    }
    public Set<DriverReview> getDriverReviewsofDriver(Integer id){
        Optional<Driver> driverOptional= driverRepository.findById(id);
        if(!driverOptional.isPresent()){
            return null;
        }
        Driver driver = driverOptional.get();
        return driverReviewRepository.findByDriver(driver);
    }

    public DriverReview saveDriverReview(DriverReview driverReview){return driverReviewRepository.save(driverReview);}

    @Override
    public ReviewRideDTO getReviewsForRide(Integer rideId) {
        Optional<Ride> rideO = rideRepository.findById(rideId);
        if(!rideO.isPresent())
            return null;
        Ride ride = rideO.get();
        VehicleReview vehicleReview = vehicleReviewRepository.findByCurrentRide(ride);
        DriverReview driverReview = driverReviewRepository.findByCurrentRide(ride);
        return new ReviewRideDTO(new DriverReviewDTO(driverReview), new VehicleReviewDTO(vehicleReview));
    }
}
