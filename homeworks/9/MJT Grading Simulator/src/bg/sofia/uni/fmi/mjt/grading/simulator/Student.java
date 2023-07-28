package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.Random;

public class Student implements Runnable {

    private int fn;
    private String name;
    private StudentGradingAPI studentGradingAPI;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }

    @Override
    public void run() {
        final int kindOfTest = 4;
        final int maxTime = 1000;
        Random randomTaskComplexity = new Random();
        int randomNumber = randomTaskComplexity.nextInt(kindOfTest);
        AssignmentType assignmentType = AssignmentType.values()[randomNumber];
        Assignment newOne = new Assignment(fn, name, assignmentType);
        Random randomTime = new Random();
        int randomTimeCreating = randomTime.nextInt(maxTime);
        try {
            Thread.sleep(randomTimeCreating);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        studentGradingAPI.submitAssignment(newOne);
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}