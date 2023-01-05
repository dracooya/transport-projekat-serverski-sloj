package rs.ac.uns.ftn.transport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User{

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Document> documents;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Ride> rides;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    @ToString.Exclude
    @JsonIgnore
    private Vehicle vehicle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Driver driver = (Driver) o;
        return getId() != null && Objects.equals(getId(), driver.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
