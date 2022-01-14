package com.example.magazinestand.service.impl;

import com.example.magazinestand.dto.MagazineDto;
import com.example.magazinestand.entity.Magazine;
import com.example.magazinestand.exception.EntityNotFoundException;
import com.example.magazinestand.repository.MagazineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MagazineServiceImplTest {

	private static final Long ID = 1L;
	private static final String TITLE = "Reader's Digest";
	private static final BigDecimal PRICE = BigDecimal.valueOf(3.99);
	private static final LocalDate DATE_OF_ISSUE = LocalDate.of(2021, 10, 1);
	private static final String FILE_LOCATION = "/magazines/Reader's-Digest-2021-10-01.pdf";
	private static final String EXISTING_TITLE = "National Geographic";
	private static final int PREEXISTING_MAGAZINES_COUNT = 5;

	@Mock
	private MagazineRepository mockRepository;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private MagazineServiceImpl service;

	@Test
	void createMagazineTest(){
		when(mockRepository.save(any(Magazine.class))).thenReturn(createMagazine());
		var magazineDto = createMagazineDto();
		var savedMagazine = service.createMagazine(magazineDto);
		assertEquals(ID, savedMagazine.getId());
		assertEquals(TITLE, savedMagazine.getTitle());
	}

	@Test
	void getMagazineByIdTestWithExistingId(){
		when(mockRepository.findById(ID)).thenReturn(Optional.of(createMagazine()));
		var savedMagazine = service.getMagazine(ID);
		assertEquals(ID, savedMagazine.getId());
		assertEquals(TITLE, savedMagazine.getTitle());
	}

	@Test
	void getMagazineByIdTestWithNotExistingId(){
		when(mockRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> service.getMagazine(ID));
	}

	@Test
	void getMagazinesByTitleTest(){
		when(mockRepository.findByTitleContainingIgnoreCase(TITLE)).thenReturn(List.of(createMagazine()));
		var magazines = service.getMagazinesByTitle(TITLE);
		assertEquals(1, magazines.size());
		assertEquals(TITLE, magazines.get(0).getTitle());
	}

	@Test
	void getAllMagazinesTest(){
		when(mockRepository.findAll()).thenReturn(List.of(createMagazine()));
		var magazines = service.getAll();
		assertEquals(1, magazines.size());
		assertEquals(TITLE, magazines.get(0).getTitle());
	}

	@Test
	void deleteTest(){
		service.deleteMagazine(ID);
		verify(mockRepository, times(1)).deleteById(ID);
	}

	private MagazineDto createMagazineDto() {
		var magazine = new MagazineDto();
		magazine.setTitle(TITLE);
		magazine.setPrice(PRICE);
		magazine.setDateOfIssue(DATE_OF_ISSUE);
		magazine.setFileLocation(FILE_LOCATION);
		return magazine;
	}

	private Magazine createMagazine() {
		var magazine = new Magazine();
		magazine.setId(ID);
		magazine.setTitle(TITLE);
		magazine.setPrice(PRICE);
		magazine.setDateOfIssue(DATE_OF_ISSUE);
		magazine.setFileLocation(FILE_LOCATION);
		return magazine;
	}

}
