package poet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GraphPoetTest {

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testGraphPoetCreationFromFile() throws IOException {
        File tempFile = File.createTempFile("test_corpus", ".txt");
        GraphPoet graphPoet = new GraphPoet(tempFile);
        assertNotNull(graphPoet);
        tempFile.delete();
    }
    @Test
    public void testPoemGenerationDifferentInput() throws IOException {
        // Create a temporary corpus file for testing
        File tempFile = File.createTempFile("test_corpus", ".txt");
        String corpusContent = "This is a beautiful day in the park.";
        Files.write(tempFile.toPath(), corpusContent.getBytes());

        // Create GraphPoet instance using the temporary corpus file
        GraphPoet graphPoet = new GraphPoet(tempFile);

        // Define input and expected poem
        String input = "a beautiful day";
        String expectedPoem = "a beautiful day";

        // Generate poem and check if it matches the expected output
        String poem = graphPoet.poem(input);
        assertEquals(expectedPoem, poem);

        // Delete the temporary file after the test
        tempFile.delete();
    }

    @Test
    public void testEmptyInputPoemGeneration() throws IOException {
        File tempFile = File.createTempFile("test_corpus", ".txt");
        GraphPoet graphPoet = new GraphPoet(tempFile);

        String input = "";
        String expectedPoem = "";
        String poem = graphPoet.poem(input);

        assertEquals(expectedPoem, poem);
        tempFile.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidFile() throws IOException {
        File nonExistentFile = new File("nonexistent.txt");
        GraphPoet graphPoet = new GraphPoet(nonExistentFile);
    }
}
