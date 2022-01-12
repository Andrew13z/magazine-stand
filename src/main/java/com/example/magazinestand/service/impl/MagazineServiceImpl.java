package com.example.magazinestand.service.impl;

import com.example.magazinestand.dto.MagazineDto;
import com.example.magazinestand.entity.Magazine;
import com.example.magazinestand.exception.EntityNotFoundException;
import com.example.magazinestand.repository.MagazineRepository;
import com.example.magazinestand.service.MagazineService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagazineServiceImpl implements MagazineService {

	private final MagazineRepository repository;
	private final ModelMapper mapper;

	@Autowired
	public MagazineServiceImpl(MagazineRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public MagazineDto createMagazine(MagazineDto magazine) {
		var savedMagazine = repository.save(mapper.map(magazine, Magazine.class));
		return mapper.map(savedMagazine, MagazineDto.class);
	}

	@Override
	public MagazineDto getMagazine(Long id) {
		var magazine = repository.findById(id).orElseThrow(
										() -> new EntityNotFoundException("Magazine not found by id: " + id));
		return mapper.map(magazine, MagazineDto.class);
	}

	@Override
	public List<MagazineDto> getMagazinesByTitle(String title) {
		var magazine = repository.findByTitleContainingIgnoreCase(title);
		return mapper.map(magazine, new TypeToken<List<MagazineDto>>() {}.getType());
	}

	@Override
	public List<MagazineDto> getAll() {
		var allMagazines = repository.findAll();
		return mapper.map(allMagazines, new TypeToken<List<MagazineDto>>() {}.getType());
	}

	@Override
	public MagazineDto updateMagazine(Long id, MagazineDto magazineDto) {
		magazineDto.setId(id);
		var updatedMagazine = repository.save(mapper.map(magazineDto, Magazine.class));
		return mapper.map(updatedMagazine, MagazineDto.class);
	}

	@Override
	public void deleteMagazine(Long id) {
		repository.deleteById(id);
	}

	@Override
	public long countAllMagazines() {
		return repository.count();
	}
}
