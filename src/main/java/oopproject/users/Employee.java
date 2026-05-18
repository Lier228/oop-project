package oopproject.users;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import oopproject.core.University;
import oopproject.enums.UserType;
import oopproject.exceptions.NonResearcherException;
import oopproject.research.ResearchPaper;
import oopproject.research.ResearchProject;
import oopproject.research.Researcher;
import oopproject.research.ResearcherProfile;

public class Employee extends User implements Researcher {
    protected double salary;
    protected LocalDate hireDate;
    protected String department;
    protected ResearcherProfile researcherProfile;

    public Employee() {
        this.role = UserType.EMPLOYEE;
    }

    public Employee(int id, String username, String password, String email, double salary, LocalDate hireDate) {
        this(id, username, password, email, salary, hireDate, null);
    }

    public Employee(int id, String username, String password, String email,
                    double salary, LocalDate hireDate, String department) {
        super(id, username, password, email, UserType.EMPLOYEE);
        this.salary = salary;
        this.hireDate = hireDate;
        this.department = department;
    }

    public void changeActive() {
        if (isActive()) {
            block();
        } else {
            unblock();
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean becomeResearcher(String school) {
        if (!isActive()) {
            return false;
        }
        String normalizedSchool = normalizeResearchSchool(school);
        if (researcherProfile == null) {
            researcherProfile = new ResearcherProfile(this, normalizedSchool);
        } else {
            researcherProfile.setSchool(normalizedSchool);
        }
        trackResearcherIfRegistered();
        return true;
    }

    public boolean joinResearchProject(ResearchProject project) throws NonResearcherException {
        if (project == null) {
            return false;
        }
        boolean joined = project.addParticipant(this);
        if (joined) {
            trackResearcherIfRegistered();
        }
        return joined;
    }

    public boolean leaveResearchProject(ResearchProject project) {
        return project != null && project.deleteParticipant(this);
    }

    @Override
    public boolean addResearchPaper(ResearchPaper paper) {
        if (paper == null || !isActive()) {
            return false;
        }
        boolean added = ensureResearcherProfile().addResearchPaper(paper);
        if (added) {
            trackResearcherIfRegistered();
        }
        return added;
    }

    @Override
    public boolean removeResearchPaper(ResearchPaper paper) {
        return researcherProfile != null && researcherProfile.removeResearchPaper(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return researcherProfile == null ? List.of() : researcherProfile.getResearchPapers();
    }

    @Override
    public Set<ResearchProject> getResearchProjects() {
        return researcherProfile == null ? Set.of() : researcherProfile.getResearchProjects();
    }

    @Override
    public void attachResearchProject(ResearchProject project) {
        if (project != null) {
            ensureResearcherProfile().attachResearchProject(project);
        }
    }

    @Override
    public void detachResearchProject(ResearchProject project) {
        if (researcherProfile != null) {
            researcherProfile.detachResearchProject(project);
        }
    }

    @Override
    public String getResearcherName() {
        return getUsername();
    }

    @Override
    public boolean isResearcher() {
        return researcherProfile != null && isActive();
    }

    @Override
    public String getResearchSchool() {
        return researcherProfile == null ? normalizeResearchSchool(null) : researcherProfile.getResearchSchool();
    }

    protected ResearcherProfile ensureResearcherProfile() {
        if (researcherProfile == null) {
            researcherProfile = new ResearcherProfile(this, normalizeResearchSchool(null));
        }
        return researcherProfile;
    }

    protected String normalizeResearchSchool(String school) {
        if (school != null && !school.isBlank()) {
            return school;
        }
        if (department != null && !department.isBlank()) {
            return department;
        }
        return "UNSPECIFIED";
    }

    protected void trackResearcherIfRegistered() {
        University university = University.getInstance();
        if (university.findUserById(getId()).isPresent()) {
            university.addResearcher(this);
        }
    }
}
