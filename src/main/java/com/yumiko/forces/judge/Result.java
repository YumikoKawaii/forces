package com.yumiko.forces.judge;

import java.util.List;

public class Result {

    public int point;
    public int maxPoint;

    public List<Detail> detailResult;

    public Result(int point, int maxPoint, List<Detail> detailResult) {
        this.point = point;
        this.maxPoint = maxPoint;
        this.detailResult = detailResult;
    }
}
