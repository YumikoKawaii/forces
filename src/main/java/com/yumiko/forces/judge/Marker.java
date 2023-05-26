package com.yumiko.forces.judge;

import com.yumiko.forces.models.Case;
import com.yumiko.forces.repositories.CaseRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class Detail {

    public boolean accepted;
    public String answer;
    public String expected;

    public Detail(boolean accepted, String answer, String expected) {
        this.accepted = accepted;
        this.answer = answer;
        this.expected = expected;
    }
}

@Service
public class Marker {

    private static final Logger logger = Logger.getLogger(Marker.class.getName());
    private static List<Case> cases = new ArrayList<>();
    private static List<Detail> details = new ArrayList<>();
    private static int point;

    public static void perform(int problem_id, String language, CaseRepo caseRepo) {

        point = 0;
        cases = caseRepo.findAllCaseByProblemId(problem_id);

        if (cases.isEmpty()) {
            logger.warning("Test case for problem: " + problem_id + " is empty!");
        }

        if (!cases.isEmpty()) {

            Executor.compile(language);

            List<Detail> detail_temp = new ArrayList<>();

            for (Case i : cases) {
                Executor.createFile(i.getInput(), Executor.inputFile);

                Executor.execute(language);

                String output = Executor.getContentFromFile(Executor.outputFile);

                boolean isRight = output.equals(i.getOutput());
                detail_temp.add(new Detail(isRight, output, i.getOutput()));
                if (isRight) point++;

            }

            details = detail_temp;
            Executor.removeAll();

        }

    }

    public static Result getResult() {
        return new Result(point, cases.size(), details);
    }

}
