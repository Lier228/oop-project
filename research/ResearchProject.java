package research;

import Serializable;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ResearchProject implements Serializable {

    /**
     * Default constructor
     */
    public ResearchProject() {
    }

    /**
     * 
     */
    private final String topic;

    /**
     * 
     */
    private final Set<void> papers;

    /**
     * 
     */
    private Set<Researcher> participants;

    /**
     * @param topic
     */
    public ResearchProject(String topic) {
        // TODO implement here
    }

    /**
     * @param researcher 
     * @return
     */
    public boolean addParticipant(void researcher) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @param researcher 
     * @return
     */
    public boolean deleteParticipant(void researcher) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @param researcher 
     * @return
     */
    public boolean findParticipant(void researcher) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @param researchPaper 
     * @return
     */
    public boolean addPaper(void researchPaper) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @param researchPaper 
     * @return
     */
    public boolean deletePaper(void researchPaper) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @param researchPaper 
     * @return
     */
    public boolean findPaper(void researchPaper) {
        // TODO implement here
        return false;
    }

    /**
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * Delete after debugging
     * @return
     */
    public String getTopic() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public Set<void> getParticipants() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Set<void> getPapers() {
        // TODO implement here
        return null;
    }

}