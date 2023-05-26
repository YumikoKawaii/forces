package com.yumiko.forces.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "challenges")
public class Challenge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String topic;

    private String problem;

    public Challenge() {
    }

    public Challenge(String topic, String problem) {
        super();
        this.topic = topic;
        this.problem = problem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "topic: " + this.topic + ", problem: " + this.problem;
    }
}
