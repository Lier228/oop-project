package oopproject.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        List<ResearchPaper> papers = new ArrayList<>();
        for (Researcher researcher : researchers) {
            if (researcher != null && researcher.isResearcher()) {
                papers.addAll(researcher.getResearchPapers());
            }
        }
        papers.sort(comparator != null ? comparator : ResearchPaperComparators.BY_CITATIONS_DESC);
        return papers;
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
