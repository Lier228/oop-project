package oopproject.research;

import java.util.Comparator;

public final class ResearchPaperComparators {
    public static final Comparator<ResearchPaper> BY_DATE_PUBLISHED_DESC =
            Comparator.comparing(ResearchPaper::getPublishDate,
                    Comparator.nullsLast(Comparator.reverseOrder()));

    public static final Comparator<ResearchPaper> BY_CITATIONS_DESC =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public static final Comparator<ResearchPaper> BY_ARTICLE_LENGTH_DESC =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

    private ResearchPaperComparators() {
    }
}
