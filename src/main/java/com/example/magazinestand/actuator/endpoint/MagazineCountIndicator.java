package com.example.magazinestand.actuator.endpoint;

import com.example.magazinestand.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MagazineCountIndicator implements HealthIndicator {

	private final MagazineService service;

	@Autowired
	public MagazineCountIndicator(MagazineService service) {
		this.service = service;
	}

	@Override
	public Health health() {
		var count = service.countAllMagazines();
		return Health.up().withDetail("count", count).build();
	}
}
