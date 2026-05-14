package oopproject.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface Researcher extends Serializable {
    boolean addResearchPaper(ResearchPaper paper);

    boolean removeResearchPaper(ResearchPaper paper);

    List<ResearchPaper> getResearchPapers();

    String getResearcherName();

    default boolean isResearcher() {
        return true;
    }

    default String getResearchSchool() {
        return "UNSPECIFIED";
    }

    default int getTotalCitations() {
        return getResearchPapers().stream()
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    default int getCitationsInYear(int year) {
        return getResearchPapers().stream()
                .filter(paper -> paper.getPublishDate() != null && paper.getPublishDate().getYear() == year)
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    default int calculateHIndex() {
        List<Integer> citations = getResearchPapers().stream()
                .map(ResearchPaper::getCitations)
                .sorted((a, b) -> Integer.compare(b, a))
                .toList();

        int hIndex = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                hIndex = i + 1;
            }
        }
        return hIndex;
    }

    default List<ResearchPaper> getPapersSorted(Comparator<ResearchPaper> comparator) {
        Comparator<ResearchPaper> order = comparator != null
                ? comparator
                : ResearchPaperComparators.BY_CITATIONS_DESC;

        List<ResearchPaper> sorted = new ArrayList<>(getResearchPapers());
        sorted.sort(order);
        return sorted;
    }

    default void printPapers(Comparator<ResearchPaper> comparator) {
        for (ResearchPaper paper : getPapersSorted(comparator)) {
            System.out.println(paper.toDisplayString());
        }
    }
}
