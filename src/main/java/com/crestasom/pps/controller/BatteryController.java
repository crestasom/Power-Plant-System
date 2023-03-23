package com.crestasom.pps.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crestasom.pps.model.AddBatteryRequest;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.GetBatteryListRequest;
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
	public AddBatteryResponse storeBatteries(@Valid @RequestBody AddBatteryRequest request) {
		return batteryService.storeBatteryInfo(request);
	}

	@GetMapping("/get-batteries")
	public GetBatteryListResponse getBatteryList(@RequestBody GetBatteryListRequest request) {
		return batteryService.getBatteryList(request);
	}

}
