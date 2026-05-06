package oopproject.research;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Comparable<ResearchPaper> {
    private String title;
    private String journal;
    private int pages;
    private int citations;
    private LocalDate publishDate;
    private String doi;
    private final List<Researcher> authors = new ArrayList<>();

    public ResearchPaper(String title, String journal, int pages, int citations, LocalDate publishDate, String doi) {
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.citations = citations;
        this.publishDate = publishDate;
        this.doi = doi;
    }

    public void addAuthor(Researcher author) {
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    public void addCitation() {
        citations++;
    }

    public String getTitle() {
        return title;
    }

    public String getJournal() {
        return journal;
    }

    public int getPages() {
        return pages;
    }

    public int getCitations() {
        return citations;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getDoi() {
        return doi;
    }

    public List<Researcher> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, citations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResearchPaper that)) {
            return false;
        }
        return Objects.equals(doi, that.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return title + ", " + journal + " (" + publishDate + ")";
    }
}
