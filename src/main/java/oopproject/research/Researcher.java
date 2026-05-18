package oopproject.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import oopproject.enums.ResearchSortType;

public interface Researcher extends Serializable {
    boolean addResearchPaper(ResearchPaper paper);

    boolean removeResearchPaper(ResearchPaper paper);

    List<ResearchPaper> getResearchPapers();

    String getResearcherName();

    default boolean isResearcher() {
        return true;
    }

    default Set<ResearchProject> getResearchProjects() {
        return Collections.emptySet();
    }

    default void attachResearchProject(ResearchProject project) {
    }

    default void detachResearchProject(ResearchProject project) {
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

    default List<ResearchPaper> getPapersSorted(ResearchSortType sortType) {
        return getPapersSorted(ResearchPaperComparators.byType(sortType));
    }

    default List<ResearchPaper> getPapersByYear(int year) {
        return getResearchPapers().stream()
                .filter(paper -> paper.getPublishDate() != null && paper.getPublishDate().getYear() == year)
                .toList();
    }

    default List<ResearchPaper> getPapersByJournal(String journal) {
        if (journal == null || journal.isBlank()) {
            return List.of();
        }
        return getResearchPapers().stream()
                .filter(paper -> paper.getJournal() != null && paper.getJournal().equalsIgnoreCase(journal))
                .toList();
    }

    default List<ResearchPaper> getTopCitedPapers(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return getResearchPapers().stream()
                .sorted(ResearchPaperComparators.BY_CITATIONS_DESC)
                .limit(limit)
                .toList();
    }

    default void printPapers(Comparator<ResearchPaper> comparator) {
        for (ResearchPaper paper : getPapersSorted(comparator)) {
            System.out.println(paper.toDisplayString());
        }
    }

    default void printPapers(ResearchSortType sortType) {
        for (ResearchPaper paper : getPapersSorted(sortType)) {
            System.out.println(paper.toDisplayString());
        }
    }
}
