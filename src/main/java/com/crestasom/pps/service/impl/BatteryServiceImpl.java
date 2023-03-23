package com.crestasom.pps.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryRequest;
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

	@Override
	public AddBatteryResponse storeBatteryInfo(AddBatteryRequest request) {
		logger.info("Received request for storing batteries [{}]", request);
		AddBatteryResponse resp = new AddBatteryResponse();
		resp.setReqId(request.getReqId());
		List<BatteryDTO> batteryList = request.getBatteryList();
		logger.debug("storing batteries to db");
		batteryList.stream().map(b -> DTOUtility.convertBatteryDTOToBattery(b)).forEach(b -> repo.save(b));
		logger.debug("storing batteries to db complete");
		resp.setRespCode(configUtility.getPropertyAsInt("server.success.resp.code"));
		resp.setRespDesc(configUtility.getProperty("add.battery.list.success.resp.desc"));
		logger.info("returning response [{}]", resp);
		return resp;
	}

	@Override
	public GetBatteryListResponse getBatteryList(GetBatteryListRequest request) {
		Integer postCodeStart = request.getPostCodeStart();
		Integer postCodeEnd = request.getPostCodeEnd();
		logger.info("Get battery list for post code range [{}] - [{}]", postCodeStart, postCodeEnd);
		List<Battery> bList = repo.findByPostCodeBetween(postCodeStart, postCodeEnd);
		logger.debug("battery list received from db for post code range [{}]", bList);
		GetBatteryListResponse resp = new GetBatteryListResponse();
		resp.setReqId(request.getReqId());
		if (bList == null || bList.size() == 0) {
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
