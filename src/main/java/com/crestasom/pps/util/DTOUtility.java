package com.crestasom.pps.util;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.Battery;

import lombok.Generated;

public class DTOUtility {
	@Generated
	private DTOUtility() {
		
	}
	
	public static Battery convertBatteryDTOToBattery(BatteryDTO batteryDto) {
		return Battery.builder().name(batteryDto.getName()).postcode(batteryDto.getPostcode())
				.capacity(batteryDto.getCapacity()).build();
	}
}
