package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.VehicleReview;
import rs.ac.uns.ftn.transport.repository.VehicleReviewRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IReviewService;

import java.util.Set;

@Service
public class ReviewServiceImpl implements IReviewService {
    private final VehicleReviewRepository vehicleReviewRepository;

    public ReviewServiceImpl(VehicleReviewRepository vehicleReviewRepository){this.vehicleReviewRepository = vehicleReviewRepository;}

    public VehicleReview saveVehicleReview(VehicleReview review) {
        return vehicleReviewRepository.save(review);
    }

    public Set<VehicleReview> getVehicleReviewsofVehicle(Integer id){return vehicleReviewRepository.findByVehicleId(id);}
}
