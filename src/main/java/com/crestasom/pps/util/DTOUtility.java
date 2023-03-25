package com.crestasom.pps.util;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.Battery;

public class DTOUtility {
	public static Battery convertBatteryDTOToBattery(BatteryDTO batteryDto) {
		return Battery.builder().name(batteryDto.getName()).postcode(batteryDto.getPostcode())
				.capacity(batteryDto.getCapacity()).build();
	}
}
