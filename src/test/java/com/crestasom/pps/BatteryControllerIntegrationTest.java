package com.crestasom.pps;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryRequest;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.GetBatteryListRequest;
import com.crestasom.pps.model.GetBatteryListResponse;
import com.crestasom.pps.service.BatteryService;
import com.crestasom.pps.util.PPSUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PpsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class BatteryControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BatteryService service;

	public void insertMockRecord() {
		service.removeAllBatteries();
		List<BatteryDTO> bList = new ArrayList<>();
		bList.add(new BatteryDTO("battery1", 44600, 220));
		bList.add(new BatteryDTO("battery2", 44700, 280));
		bList.add(new BatteryDTO("battery3", 44800, 190));
		service.storeBatteryInfo(bList);
	}

	@Test
	public void testInsertMockRecord() throws Exception {
		List<BatteryDTO> bList = new ArrayList<>();
		bList.add(new BatteryDTO("battery1", 44600, 220));
		bList.add(new BatteryDTO("battery2", 44700, 280));
		bList.add(new BatteryDTO("battery3", 44800, 190));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = objtoJson(bList);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/add-batteries?reqId=" + PPSUtils.getUUID())
						.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		AddBatteryResponse resp = mapper.readValue(result.getResponse().getContentAsString(), AddBatteryResponse.class);
		assertEquals(resp.getRespCode().intValue(), 200);
	}

	@Test
	public void testInsertMockRecordNameValidationFailed() throws Exception {
		List<BatteryDTO> bList = new ArrayList<>();
		BatteryDTO dto = new BatteryDTO();
		dto.setCapacity(200);
		dto.setPostcode(44600);
		bList.add(dto);
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = objtoJson(bList);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/add-batteries?reqId=" + PPSUtils.getUUID())
						.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
		AddBatteryResponse resp = mapper.readValue(result.getResponse().getContentAsString(), AddBatteryResponse.class);
		assertEquals(resp.getRespCode().intValue(), 400);
		assertEquals(resp.getRespDesc(), "Name should not be empty");
	}

	@Test
	public void testGetBatteryList() throws Exception {
		insertMockRecord();
		GetBatteryListRequest req = new GetBatteryListRequest();
		req.setPostCodeStart(44600);
		req.setPostCodeEnd(44700);
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = objtoJson(req);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/get-batteries?reqId=" + PPSUtils.getUUID())
						.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		GetBatteryListResponse resp = mapper.readValue(result.getResponse().getContentAsString(),
				GetBatteryListResponse.class);
		assertEquals(resp.getRespCode().intValue(), 200);
		assertEquals(resp.getBatteryNames().size(), 2);
		assertEquals(resp.getTotalBatteryCapacity().intValue(), 500);
	}

	@Test
	public void testGetBatteryListWhenNoRecord() throws Exception {
		GetBatteryListRequest req = new GetBatteryListRequest();
		req.setPostCodeStart(44600);
		req.setPostCodeEnd(44700);
		String requestJson = objtoJson(req);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/get-batteries?reqId=" + PPSUtils.getUUID())
						.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		GetBatteryListResponse resp = mapper.readValue(result.getResponse().getContentAsString(),
				GetBatteryListResponse.class);
		assertEquals(resp.getRespCode().intValue(), 404);
		assertEquals(resp.getBatteryNames(), null);
	}

	public static String objtoJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(obj);
		return requestJson;
	}

}
