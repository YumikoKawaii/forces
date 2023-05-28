package com.yumiko.forces.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cases")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String input;
    private String output;
    private int problem_id;

    public Case() {
    }

    public Case(String input, String output, int problem_id) {
        this.input = input;
        this.output = output;
        this.problem_id = problem_id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }
}
