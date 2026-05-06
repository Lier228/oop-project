package oopproject.academic;

import java.time.LocalDate;
import oopproject.enums.ReportType;

public class Report {
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
