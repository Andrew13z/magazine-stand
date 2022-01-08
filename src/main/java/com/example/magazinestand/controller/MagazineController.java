package com.example.magazinestand.controller;

import com.example.magazinestand.dto.MagazineDto;
import com.example.magazinestand.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/magazines")
public class MagazineController {

	private final MagazineService service;

	@Autowired
	public MagazineController(MagazineService service) {
		this.service = service;
	}

	@PostMapping
	public MagazineDto createMagazine(@RequestBody MagazineDto magazine) {
		return service.createMagazine(magazine);
	}

	@GetMapping("/{id}")
	public MagazineDto getMagazine(@PathVariable("id") Long id) {
		return service.getMagazine(id);
	}

	@GetMapping(params = "title")
	public List<MagazineDto> getMagazine(@RequestParam("title") String title) {
		return service.getMagazinesByTitle(title);
	}

	@GetMapping
	public List<MagazineDto> getAll(@RequestParam(value = "title", required = false) String title) {
		return service.getAll();
	}

	@PutMapping("/{id}")
	public MagazineDto updateMagazine(@PathVariable("id") Long id,
									  @RequestBody MagazineDto magazine) {
		return service.updateMagazine(id, magazine);
	}

	@DeleteMapping
	public void deleteMagazine(@RequestBody Long id) {
		service.deleteMagazine(id);
	}
}
