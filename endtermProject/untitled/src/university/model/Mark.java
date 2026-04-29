package university.model;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double firstAttestation;
    private final double secondAttestation;
    private final double finalExam;

    public Mark(double firstAttestation, double secondAttestation, double finalExam) {
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public double calculateTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public boolean isPassed() {
        return calculateTotal() >= 50.0;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "firstAttestation=" + firstAttestation +
                ", secondAttestation=" + secondAttestation +
                ", finalExam=" + finalExam +
                ", total=" + calculateTotal() +
                '}';
    }
}
