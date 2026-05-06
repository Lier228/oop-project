package oopproject.academic;

public class Mark {
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

    @Override
    public String toString() {
        return "Mark{att1=" + att1 + ", att2=" + att2 + ", finalExam=" + finalExam + ", total=" + getTotal() + "}";
    }
}
