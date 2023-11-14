import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class WordRecommender {

    private HashSet<String> words;

    public WordRecommender(String dictionaryFile) {
        words = new HashSet<>();
        try {
            Scanner scnr = new Scanner(new File(dictionaryFile));
            while (scnr.hasNext()) {
                words.add(scnr.next().toLowerCase());
            }
            scnr.close();
        } catch (FileNotFoundException e) {
            System.out.println("There was an error in opening that file.");
        }
    }

    public double getSimilarity(String word1, String word2) {
        double left = 0.0;
        double right = 0.0;
        for (int i = 0; i < Math.min(word1.length(), word2.length()); i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                left++;
            }
        }

        for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
            if (word1.charAt(word1.length() - 1 - j) == word2.charAt(word2.length() - 1 - j)) {
                right++;
            }
        }
        return (left + right) / 2.0;
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (String candidate : words) {
            if (Math.abs(candidate.length() - word.length()) <= tolerance && getPercent(word, candidate) >= commonPercent) {
                suggestions.add(candidate);
            }
        }
        suggestions.sort((w1, w2) -> Double.compare(getSimilarity(word, w2), getSimilarity(word, w1)));
        return new ArrayList<>(suggestions.subList(0, Math.min(topN, suggestions.size())));
    }

    public double getPercent(String word1, String word2) {
        HashSet<Character> set1 = new HashSet<>();
        HashSet<Character> set2 = new HashSet<>();

        for (char a : word1.toCharArray()) {
            set1.add(a);
        }

        for (char a : word2.toCharArray()) {
            set2.add(a);
        }

        HashSet<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        HashSet<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        return intersection.size() / (double) union.size();
    }

    public boolean isValid(String word) {
        return words.contains(word.toLowerCase());
    }
}