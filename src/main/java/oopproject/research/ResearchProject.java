package oopproject.research;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ResearchProject {
    private final String topic;
    private final Set<Researcher> participants = new HashSet<>();
    private final Set<ResearchPaper> papers = new HashSet<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public boolean addParticipant(Researcher researcher) {
        return participants.add(researcher);
    }

    public boolean deleteParticipant(Researcher researcher) {
        return participants.remove(researcher);
    }

    public boolean findParticipant(Researcher researcher) {
        return participants.contains(researcher);
    }

    public boolean addPaper(ResearchPaper paper) {
        return papers.add(paper);
    }

    public boolean deletePaper(ResearchPaper paper) {
        return papers.remove(paper);
    }

    public boolean findPaper(ResearchPaper paper) {
        return papers.contains(paper);
    }

    public String getTopic() {
        return topic;
    }

    public Set<Researcher> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public Set<ResearchPaper> getPapers() {
        return Collections.unmodifiableSet(papers);
    }
}
