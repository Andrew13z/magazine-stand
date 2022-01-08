package com.example.magazinestand.repository;

import com.example.magazinestand.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

	List<Magazine> findByTitleContainingIgnoreCase(String title);
}
