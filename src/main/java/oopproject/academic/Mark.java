package oopproject.academic;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private double att1;
    private double att2;
    private double finalExam;

    public Mark(double att1, double att2, double finalExam) {
        this.att1 = att1;
        this.att2 = att2;
        this.finalExam = finalExam;
    }

    public double getTotal() {
        return att1 + att2 + finalExam;
    }

    public double getAtt1() {
        return att1;
    }

    public double getAtt2() {
        return att2;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public boolean isPassing() {
        return getTotal() >= 50.0;
    }

    @Override
    public String toString() {
        return "Mark{att1=" + att1 + ", att2=" + att2 + ", finalExam=" + finalExam + ", total=" + getTotal() + "}";
    }
}
