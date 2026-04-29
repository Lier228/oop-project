package university.model;

import university.exceptions.NotResearcherException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String topic;
    private final List<Researcher> participants = new ArrayList<>();
    private final List<ResearchPaper> publishedPapers = new ArrayList<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public List<Researcher> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<ResearchPaper> getPublishedPapers() {
        return Collections.unmodifiableList(publishedPapers);
    }

    public void addParticipant(Researcher researcher) throws NotResearcherException {
        if (researcher == null || !researcher.isResearcher()) {
            throw new NotResearcherException("Only active researchers can join a research project.");
        }
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            researcher.joinResearchProject(this);
        }
    }

    public void publishPaper(ResearchPaper paper) {
        if (paper != null && !publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
        }
    }

    @Override
    public String toString() {
        return "ResearchProject{" +
                "topic='" + topic + '\'' +
                ", participants=" + participants.size() +
                ", publishedPapers=" + publishedPapers.size() +
                '}';
    }
}
