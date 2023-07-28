package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;

public class Assistant extends Thread {

    private String name;

    private AdminGradingAPI gradingAPI;

    private int numberOfGratedAssigment = 0;

    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.gradingAPI = grader;
    }

    @Override
    public void run() {
        Assignment curr = gradingAPI.getAssignment();
        while (curr != null) {
            try {
                Thread.sleep(curr.type().getGradingTime());
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
            numberOfGratedAssigment++;
            curr = gradingAPI.getAssignment();
        }
    }

    public int getNumberOfGradedAssignments() {
        return numberOfGratedAssigment;
    }

}