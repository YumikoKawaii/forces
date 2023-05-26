package com.yumiko.forces.judge;

import com.yumiko.forces.models.Case;
import com.yumiko.forces.repositories.CaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static void perform (int problem_id, CaseRepo caseRepo) {

        point = 0;
        cases = caseRepo.findAllCaseByProblemId(problem_id);

        if (cases.isEmpty()) {
            logger.warning("Test case for problem: " + problem_id + " is empty!");
        }

        if (!cases.isEmpty()) {

            List<Detail> detail_temp = new ArrayList<>();

            for (Case i : cases) {
                Executor.createFile(i.getInput(),Executor.inputFile);
                Executor.createFile(i.getOutput(), Executor.keyFile);

                Executor.executeFile();

                boolean isRight = Executor.isRightAnswer();
                detail_temp.add(new Detail(isRight, i.getOutput(),Executor.getOutput()));
                if (isRight) point++;

            }

            details = detail_temp;
            Executor.deleteFile(Executor.inputFile);
            Executor.deleteFile(Executor.outputFile);
            Executor.deleteFile(Executor.keyFile);
            Executor.deleteFile(Executor.executable);
            Executor.deleteFile(Executor.sourceFile);

        }

    }

    public static Result getResult() {
        return new Result(point, cases.size(), details);
    }

}
