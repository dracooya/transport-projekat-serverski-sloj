package rs.ac.uns.ftn.transport.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerWithoutIdPasswordDTO;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends User{

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "passenger_Ride", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RideId", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Ride> rides;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "passenger_FavoriteRoute", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RouteId", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Route> favorites;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Passenger passenger = (Passenger) o;
        return getId() != null && Objects.equals(getId(), passenger.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void update(PassengerWithoutIdPasswordDTO newInfo)
    {
        this.setName(newInfo.getName());
        this.setSurname(newInfo.getSurname());
        this.setProfilePicture(newInfo.getProfilePicture());
        this.setAddress(newInfo.getAddress());
        this.setTelephoneNumber(newInfo.getTelephoneNumber());
        this.setEmail(newInfo.getEmail());
    }
}
