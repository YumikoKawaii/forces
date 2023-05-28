package com.yumiko.forces.models;

import jakarta.persistence.*;

@Entity
@Table(name = "contributions")
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String contributor;
    private String topic;
    private String problem;

    public Contribution() {
    }

    public Contribution(String contributor, String topic, String problem) {
        super();
        this.contributor = contributor;
        this.topic = topic;
        this.problem = problem;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
