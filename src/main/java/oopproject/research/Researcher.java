package oopproject.research;

import java.util.List;

public interface Researcher {
    boolean addResearchPaper(ResearchPaper paper);

    List<ResearchPaper> getResearchPapers();

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
}
