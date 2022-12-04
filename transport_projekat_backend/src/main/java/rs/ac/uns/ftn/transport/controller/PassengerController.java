package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.transport.dto.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.PassengerCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;

@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;

    public PassengerController(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> savePassenger(PassengerDTO passenger)
    {
        Passenger created = passengerService.save(PassengerDTOMapper.fromDTOtoPassenger(passenger));
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.CREATED);
    }
}