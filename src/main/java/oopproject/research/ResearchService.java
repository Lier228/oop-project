package oopproject.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import oopproject.enums.ResearchSortType;
import oopproject.exceptions.LowHIndexException;
import oopproject.users.Student;

public final class ResearchService {
    private ResearchService() {
    }

    public static void assignSupervisor(Student student, Researcher supervisor) throws LowHIndexException {
        student.assignResearchSupervisor(supervisor);
    }

    public static List<ResearchPaper> getAllPapersSorted(Collection<? extends Researcher> researchers,
                                                         Comparator<ResearchPaper> comparator) {
        Set<ResearchPaper> uniquePapers = new LinkedHashSet<>();
        for (Researcher researcher : researchers) {
            if (researcher != null && researcher.isResearcher()) {
                uniquePapers.addAll(researcher.getResearchPapers());
            }
        }
        List<ResearchPaper> papers = new ArrayList<>(uniquePapers);
        papers.sort(comparator != null ? comparator : ResearchPaperComparators.BY_CITATIONS_DESC);
        return papers;
    }

    public static List<ResearchPaper> getAllPapersSorted(Collection<? extends Researcher> researchers,
                                                         ResearchSortType sortType) {
        return getAllPapersSorted(researchers, ResearchPaperComparators.byType(sortType));
    }

    public static List<ResearchPaper> getPapersByYear(Collection<? extends Researcher> researchers, int year) {
        return getAllPapersSorted(researchers, ResearchSortType.DATE).stream()
                .filter(paper -> paper.getPublishDate() != null && paper.getPublishDate().getYear() == year)
                .toList();
    }

    public static List<ResearchPaper> getPapersByJournal(Collection<? extends Researcher> researchers, String journal) {
        if (journal == null || journal.isBlank()) {
            return List.of();
        }
        return getAllPapersSorted(researchers, ResearchSortType.CITATIONS).stream()
                .filter(paper -> paper.getJournal() != null && paper.getJournal().equalsIgnoreCase(journal))
                .toList();
    }

    public static List<ResearchPaper> getTopCitedPapers(Collection<? extends Researcher> researchers, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return getAllPapersSorted(researchers, ResearchSortType.CITATIONS).stream()
                .limit(limit)
                .toList();
    }

    public static List<Researcher> getResearchersBySchool(Collection<? extends Researcher> researchers, String school) {
        if (school == null || school.isBlank()) {
            return List.of();
        }
        return researchers.stream()
                .filter(researcher -> researcher != null && researcher.isResearcher())
                .filter(researcher -> researcher.getResearchSchool().equalsIgnoreCase(school))
                .map(Researcher.class::cast)
                .toList();
    }

    public static void printAllPapers(Collection<? extends Researcher> researchers,
                                      Comparator<ResearchPaper> comparator) {
        for (ResearchPaper paper : getAllPapersSorted(researchers, comparator)) {
            System.out.println(paper.toDisplayString());
        }
    }

    public static Optional<Researcher> findTopCitedResearcher(Collection<? extends Researcher> researchers) {
        return researchers.stream()
                .filter(researcher -> researcher != null && researcher.isResearcher())
                .map(Researcher.class::cast)
                .max(Comparator.comparingInt(Researcher::getTotalCitations));
    }

    public static Optional<Researcher> findTopCitedResearcherOfSchool(Collection<? extends Researcher> researchers,
                                                                      String school) {
        if (school == null || school.isBlank()) {
            return Optional.empty();
        }
        return researchers.stream()
                .filter(researcher -> researcher != null && researcher.isResearcher())
                .filter(researcher -> researcher.getResearchSchool().equalsIgnoreCase(school))
                .map(Researcher.class::cast)
                .max(Comparator.comparingInt(Researcher::getTotalCitations));
    }

    public static Optional<Researcher> findTopCitedResearcherOfYear(Collection<? extends Researcher> researchers,
                                                                    int year) {
        return researchers.stream()
                .filter(researcher -> researcher != null && researcher.isResearcher())
                .map(Researcher.class::cast)
                .max(Comparator.comparingInt(researcher -> researcher.getCitationsInYear(year)));
    }
}
