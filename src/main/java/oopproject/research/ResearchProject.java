package oopproject.research;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import oopproject.exceptions.NonResearcherException;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String topic;
    private final Set<Researcher> participants = new HashSet<>();
    private final Set<ResearchPaper> papers = new HashSet<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public boolean addParticipant(Object candidate) throws NonResearcherException {
        if (!(candidate instanceof Researcher researcher)) {
            throw new NonResearcherException("Only researchers can join a research project.");
        }
        return addParticipant(researcher);
    }

    public boolean addParticipant(Researcher researcher) throws NonResearcherException {
        if (researcher == null || !researcher.isResearcher()) {
            throw new NonResearcherException("Only active researchers can join a research project.");
        }
        boolean added = participants.add(researcher);
        if (added) {
            for (ResearchPaper paper : papers) {
                paper.addAuthor(researcher);
            }
        }
        return added;
    }

    public boolean deleteParticipant(Researcher researcher) {
        return participants.remove(researcher);
    }

    public boolean findParticipant(Researcher researcher) {
        return participants.contains(researcher);
    }

    public boolean addPaper(ResearchPaper paper) {
        if (paper == null) {
            return false;
        }
        for (Researcher participant : participants) {
            paper.addAuthor(participant);
        }
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

    @Override
    public String toString() {
        return topic + " [participants=" + participants.size() + ", papers=" + papers.size() + "]";
    }
}
