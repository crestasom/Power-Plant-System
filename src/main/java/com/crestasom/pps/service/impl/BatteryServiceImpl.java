package com.crestasom.pps.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.Battery;
import com.crestasom.pps.model.GetBatteryListRequest;
import com.crestasom.pps.model.GetBatteryListResponse;
import com.crestasom.pps.repo.BatteryRepo;
import com.crestasom.pps.service.BatteryService;
import com.crestasom.pps.util.ConfigUtility;
import com.crestasom.pps.util.DTOUtility;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BatteryServiceImpl implements BatteryService {

	private BatteryRepo repo;
	private ConfigUtility configUtility;
	private static final Logger logger = LoggerFactory.getLogger(BatteryServiceImpl.class);

	/**
	 * Service method to store the battery list received
	 * 
	 * @param List<BatteryDTO> batteryList List of battery
	 * @return AddBatteryResponse response consisting of response code and response
	 *         description
	 */
	@Override
	public AddBatteryResponse storeBatteryInfo(List<BatteryDTO> batteryList) {
		logger.info("Received request for storing batteries [{}]", batteryList);
		AddBatteryResponse resp = new AddBatteryResponse();
		logger.debug("storing batteries to db");
		batteryList.stream().map(DTOUtility::convertBatteryDTOToBattery).forEach(b -> repo.save(b));
		logger.debug("storing batteries to db complete");
		resp.setRespCode(configUtility.getPropertyAsInt("server.success.resp.code"));
		resp.setRespDesc(configUtility.getProperty("add.battery.list.success.resp.desc"));
		logger.info("returning response [{}]", resp);
		return resp;
	}

	/**
	 * Service method to get the battery information with in the range of postCode
	 * passed in request
	 * 
	 * @param GetBatteryListRequest request containing range of postCode
	 * @return GetBatteryListResponse response consisting of response code, response
	 *         description, list of battery names matching the post code range
	 *         sorted in ascending order and statistics of result like total battery
	 *         capacity and average battery capacity
	 */
	@Override
	public GetBatteryListResponse getBatteryList(GetBatteryListRequest request) {
		Integer postCodeStart = request.getPostCodeStart();
		Integer postCodeEnd = request.getPostCodeEnd();
		logger.info("Get battery list for post code range [{}] - [{}]", postCodeStart, postCodeEnd);
		List<Battery> bList = repo.findByPostcodeBetween(postCodeStart, postCodeEnd);
		logger.debug("battery list received from db for post code range [{}]", bList);
		GetBatteryListResponse resp = new GetBatteryListResponse();
		if (bList == null || bList.isEmpty()) {
			logger.debug("No battery found for post code range [{}] - [{}]", postCodeStart, postCodeEnd);
			resp.setRespCode(configUtility.getPropertyAsInt("server.not.found.resp.code"));
			resp.setRespDesc(configUtility.getProperty("get.battery.list.not.found.resp.desc"));
		} else {
			resp.setRespCode(configUtility.getPropertyAsInt("server.success.resp.code"));
			resp.setRespDesc(configUtility.getProperty("get.battery.list.success.resp.desc"));
			resp.setBatteryNames(bList.stream().sorted().map(x -> x.getName()).toList());
			resp.setTotalBatteryCapacity(bList.stream().mapToInt(b -> b.getCapacity()).sum());
			resp.setAverageBatteryCapacity(bList.stream().mapToInt(b -> b.getCapacity()).average().orElse(0.0));
		}
		logger.info("returning response [{}]", resp);
		return resp;
	}

	@Override
	public void removeAllBatteries() {
		logger.info("removing all batteries from database");
		repo.deleteAll();

	}

}
