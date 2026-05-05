package research;

import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Researcher {


    /**
     * @return
     */
    int getHIndex();

    /**
     * @return
     */
    void updateHIndex();

    /**
     * @param paper 
     * @return
     */
    boolean addPaper(void paper);

    /**
     * @return
     */
    Set<void> getPapers();

    /**
     * @param c 
     * @return
     */
    void printPapers(void c);

}