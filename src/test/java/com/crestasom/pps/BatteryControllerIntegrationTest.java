package com.crestasom.pps;

import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import com.crestasom.pps.dto.BatteryDTO;
import com.crestasom.pps.model.AddBatteryResponse;
import com.crestasom.pps.model.GetBatteryListResponse;
import com.crestasom.pps.service.BatteryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PpsApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "src/docs")
@TestPropertySource(locations = "classpath:application-test.properties")
class BatteryControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BatteryService service;

	void insertMockRecord() {
		service.removeAllBatteries();
		List<BatteryDTO> bList = new ArrayList<>();
		bList.add(new BatteryDTO("battery1", 44600, 220));
		bList.add(new BatteryDTO("battery2", 44700, 280));
		bList.add(new BatteryDTO("battery3", 44800, 190));
		service.storeBatteryInfo(bList);
	}

	@Test
	void testInsertMockRecord() throws Exception {
		List<BatteryDTO> bList = new ArrayList<>();
		bList.add(new BatteryDTO("battery1", 44600, 220));
		bList.add(new BatteryDTO("battery2", 44700, 280));
		bList.add(new BatteryDTO("battery3", 44800, 190));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = objtoJson(bList);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/add-batteries").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(document("add-batteries", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.andReturn();
		AddBatteryResponse resp = mapper.readValue(result.getResponse().getContentAsString(), AddBatteryResponse.class);
		assertEquals(200, resp.getRespCode().intValue());
	}

	@Test
	void testInsertMockRecordNameValidationFailed() throws Exception {
		List<BatteryDTO> bList = new ArrayList<>();
		BatteryDTO dto = new BatteryDTO();
		dto.setCapacity(200);
		dto.setPostcode(44600);
		bList.add(dto);
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = objtoJson(bList);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/add-batteries")
				.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
		AddBatteryResponse resp = mapper.readValue(result.getResponse().getContentAsString(), AddBatteryResponse.class);
		assertEquals(400, resp.getRespCode().intValue());
		assertEquals("Name should not be empty", resp.getRespDesc());
	}

	@Test
	void testGetBatteryList() throws Exception {
		insertMockRecord();
		ObjectMapper mapper = new ObjectMapper();
		Integer postCodeStart = 44600;
		Integer posCodeEnd = 44700;
		String requestUri = String.format("/get-batteries?postCodeStart=%d&postCodeEnd=%d", postCodeStart, posCodeEnd);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(document("get-battery-list",
						preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
				.andReturn();
		GetBatteryListResponse resp = mapper.readValue(result.getResponse().getContentAsString(),
				GetBatteryListResponse.class);
		assertEquals(200, resp.getRespCode().intValue());
		assertEquals(2, resp.getBatteryNames().size());
		assertEquals(500, resp.getTotalBatteryCapacity().intValue());
	}

	@Test
	void testGetBatteryListWhenNoRecord() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Integer postCodeStart = 44600;
		Integer posCodeEnd = 44700;
		String requestUri = String.format("/get-batteries?postCodeStart=%d&postCodeEnd=%d", postCodeStart, posCodeEnd);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		GetBatteryListResponse resp = mapper.readValue(result.getResponse().getContentAsString(),
				GetBatteryListResponse.class);
		assertEquals(404, resp.getRespCode().intValue());
		assertEquals(null, resp.getBatteryNames());
	}

	static String objtoJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(obj);
		return requestJson;
	}

}
