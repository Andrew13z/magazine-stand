package com.example.magazinestand.controller;

import com.example.magazinestand.dto.MagazineDto;
import com.example.magazinestand.service.MagazineService;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/magazines")
public class MagazineController {

	private final MagazineService service;
	private final Histogram requestHistogram;

	@Autowired
	public MagazineController(MagazineService service, CollectorRegistry collectorRegistry) {
		this.service = service;
		requestHistogram = Histogram.build()
				.name("create_reqeust_duration")
				.help("Time spent prossesing create reqeust.")
				.register(collectorRegistry);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MagazineDto createMagazine(@RequestBody MagazineDto magazine) {
		Histogram.Timer timer = requestHistogram.startTimer();

		var createdMagazine = service.createMagazine(magazine);

		timer.observeDuration();
		return createdMagazine;
	}

	@GetMapping("/{id}")
	public MagazineDto getMagazine(@PathVariable("id") Long id) {
		return service.getMagazine(id);
	}

	@GetMapping
	public List<MagazineDto> getAll(@RequestParam(value = "title", required = false) String title) {
		if (title != null) {
			return service.getMagazinesByTitle(title);
		}
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
