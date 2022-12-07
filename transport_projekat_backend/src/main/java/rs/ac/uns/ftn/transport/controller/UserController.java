package rs.ac.uns.ftn.transport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.*;
import rs.ac.uns.ftn.transport.dto.ride.RidePage2DTO;
import rs.ac.uns.ftn.transport.mapper.RideDTOMapper;
import rs.ac.uns.ftn.transport.mapper.UserDTOMapper;
import rs.ac.uns.ftn.transport.model.Message;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.User;
import rs.ac.uns.ftn.transport.model.enumerations.MessageType;
import rs.ac.uns.ftn.transport.service.interfaces.IDriverService;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    private final IUserService userService;
    private final IDriverService driverService;
    private final IPassengerService passengerService;

    private final IRideService rideService;

    public UserController(IUserService userService, IDriverService driverService, IPassengerService passengerService,
                          IRideService rideService){
        this.rideService = rideService;
        this.passengerService = passengerService;
        this.driverService = driverService;
        this.userService = userService;
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePage2DTO> findRides(@PathVariable Integer id, Pageable page){
        Page<Ride> rides = rideService.findPassenger(id, (page));
        if(rides.isEmpty()){
            rides = rideService.findAllByDriver_Id(id, (page));
        }
        Set<RideDTO> rideDTOs = rides.stream()
                .map(RideDTOMapper:: fromRidetoDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(new RidePage2DTO(rides.getTotalElements(),rideDTOs),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserPageDTO> findUsers(Pageable page){
        Page<User> users = userService.findAll(page);
        Set<UserDTO> userDTOS = new HashSet<>();
        System.err.println(users.getTotalElements());
        if(users.getTotalElements() != 0)
             userDTOS = users.stream().map(UserDTOMapper::fromUsertoDTO).collect(Collectors.toSet());

        return new ResponseEntity<>(new UserPageDTO(users.getTotalElements(), userDTOS), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody Passenger user){
        user = userService.findByLogin(user);
        TokenDTO token = userService.saveToken(user);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/message")
    public ResponseEntity<MessagePageDTO> findUserMessages(@PathVariable Integer id){
        Set<MessageDTO> messageDTOS = userService.findMessagesOfUser(id);
        return new ResponseEntity<>(new MessagePageDTO((long) messageDTOS.size(), messageDTOS), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/message")
    public ResponseEntity<MessageLightDTO> createMessage(@PathVariable Integer id, @RequestBody MessageLightDTO messageLight){
        Message message = new Message();
        message.setSender(userService.findOne(id));
        message.setReceiver(userService.findOne(messageLight.getReceiverId()));
        message.setMessage(messageLight.getMessage());
        message.setSentDateTime(LocalDateTime.now());
        message.setMessageType(MessageType.VOZNJA);
        message.setRide(rideService.findOne(id));
        message = userService.SaveMessage(message);

        messageLight.setId(message.getId());
        messageLight.setTimeOfSending(message.getSentDateTime());
        messageLight.setSenderId(id);
        return new ResponseEntity<>(messageLight, HttpStatus.CREATED);
    }
}
