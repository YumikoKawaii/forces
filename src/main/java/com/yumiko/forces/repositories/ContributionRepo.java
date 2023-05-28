package com.yumiko.forces.repositories;

import com.yumiko.forces.models.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepo extends JpaRepository<Contribution, Integer> {
}
