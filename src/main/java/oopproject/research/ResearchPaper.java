package oopproject.research;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String journal;
    private String publisher;
    private int pages;
    private int citations;
    private LocalDate publishDate;
    private String doi;
    private String keywords;
    private final List<Researcher> authors = new ArrayList<>();

    public ResearchPaper(String title, String journal, int pages, int citations, LocalDate publishDate, String doi) {
        this(title, journal, null, pages, citations, publishDate, doi, null);
    }

    public ResearchPaper(String title, String journal, String publisher, int pages, int citations,
                         LocalDate publishDate, String doi, String keywords) {
        this.title = title;
        this.journal = journal;
        this.publisher = publisher;
        this.pages = pages;
        this.citations = citations;
        this.publishDate = publishDate;
        this.doi = doi;
        this.keywords = keywords;
    }

    public void addAuthor(Researcher author) {
        if (author != null && !authors.contains(author)) {
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

    public String getPublisher() {
        return publisher;
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

    public String getKeywords() {
        return keywords;
    }

    public List<Researcher> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public String getAuthorNames() {
        return authors.stream()
                .map(Researcher::getResearcherName)
                .collect(Collectors.joining(", "));
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
        if (doi != null && that.doi != null) {
            return Objects.equals(doi, that.doi);
        }
        return Objects.equals(title, that.title)
                && Objects.equals(publishDate, that.publishDate);
    }

    @Override
    public int hashCode() {
        return doi != null ? Objects.hash(doi) : Objects.hash(title, publishDate);
    }

    public String toDisplayString() {
        String authorText = authors.isEmpty() ? "unknown authors" : getAuthorNames();
        return title + " by " + authorText
                + ", " + journal
                + " (" + publishDate + "), citations=" + citations
                + ", pages=" + pages
                + ", doi=" + doi;
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}
