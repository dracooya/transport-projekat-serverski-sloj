package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.WorkingHours;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Integer> {

}
