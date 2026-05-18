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

    private ResearchPaper(Builder builder) {
        this.title = builder.title;
        this.journal = builder.journal;
        this.publisher = builder.publisher;
        this.pages = builder.pages;
        this.citations = builder.citations;
        this.publishDate = builder.publishDate;
        this.doi = builder.doi;
        this.keywords = builder.keywords;
        this.authors.addAll(builder.authors);
    }

    public static Builder builder() {
        return new Builder();
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
        StringBuilder text = new StringBuilder();
        text.append(title)
                .append(" by ").append(authorText)
                .append(", journal=").append(journal)
                .append(", publisher=").append(publisher == null ? "n/a" : publisher)
                .append(", published=").append(publishDate)
                .append(", pages=").append(pages)
                .append(", citations=").append(citations)
                .append(", doi=").append(doi == null ? "n/a" : doi);
        if (keywords != null && !keywords.isBlank()) {
            text.append(", keywords=").append(keywords);
        }
        return text.toString();
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

    public static final class Builder {
        private String title;
        private String journal;
        private String publisher;
        private int pages;
        private int citations;
        private LocalDate publishDate;
        private String doi;
        private String keywords;
        private final List<Researcher> authors = new ArrayList<>();

        public Builder title(String title) {
            this.title = normalize(title);
            return this;
        }

        public Builder journal(String journal) {
            this.journal = normalize(journal);
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = normalize(publisher);
            return this;
        }

        public Builder pages(int pages) {
            this.pages = pages;
            return this;
        }

        public Builder citations(int citations) {
            this.citations = citations;
            return this;
        }

        public Builder publishDate(LocalDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public Builder doi(String doi) {
            this.doi = normalize(doi);
            return this;
        }

        public Builder keywords(String keywords) {
            this.keywords = normalize(keywords);
            return this;
        }

        public Builder addAuthor(Researcher author) {
            if (author != null && !authors.contains(author)) {
                authors.add(author);
            }
            return this;
        }

        public ResearchPaper build() {
            if (title == null || journal == null || publishDate == null) {
                throw new IllegalStateException("Research paper requires title, journal, and publish date.");
            }
            if (pages <= 0) {
                throw new IllegalStateException("Research paper pages must be positive.");
            }
            if (citations < 0) {
                throw new IllegalStateException("Research paper citations cannot be negative.");
            }
            return new ResearchPaper(this);
        }

        private String normalize(String value) {
            if (value == null) {
                return null;
            }
            String trimmed = value.trim();
            return trimmed.isEmpty() ? null : trimmed;
        }
    }
}
