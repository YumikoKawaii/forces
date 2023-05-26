package com.yumiko.forces.controllers;

import com.yumiko.forces.judge.Executor;
import com.yumiko.forces.judge.Marker;
import com.yumiko.forces.judge.Result;
import com.yumiko.forces.models.Challenge;
import com.yumiko.forces.repositories.CaseRepo;
import com.yumiko.forces.repositories.ChallengeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path = "/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeRepo challengeRepo;
    @Autowired
    private CaseRepo caseRepo;

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Optional<Challenge>> getChallengeById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(challengeRepo.findById(id));
    }

    @PostMapping
    public @ResponseBody ResponseEntity<String> postNewChallenge(@ModelAttribute("challenge") Challenge challenge) {

        if (challenge.getTopic().isEmpty() || challenge.getProblem().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
        }

        challengeRepo.save(challenge);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Result> submitChallenge(@RequestParam("source") MultipartFile source,@RequestParam("language") String language, @PathVariable("id") int problem_id) {

        Executor.storeSourceFile(source);
        Marker.perform(problem_id,language ,caseRepo);

        return ResponseEntity.status(HttpStatus.OK).body(Marker.getResult());
    }

}
