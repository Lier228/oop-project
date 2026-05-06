package endtermProject.src.university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean active;
    private int hIndex;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();

    public boolean isActive() {
        return active;
    }

    public void activate(int hIndex) {
        this.active = true;
        this.hIndex = Math.max(hIndex, 0);
    }

    public int getHIndex() {
        return hIndex;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = Math.max(hIndex, 0);
        this.active = true;
    }

    public List<ResearchPaper> getResearchPapers() {
        return Collections.unmodifiableList(researchPapers);
    }

    public List<ResearchProject> getResearchProjects() {
        return Collections.unmodifiableList(researchProjects);
    }

    public void addPaper(ResearchPaper paper) {
        if (paper != null && !researchPapers.contains(paper)) {
            active = true;
            researchPapers.add(paper);
        }
    }

    public void joinProject(ResearchProject project) {
        if (project != null && !researchProjects.contains(project)) {
            active = true;
            researchProjects.add(project);
        }
    }

    public int getTotalCitations() {
        return researchPapers.stream().mapToInt(ResearchPaper::getCitations).sum();
    }
}
