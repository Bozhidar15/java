package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.Student;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ErrorClassTest {

    CodePostGrader grader;

    private void addMoreStudents() {
        Random fn = new Random();
        int randomNumber = fn.nextInt();
        Random randomName = new Random();
        String currName = randomName.toString();
        Student student1 = new Student(randomNumber, currName, grader);
        randomNumber = fn.nextInt();
        currName = randomName.toString();
        Student student2 = new Student(randomNumber, currName, grader);
        randomNumber = fn.nextInt();
        currName = randomName.toString();
        Student student3 = new Student(randomNumber, currName, grader);
        randomNumber = fn.nextInt();
        currName = randomName.toString();
        Student student4 = new Student(randomNumber, currName, grader);
        student1.run();
        student2.run();
        student3.run();
        student4.run();
    }

    private void addHundredStudents() {
        final int maxAssistants = 15;
        grader = new CodePostGrader(maxAssistants);
        final int maxStudents = 100;
        Student[] hundredStudents = new Student[maxStudents];
        for (int i = 0; i < maxStudents; i++) {
            Random fn = new Random();
            int randomNumber = fn.nextInt();
            Random randomName = new Random();
            String currName = randomName.toString();
            hundredStudents[i] = new Student(randomNumber, currName, grader);
            hundredStudents[i].run();
        }
    }

    //@BeforeEach
    void beforeAllTests() {
        final int maxAssistants = 12;
        Random number = new Random();
        int numberOfAssistants = number.nextInt(maxAssistants);
        grader = new CodePostGrader(numberOfAssistants);
        addMoreStudents();
    }

    @Test
    @Order(2)
    void testWithFourStudents() {
        beforeAllTests();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    @Test
    @Order(3)
    void testWithEightStudents() {
        beforeAllTests();
        addMoreStudents();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    //@Disabled
    @Test
    @Order(4)
    void testWithTwelveStudents() {
        beforeAllTests();
        addMoreStudents();
        addMoreStudents();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    //@Disabled
    @Test
    @Order(7)
    void testWithAHundredStudents() {
        addHundredStudents();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    @Disabled
    @Test
    @Order(1)
    void testWithFourStudentsReturnFourGradedStudents() {
        final int oneAssistant = 1;
        grader = new CodePostGrader(oneAssistant);
        Student st1 = new Student(oneAssistant, "petur", grader);
        st1.run();
        grader.finalizeGrading();
        assertEquals(1, grader.getSubmittedAssignmentsCount(), "submitted count of homeworks" +
                " is invalid");
    }

    @Test
    @Order(5)
    void testAddAssistantInUnmodifiableReturnedList() {
        beforeAllTests();
        assertThrows(UnsupportedOperationException.class, () -> grader.getAssistants().add(new
                Assistant("pp", grader)), "You cant add new Assistant");
    }

    @Disabled
    @Test
    @Order(1)
    void testGetNumberOfSubmittedSolutions() {
        final int oneAssistant = 1;
        grader = new CodePostGrader(oneAssistant);
        Student st1 = new Student(oneAssistant, "petur", grader);
        st1.run();
        grader.finalizeGrading();
        assertEquals(1, grader.getAssistants().get(0).getNumberOfGradedAssignments(),
                "graded homeworks by one assistant is invalid");
    }

}