package com.crestasom.pps.service;

import com.crestasom.pps.model.AddBatteryRequest;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.GetBatteryListRequest;
import com.crestasom.pps.model.GetBatteryListResponse;

public interface BatteryService {

	AddBatteryResponse storeBatteryInfo(AddBatteryRequest request);

	GetBatteryListResponse getBatteryList(GetBatteryListRequest request);
	
	void removeAllBatteries();

}
