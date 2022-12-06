package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import rs.ac.uns.ftn.transport.model.Passenger;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.model.Ride;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IPassengerService {

    Passenger save(Passenger passenger);
    Passenger findOne(Integer id);
    Page<Passenger> findAll(Pageable page);
    Page<Ride> findRidesBetweenTimeRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);
}
