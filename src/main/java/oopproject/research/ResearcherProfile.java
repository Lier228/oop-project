package oopproject.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import oopproject.exceptions.NonResearcherException;

public class ResearcherProfile implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    private final Researcher owner;
    private final int ownerId;
    private final String ownerName;
    private String school;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final Set<ResearchProject> researchProjects = new HashSet<>();

    public ResearcherProfile(Researcher owner, String school) {
        this(owner instanceof oopproject.users.User user ? user.getId() : 0,
                owner instanceof oopproject.users.User user ? user.getUsername() : owner.getResearcherName(),
                school,
                owner);
    }

    public ResearcherProfile(int ownerId, String ownerName, String school) {
        this(ownerId, ownerName, school, null);
    }

    private ResearcherProfile(int ownerId, String ownerName, String school, Researcher owner) {
        this.owner = owner;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.school = school;
    }

    @Override
    public boolean addResearchPaper(ResearchPaper paper) {
        if (paper == null || researchPapers.contains(paper)) {
            return false;
        }
        researchPapers.add(paper);
        paper.addAuthor(authorIdentity());
        return true;
    }

    @Override
    public boolean removeResearchPaper(ResearchPaper paper) {
        return researchPapers.remove(paper);
    }

    public boolean joinProject(ResearchProject project) throws NonResearcherException {
        if (project == null) {
            return false;
        }
        return project.addParticipant(authorIdentity());
    }

    public boolean leaveProject(ResearchProject project) {
        if (project == null) {
            return false;
        }
        return project.deleteParticipant(authorIdentity());
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return Collections.unmodifiableList(researchPapers);
    }

    public Set<ResearchProject> getResearchProjects() {
        return Collections.unmodifiableSet(researchProjects);
    }

    @Override
    public void attachResearchProject(ResearchProject project) {
        if (project != null) {
            researchProjects.add(project);
        }
    }

    @Override
    public void detachResearchProject(ResearchProject project) {
        if (project != null) {
            researchProjects.remove(project);
        }
    }

    @Override
    public String getResearcherName() {
        if (owner instanceof oopproject.users.User user) {
            return user.getUsername();
        }
        return ownerName;
    }

    @Override
    public String getResearchSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    private Researcher authorIdentity() {
        return owner != null ? owner : this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ResearcherProfile that)) {
            return false;
        }
        return ownerId == that.ownerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId);
    }

    @Override
    public String toString() {
        return ownerName + " (" + school + "), h-index=" + calculateHIndex();
    }
}
