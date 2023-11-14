import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WordRecommenderTest {
     private WordRecommender recommender;

     @BeforeEach
     public void setUp() {
         recommender = new WordRecommender("engDictionary.txt");
     }

     @Test
    public void testGetSimilarity() {
         double actual = recommender.getSimilarity("hamburger", "hamburger");
         double expected = 9.0;
         assertEquals(expected, actual);

         double actual2 = recommender.getSimilarity("sleep", "sleepy");
         double expected1 = 3.0;
         assertEquals(expected1, actual2);
     }

     @Test
    public void testGetWordSuggestions() {
         ArrayList<String> actual = recommender.getWordSuggestions("head", 2, 0.5, 4);
         assertNotNull(actual);
         assertTrue(actual.size() <= 4);
     }

     @Test
    public void testGetPercent() {
         double actual = recommender.getPercent("abandon", "angry");
         double expected = 0.25;
         assertEquals(expected, actual);

         double actual1 = recommender.getPercent("word", "world");
         double expected1 = 0.8;
         assertEquals(expected1, actual1);
     }

    @Test
    public void testIsValid() {
        assertTrue(recommender.isValid("her"));
        assertFalse(recommender.isValid("hdushif"));
    }

}
