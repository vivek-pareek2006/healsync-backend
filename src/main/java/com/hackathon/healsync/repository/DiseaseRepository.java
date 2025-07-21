package com.hackathon.healsync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.Disease;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer> {
}
