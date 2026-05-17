package oopproject.research;

import java.util.Comparator;
import oopproject.enums.ResearchSortType;

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

    public static Comparator<ResearchPaper> byType(ResearchSortType type) {
        if (type == null) {
            return BY_CITATIONS_DESC;
        }
        return switch (type) {
            case DATE -> BY_DATE_PUBLISHED_DESC;
            case CITATIONS -> BY_CITATIONS_DESC;
            case LENGTH -> BY_ARTICLE_LENGTH_DESC;
        };
    }
}
