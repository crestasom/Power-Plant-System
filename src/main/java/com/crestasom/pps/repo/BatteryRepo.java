package com.crestasom.pps.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.crestasom.pps.model.Battery;

public interface BatteryRepo extends CrudRepository<Battery, Integer> {

	public List<Battery> findByPostCodeBetween(Integer postCodeStart, Integer postCodeEnd);
}
