package rs.ac.uns.ftn.transport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerCreatedDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerPageDTO;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;

    public PassengerController(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> savePassenger(@RequestBody PassengerDTO passenger)
    {
        Passenger created = passengerService.save(PassengerDTOMapper.fromDTOtoPassenger(passenger));
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerCreatedDTO> findPassenger(@PathVariable Integer id)
    {
        Passenger retrieved = passengerService.findOne(id);
        if (retrieved == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> updatePassenger(@PathVariable Integer id, @RequestBody PassengerDTO newInfo)
    {
        Passenger retrieved = passengerService.findOne(id);
        if(retrieved == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        retrieved.update(newInfo);
        passengerService.save(retrieved);
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PassengerPageDTO> getPassengers(Pageable page) {
        Page<Passenger> passengers = passengerService.findAll(page);

        Set<PassengerCreatedDTO> passengerCreatedDTOs = passengers.stream()
                .map(PassengerCreatedDTOMapper :: fromPassengerToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new PassengerPageDTO(passengers.getTotalElements(), passengerCreatedDTOs), HttpStatus.OK);
    }
}
