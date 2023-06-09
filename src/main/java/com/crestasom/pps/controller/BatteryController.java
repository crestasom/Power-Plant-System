package com.crestasom.pps.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.GetBatteryListResponse;
import com.crestasom.pps.service.BatteryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class BatteryController {

	private BatteryService batteryService;

	@PostMapping("/add-batteries")
	public AddBatteryResponse storeBatteries(@Valid @RequestBody List<BatteryDTO> request) {
		return batteryService.storeBatteryInfo(request);
	}

	@GetMapping("/get-batteries")
	public GetBatteryListResponse getBatteryList(@RequestParam Integer postCodeStart,
			@RequestParam Integer postCodeEnd) {
		return batteryService.getBatteryList(postCodeStart, postCodeEnd);
	}

}
