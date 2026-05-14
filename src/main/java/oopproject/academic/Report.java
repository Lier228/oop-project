package oopproject.academic;

import java.io.Serializable;
import java.time.LocalDate;
import oopproject.enums.ReportType;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReportType type;
    private LocalDate createdDate;
    private String data;

    public Report() {
        this.createdDate = LocalDate.now();
    }

    public Report(ReportType type, String data) {
        this(type, LocalDate.now(), data);
    }

    public Report(ReportType type, LocalDate createdDate, String data) {
        this.type = type;
        this.createdDate = createdDate;
        this.data = data;
    }

    public void generate() {
        createdDate = LocalDate.now();
    }

    public ReportType getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
