package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;

//TODO:Pitacu za zivotni ve sta predstavlja

@Entity
@Table(name = "userActivations")
@Data
public class UserActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "userActivation")
    private User user;

    @Column(name = "dateCreated")
    private SimpleDateFormat dateCreated;

    @Column(name = "validUntil")
    private SimpleDateFormat validUntil;
}