package com.yumiko.forces.controllers;

import com.yumiko.forces.judge.Executor;
import com.yumiko.forces.judge.Marker;
import com.yumiko.forces.models.Challenge;
import com.yumiko.forces.models.Contribution;
import com.yumiko.forces.repositories.CaseRepo;
import com.yumiko.forces.repositories.ChallengeRepo;
import com.yumiko.forces.repositories.ContributionRepo;
import com.yumiko.forces.util.Token;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private ContributionRepo contributionRepo;

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Optional<Challenge>> getChallengeById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(challengeRepo.findById(id));
    }

    @PostMapping
    public @ResponseBody ResponseEntity<?> postNewChallenge(@ModelAttribute("challenge") Challenge challenge, HttpSession session) {

        String id = "";
        try {
            if (session.getAttribute("auth") == null || !Token.validateToken(session.getAttribute("auth").toString())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
            }
            id = Token.extractId(session.getAttribute("auth").toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

        if (challenge.getTopic() == null || challenge.getTopic().isEmpty() || challenge.getProblem() == null || challenge.getProblem().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
        }

        contributionRepo.save(new Contribution(id, challenge.getTopic(), challenge.getProblem()));
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> submitChallenge(@RequestParam("source") MultipartFile source,@RequestParam("language") String language, @PathVariable("id") int problem_id, HttpSession session) {

        String id = "";
        try {
            if (session.getAttribute("auth") == null || !Token.validateToken(session.getAttribute("auth").toString())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
            }
            id = Token.extractId(session.getAttribute("auth").toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

        Executor.storeSourceFile(source);
        Marker.perform(problem_id,language ,caseRepo);

        return ResponseEntity.status(HttpStatus.OK).body(Marker.getResult());
    }

}
