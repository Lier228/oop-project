package university.model;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    ResearchProfile getResearchProfile();

    String getResearcherName();

    default boolean isResearcher() {
        return getResearchProfile().isActive();
    }

    default void activateResearchProfile(int hIndex) {
        getResearchProfile().activate(hIndex);
    }

    default int getHIndex() {
        return getResearchProfile().getHIndex();
    }

    default List<ResearchPaper> getResearchPapers() {
        return getResearchProfile().getResearchPapers();
    }

    default List<ResearchProject> getResearchProjects() {
        return getResearchProfile().getResearchProjects();
    }

    default void addResearchPaper(ResearchPaper paper) {
        getResearchProfile().addPaper(paper);
    }

    default void joinResearchProject(ResearchProject project) {
        getResearchProfile().joinProject(project);
    }

    default int getTotalCitations() {
        return getResearchProfile().getTotalCitations();
    }

    default void printPapers(Comparator<ResearchPaper> comparator) {
        getResearchPapers()
                .stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }
}
