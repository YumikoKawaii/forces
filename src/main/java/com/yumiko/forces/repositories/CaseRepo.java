package com.yumiko.forces.repositories;

import com.yumiko.forces.models.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaseRepo extends JpaRepository<Case, Integer> {

    @Query(value = "select * from cases c where c.problem_id = problem_id order by c.id asc", nativeQuery = true)
    List<Case> findAllCaseByProblemId(int problem_id);

}
