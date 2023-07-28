package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.Student;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GraderTest {

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

    @BeforeEach
    void beforeAllTests() {
        final int maxAssistants = 12;
        Random number = new Random();
        int numberOfAssistants = number.nextInt(maxAssistants);
        grader = new CodePostGrader(numberOfAssistants);
        addMoreStudents();
    }

    @Test
    void testWithFourStudents() {
        //beforeAllTests();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    @Test
    void testWithEightStudents() {
        //beforeAllTests();
        addMoreStudents();
        assertTimeout(ofMillis(7000), () -> grader.finalizeGrading(),
                "Checking students is too slow");
    }

    @Test
    void testAddAssistantInUnmodifiableReturnedList() {
        //beforeAllTests();
        assertThrows(UnsupportedOperationException.class, () -> grader.getAssistants().add(new
                Assistant("pp", grader)), "You cant add new Assistant");
    }

}