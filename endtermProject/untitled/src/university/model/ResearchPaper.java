package university.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String journal;
    private final int pages;
    private final LocalDate publicationDate;
    private final String doi;
    private final int citations;
    private final List<String> authors;

    public ResearchPaper(
            String title,
            String journal,
            int pages,
            LocalDate publicationDate,
            String doi,
            int citations,
            List<String> authors
    ) {
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.publicationDate = publicationDate;
        this.doi = doi;
        this.citations = citations;
        this.authors = new ArrayList<>(authors);
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

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getDoi() {
        return doi;
    }

    public int getCitations() {
        return citations;
    }

    public List<String> getAuthors() {
        return List.copyOf(authors);
    }

    public int getLength() {
        return pages;
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return other.publicationDate.compareTo(publicationDate);
    }

    public static Comparator<ResearchPaper> byDatePublished() {
        return Comparator.comparing(ResearchPaper::getPublicationDate).reversed();
    }

    public static Comparator<ResearchPaper> byCitations() {
        return Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    }

    public static Comparator<ResearchPaper> byLength() {
        return Comparator.comparingInt(ResearchPaper::getLength).reversed();
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
        return "ResearchPaper{" +
                "title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", pages=" + pages +
                ", publicationDate=" + publicationDate +
                ", doi='" + doi + '\'' +
                ", citations=" + citations +
                '}';
    }
}
