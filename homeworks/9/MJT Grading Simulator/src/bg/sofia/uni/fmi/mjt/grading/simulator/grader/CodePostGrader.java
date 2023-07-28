package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.*;

public class CodePostGrader implements AdminGradingAPI {

    Queue<Assignment> ungraded;
    Set<Assignment> graded;
    List<Assistant> assistants;
    public boolean finalize = true;
    private int numberOfAssistants;
    private Assistant[] assistantsArray;

    public CodePostGrader(int numberOfAssistants) {
        ungraded = new LinkedList<>();
        graded = new HashSet<>();
        assistants = new ArrayList<>();
        this.numberOfAssistants = numberOfAssistants;
        assistantsArray = new Assistant[numberOfAssistants];
        for (int i = 0; i < numberOfAssistants; i++) {
            Random randomName = new Random();
            String currName = randomName.toString();
            assistantsArray[i] = new Assistant(currName, this);
            assistants.add(assistantsArray[i]);
            assistantsArray[i].start();
        }
    }

    @Override
    public synchronized Assignment getAssignment() {
        while (!ungraded.isEmpty() || finalize) {
            if (!ungraded.isEmpty()) {
                Assignment temp = ungraded.peek();
                ungraded.remove();
                graded.add(temp);
                return temp;
            }
            else {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public synchronized int getSubmittedAssignmentsCount() {
        return graded.size();
    }

    @Override
    public synchronized void finalizeGrading()  {
        finalize = false;
        this.notifyAll();
//        for (int i = 0; i < numberOfAssistants; i++) {
//            try {
//                assistantsArray[i].join();
//            } catch (InterruptedException e) {
//                e.getStackTrace();
//            }
//        }
    }

    @Override
    public synchronized List<Assistant> getAssistants() {
        return Collections.unmodifiableList(assistants);
    }

    @Override
    public synchronized void submitAssignment(Assignment assignment) {
        ungraded.add(assignment);
        this.notifyAll();
    }
}