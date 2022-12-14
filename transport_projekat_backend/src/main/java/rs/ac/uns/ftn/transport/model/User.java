package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Table(name = "users")
@Inheritance(strategy = JOINED)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Integer id;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "surname", nullable = false)
    private String surname;

    @Column (name = "profilePicture")
    @Lob
    private String profilePicture;

    @Column (name = "telephoneNumber")
    private String telephoneNumber;

    @Column (name = "email", nullable = false, unique = true)
    private String email;

    @Column (name = "address", nullable = false)
    private String address;

    @Column (name = "password", nullable = false)
    private String password;

    @Column (name = "isActivated")
    private Boolean isActivated;

    @Column (name = "isBlocked")
    private Boolean isBlocked;

    @Column (name = "resetPasswordToken")
    private String resetPasswordToken;

    @Column (name = "resetPasswordTokenExpiration")
    private LocalDateTime resetPasswordTokenExpiration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
