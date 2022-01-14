package com.example.magazinestand.controller;

import com.example.magazinestand.dto.MagazineDto;
import com.example.magazinestand.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Sql(value = {"classpath:drop.sql", "classpath:init.sql"})
class MagazineControllerTest {

	private static final Long ID = 1L;
	private static final String TITLE = "Reader's Digest";
	private static final BigDecimal PRICE = BigDecimal.valueOf(3.99);
	private static final LocalDate DATE_OF_ISSUE = LocalDate.of(2021, 10, 1);
	private static final String FILE_LOCATION = "/magazines/Reader's-Digest-2021-10-01.pdf";
	private static final String EXISTING_TITLE = "National Geographic";
	private static final int PREEXISTING_MAGAZINES_COUNT = 5;

	private MockMvc mockMvc;
	@MockBean
	private CollectorRegistry registry;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testCreateMagazine() throws Exception{
		mockMvc.perform(post("/magazines")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createMagazineDto())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value(TITLE));
	}

	@Test
	void testGetMagazineById() throws Exception{
		mockMvc.perform(get("/magazines/" + ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ID));
	}

	@Test
	void testGetMagazineByIdWithNotExistingId() throws Exception{
		mockMvc.perform(get("/magazines/" + 1000))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
	}

	@Test
	void testGetAll() throws Exception{
		mockMvc.perform(get("/magazines"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(PREEXISTING_MAGAZINES_COUNT)));
	}

	@Test
	void testGetMagazinesByTitle() throws Exception{
		mockMvc.perform(get("/magazines")
						.param("title", EXISTING_TITLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value(EXISTING_TITLE));
	}

	@Test
	void testUpdateMagazine() throws Exception{
		mockMvc.perform(put("/magazines/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createMagazineDto())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ID))
				.andExpect(jsonPath("$.title").value(TITLE));
	}

	@Test
	void testUpdateMagazineWithNotExistingId() throws Exception{
		mockMvc.perform(put("/magazines/" + 1000)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createMagazineDto())))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
	}

	@Test
	void testDeleteMagazine() throws Exception{
		mockMvc.perform(delete("/magazines")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(1L)))
				.andExpect(status().isOk());
		mockMvc.perform(get("/magazines"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(PREEXISTING_MAGAZINES_COUNT - 1)));
	}

	private MagazineDto createMagazineDto() {
		var magazine = new MagazineDto();
		magazine.setTitle(TITLE);
		magazine.setPrice(PRICE);
		magazine.setDateOfIssue(DATE_OF_ISSUE);
		magazine.setFileLocation(FILE_LOCATION);
		return magazine;
	}
}
