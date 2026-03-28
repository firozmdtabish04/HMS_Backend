package com.application.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.application.model.Slots;

public interface SlotBookingRepository extends CrudRepository<Slots, Integer> {
	List<Slots> findByEmail(String email);
}