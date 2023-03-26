package com.crestasom.pps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.Battery;
import com.crestasom.pps.model.GetBatteryListResponse;
import com.crestasom.pps.repo.BatteryRepo;
import com.crestasom.pps.service.impl.BatteryServiceImpl;
import com.crestasom.pps.util.ConfigUtility;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BatteryServiceUnitTest {
	@Mock
	BatteryRepo repo;
	@Mock
	ConfigUtility util;
	@InjectMocks
	BatteryServiceImpl service;
	private static Logger logger = LoggerFactory.getLogger(BatteryServiceUnitTest.class);

	@BeforeEach
	void setUp() {
		logger.debug("starting setup");
		Mockito.when(repo.save(any())).thenReturn(null);
		List<Battery> bList = new ArrayList<>();
		bList.add(new Battery(1, "b1", 44600, 220));
		Battery b2 = new Battery();
		b2.setName("b2");
		b2.setPostcode(44700);
		b2.setCapacity(230);
		bList.add(b2);
		bList.add(new Battery(3, "b3", 44800, 240));
		Mockito.when(repo.findByPostcodeBetween(44600, 44700)).thenReturn(bList);
		Mockito.when(repo.findByPostcodeBetween(446800, 44900)).thenReturn(null);
		Mockito.when(util.getPropertyAsInt("server.success.resp.code")).thenReturn(200);
		Mockito.when(util.getPropertyAsInt("server.not.found.resp.code")).thenReturn(404);
		Mockito.doNothing().when(repo).deleteAll();

	}

	@Test
	void testInsertBatteries() {
		List<BatteryDTO> bList = new ArrayList<>();
		bList.add(new BatteryDTO("battery1", 44600, 220));
		bList.add(new BatteryDTO("battery2", 44700, 280));
		bList.add(new BatteryDTO("battery3", 44800, 190));
		bList.forEach(b -> logger.debug("battery [{}]", b));
		AddBatteryResponse resp = service.storeBatteryInfo(bList);
		assertEquals(Integer.valueOf(200), resp.getRespCode());

	}

	@Test
	void testGetBatteries() {
		Integer postCodeStart = 44600;
		Integer posCodeEnd = 44700;
		GetBatteryListResponse resp = service.getBatteryList(postCodeStart, posCodeEnd);
		assertEquals(200, resp.getRespCode().intValue());
		assertEquals(Integer.valueOf(690), resp.getTotalBatteryCapacity());
		assertEquals(Double.valueOf(230), resp.getAverageBatteryCapacity());

	}

	@Test
	void testGetBatteriesNoResult() {
		Integer postCodeStart = 446800;
		Integer posCodeEnd = 44900;
		GetBatteryListResponse resp = service.getBatteryList(postCodeStart, posCodeEnd);
		assertEquals(404, resp.getRespCode().intValue());
	}
}
