import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2025/3/21
 */
public class MainTest {

    @Test
    public void testGenerateQuestionsAndAnswers() {
        Main.generateQuestionsAndAnswers(5, 10);
        File exerciseFile = new File("Exercises.txt");
        File answerFile = new File("Answers.txt");

        assertTrue(exerciseFile.exists(), "Exercises.txt should be created");
        assertTrue(answerFile.exists(), "Answers.txt should be created");

        try (BufferedReader exerciseReader = new BufferedReader(new FileReader(exerciseFile));
             BufferedReader answerReader = new BufferedReader(new FileReader(answerFile))) {

            List<String> exercises = new ArrayList<>();
            List<String> answers = new ArrayList<>();
            String line;

            while ((line = exerciseReader.readLine()) != null) {
                exercises.add(line);
            }

            while ((line = answerReader.readLine()) != null) {
                answers.add(line);
            }

            assertEquals(5, exercises.size(), "There should be 5 exercises");
            assertEquals(5, answers.size(), "There should be 5 answers");

        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }

    @Test
    public void testWriteToFiles() {
        List<String> questions = List.of("1 + 1 =", "2 * 2 =", "3 - 1 =", "4 / 2 =", "5 + 5 =");
        List<String> answers = List.of("2", "4", "2", "2", "10");

        Main.writeToFiles(questions, answers);

        File exerciseFile = new File("Exercises.txt");
        File answerFile = new File("Answers.txt");

        assertTrue(exerciseFile.exists(), "Exercises.txt should be created");
        assertTrue(answerFile.exists(), "Answers.txt should be created");

        try (BufferedReader exerciseReader = new BufferedReader(new FileReader(exerciseFile));
             BufferedReader answerReader = new BufferedReader(new FileReader(answerFile))) {

            List<String> exercises = new ArrayList<>();
            List<String> answersList = new ArrayList<>();
            String line;

            while ((line = exerciseReader.readLine()) != null) {
                exercises.add(line);
            }

            while ((line = answerReader.readLine()) != null) {
                answersList.add(line);
            }

            assertEquals(questions, exercises, "Exercises should match the input questions");
            assertEquals(answers, answersList, "Answers should match the input answers");

        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }

    @Test
    public void testGradeAnswers() {
        List<String> questions = List.of("1 + 1 =", "2 * 2 =", "3 - 1 =", "4 / 2 =", "5 + 5 =");
        List<String> answers = List.of("2", "4", "2", "2", "10");

        Main.writeToFiles(questions, answers);

        Main.gradeAnswers("Exercises.txt", "Answers.txt");

        File gradeFile = new File("Grade.txt");
        assertTrue(gradeFile.exists(), "Grade.txt should be created");

        try (BufferedReader gradeReader = new BufferedReader(new FileReader(gradeFile))) {
            String correctLine = gradeReader.readLine();
            String wrongLine = gradeReader.readLine();

            assertNotNull(correctLine, "Correct line should not be null");
            assertNotNull(wrongLine, "Wrong line should not be null");

            assertTrue(correctLine.startsWith("Correct: 5"), "All answers should be correct");
            assertTrue(wrongLine.startsWith("Wrong: 0"), "No answers should be wrong");

        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }
}
