package oopproject.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Marks implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Double> att1Marks;
    private final List<Double> att2Marks;
    private double finalExamMark;
    private boolean finalExamAssigned;

    public Marks() {
        this.att1Marks = new ArrayList<>();
        this.att2Marks = new ArrayList<>();
        this.finalExamMark = 0.0;
        this.finalExamAssigned = false;
    }

    public Marks(double att1, double att2, double finalExam) {
        this();
        addAtt1Mark(att1);
        addAtt2Mark(att2);
        addFinalExamMark(finalExam);
    }

    public Marks(List<Double> att1Marks, List<Double> att2Marks, double finalExamMark) {
        this.att1Marks = new ArrayList<>(att1Marks == null ? List.of() : att1Marks);
        this.att2Marks = new ArrayList<>(att2Marks == null ? List.of() : att2Marks);
        this.finalExamMark = finalExamMark;
        this.finalExamAssigned = true;
    }

    public void addAtt1Mark(double value) {
        att1Marks.add(value);
    }

    public void addAtt2Mark(double value) {
        att2Marks.add(value);
    }

    public void addFinalExamMark(double value) {
        finalExamMark = value;
        finalExamAssigned = true;
    }

    public void mergeFrom(Marks other) {
        if (other == null) {
            return;
        }
        att1Marks.addAll(other.att1Marks);
        att2Marks.addAll(other.att2Marks);
        if (other.finalExamAssigned) {
            finalExamMark = other.finalExamMark;
            finalExamAssigned = true;
        }
    }

    public double getTotal() {
        return getAtt1() + getAtt2() + getFinalExam();
    }

    public double getAtt1() {
        return sum(att1Marks);
    }

    public double getAtt2() {
        return sum(att2Marks);
    }

    public double getFinalExam() {
        return finalExamMark;
    }

    public boolean isPassing() {
        return getTotal() >= 50.0;
    }

    public List<Double> getAtt1Marks() {
        return Collections.unmodifiableList(att1Marks);
    }

    public List<Double> getAtt2Marks() {
        return Collections.unmodifiableList(att2Marks);
    }

    private double sum(List<Double> marks) {
        return marks.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    public String toString() {
        return "Marks{att1=" + att1Marks
                + ", att1Total=" + getAtt1()
                + ", att2=" + att2Marks
                + ", att2Total=" + getAtt2()
                + ", final=" + (finalExamAssigned ? finalExamMark : "not set")
                + ", total=" + getTotal()
                + "}";
    }
}
