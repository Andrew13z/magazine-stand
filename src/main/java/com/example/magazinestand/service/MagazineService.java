package com.example.magazinestand.service;

import com.example.magazinestand.dto.MagazineDto;

import java.util.List;

public interface MagazineService {

	MagazineDto createMagazine(MagazineDto magazine);

	MagazineDto getMagazine(Long id);

	List<MagazineDto> getMagazinesByTitle(String title);

	List<MagazineDto> getAll();

	MagazineDto updateMagazine(Long id, MagazineDto magazineDto);

	void deleteMagazine(Long id);
}
