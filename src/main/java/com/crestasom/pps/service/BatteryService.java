package com.crestasom.pps.service;

import java.util.List;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.GetBatteryListResponse;

public interface BatteryService {

	AddBatteryResponse storeBatteryInfo(List<BatteryDTO> batteryList);

	GetBatteryListResponse getBatteryList(Integer postCodeStart,Integer postCodeEnd);
	
	void removeAllBatteries();

}
