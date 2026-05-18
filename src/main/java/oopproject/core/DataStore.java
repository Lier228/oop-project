package oopproject.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import oopproject.academic.Course;
import oopproject.academic.Enrollment;
import oopproject.research.ResearchPaper;
import oopproject.research.ResearchProject;
import oopproject.research.Researcher;
import oopproject.users.User;

public final class DataStore {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path STATE_FILE = DATA_DIR.resolve("university.ser");
    private static final Path JSON_SNAPSHOT_FILE = DATA_DIR.resolve("university-summary.json");

    private DataStore() {
    }

    public static boolean saveState() {
        return saveState(University.getInstance());
    }

    public static boolean saveState(University university) {
        try {
            Files.createDirectories(DATA_DIR);
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(STATE_FILE))) {
                output.writeObject(university);
            }
            exportJsonSnapshot(university);
            university.addLog(null, "STATE_SAVED " + STATE_FILE);
            return true;
        } catch (IOException exception) {
            university.addLog(null, "STATE_SAVE_FAILED " + exception.getMessage());
            return false;
        }
    }

    public static boolean loadState() {
        return loadState(University.getInstance());
    }

    public static boolean loadState(University targetUniversity) {
        if (!Files.exists(STATE_FILE)) {
            return false;
        }

        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(STATE_FILE))) {
            Object loaded = input.readObject();
            if (!(loaded instanceof University savedUniversity)) {
                return false;
            }
            targetUniversity.copyFrom(savedUniversity);
            targetUniversity.addLog(null, "STATE_LOADED " + STATE_FILE);
            return true;
        } catch (IOException | ClassNotFoundException exception) {
            targetUniversity.addLog(null, "STATE_LOAD_FAILED " + exception.getMessage());
            return false;
        }
    }

    public static boolean exportJsonSnapshot() {
        return exportJsonSnapshot(University.getInstance());
    }

    public static boolean exportJsonSnapshot(University university) {
        try {
            Files.createDirectories(DATA_DIR);
            Files.writeString(JSON_SNAPSHOT_FILE, buildJsonSnapshot(university), StandardCharsets.UTF_8);
            return true;
        } catch (IOException exception) {
            university.addLog(null, "JSON_EXPORT_FAILED " + exception.getMessage());
            return false;
        }
    }

    public static String getStateFile() {
        return STATE_FILE.toString();
    }

    public static String getJsonSnapshotFile() {
        return JSON_SNAPSHOT_FILE.toString();
    }

    private static String buildJsonSnapshot(University university) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"name\": \"").append(escape(university.getName())).append("\",\n");
        appendUsers(json, university);
        json.append(",\n");
        appendCourses(json, university);
        json.append(",\n");
        appendResearchers(json, university);
        json.append(",\n");
        appendProjects(json, university);
        json.append(",\n");
        appendLogs(json, university);
        json.append("\n}\n");
        return json.toString();
    }

    private static void appendUsers(StringBuilder json, University university) {
        json.append("  \"users\": [\n");
        for (int i = 0; i < university.getUsers().size(); i++) {
            User user = university.getUsers().get(i);
            appendComma(json, i);
            json.append("    {")
                    .append("\"id\": ").append(user.getId()).append(", ")
                    .append("\"type\": \"").append(user.getClass().getSimpleName()).append("\", ")
                    .append("\"role\": \"").append(user.getRole()).append("\", ")
                    .append("\"active\": ").append(user.isActive()).append(", ")
                    .append("\"username\": \"").append(escape(user.getUsername())).append("\", ")
                    .append("\"email\": \"").append(escape(user.getEmail())).append("\"")
                    .append("}");
        }
        json.append("\n  ]");
    }

    private static void appendCourses(StringBuilder json, University university) {
        json.append("  \"courses\": [\n");
        for (int i = 0; i < university.getCourses().size(); i++) {
            Course course = university.getCourses().get(i);
            appendComma(json, i);
            json.append("    {")
                    .append("\"code\": \"").append(escape(course.getCode())).append("\", ")
                    .append("\"name\": \"").append(escape(course.getName())).append("\", ")
                    .append("\"credits\": ").append(course.getCredits()).append(", ")
                    .append("\"targetMajor\": \"").append(escape(course.getTargetMajor())).append("\", ")
                    .append("\"targetYear\": ").append(course.getTargetYear() == null ? "null" : course.getTargetYear()).append(", ")
                    .append("\"studentIds\": ").append(userIds(course.getEnrollments().stream()
                            .map(Enrollment::getStudent)
                            .toList())).append(", ")
                    .append("\"instructorIds\": ").append(userIds(course.getInstructors()))
                    .append("}");
        }
        json.append("\n  ]");
    }

    private static void appendResearchers(StringBuilder json, University university) {
        json.append("  \"researchers\": [\n");
        for (int i = 0; i < university.getResearchers().size(); i++) {
            Researcher researcher = university.getResearchers().get(i);
            appendComma(json, i);
            json.append("    {")
                    .append("\"name\": \"").append(escape(researcher.getResearcherName())).append("\", ")
                    .append("\"school\": \"").append(escape(researcher.getResearchSchool())).append("\", ")
                    .append("\"hIndex\": ").append(researcher.calculateHIndex()).append(", ")
                    .append("\"totalCitations\": ").append(researcher.getTotalCitations())
                    .append("}");
        }
        json.append("\n  ]");
    }

    private static void appendProjects(StringBuilder json, University university) {
        json.append("  \"researchProjects\": [\n");
        for (int i = 0; i < university.getProjects().size(); i++) {
            ResearchProject project = university.getProjects().get(i);
            appendComma(json, i);
            json.append("    {")
                    .append("\"topic\": \"").append(escape(project.getTopic())).append("\", ")
                    .append("\"participants\": [");

            int participantIndex = 0;
            for (Researcher researcher : project.getParticipants()) {
                if (participantIndex++ > 0) {
                    json.append(", ");
                }
                json.append("\"").append(escape(researcher.getResearcherName())).append("\"");
            }

            json.append("], \"papers\": [");
            int paperIndex = 0;
            for (ResearchPaper paper : project.getPapers()) {
                if (paperIndex++ > 0) {
                    json.append(", ");
                }
                json.append("\"").append(escape(paper.getTitle())).append("\"");
            }
            json.append("]}");
        }
        json.append("\n  ]");
    }

    private static void appendLogs(StringBuilder json, University university) {
        json.append("  \"logs\": [\n");
        for (int i = 0; i < university.getLogs().size(); i++) {
            Log log = university.getLogs().get(i);
            appendComma(json, i);
            json.append("    {")
                    .append("\"date\": \"").append(log.getDate()).append("\", ")
                    .append("\"actor\": \"").append(escape(log.getUser() == null ? "SYSTEM" : log.getUser().getUsername())).append("\", ")
                    .append("\"action\": \"").append(escape(log.getAction())).append("\"")
                    .append("}");
        }
        json.append("\n  ]");
    }

    private static String userIds(Iterable<? extends User> users) {
        StringBuilder ids = new StringBuilder("[");
        int index = 0;
        for (User user : users) {
            if (index++ > 0) {
                ids.append(", ");
            }
            ids.append(user.getId());
        }
        ids.append("]");
        return ids.toString();
    }

    private static void appendComma(StringBuilder json, int index) {
        if (index > 0) {
            json.append(",\n");
        }
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
