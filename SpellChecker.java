import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class SpellChecker {
    private WordRecommender recommender;
    public SpellChecker(String dictionaryFile) {
        this.recommender = new WordRecommender(dictionaryFile);
    }

    public void start(String fileToCheck) {

        Scanner scanner = new Scanner(System.in);

        // Input dictionary file
        String dictionaryFileName = "";
        boolean validDictionary = false;

        System.out.println("Please enter the name of a file to use as a dictionary.");

        while (!validDictionary) {
            dictionaryFileName = scanner.nextLine();

            try {
                File file = new File(dictionaryFileName);

                if (file.exists()) {
                    System.out.println("Using the dictionary at '" + dictionaryFileName + "'.");
                    validDictionary = true;
                } else {
                    System.out.println("There was an error in opening that file.");
                    System.out.println("Please enter the name of a file to use as a dictionary.");
                }
            } catch (Exception e) {
                System.out.println("There was an error in opening that file.");
                System.out.println("Please enter the name of a file to use as a dictionary.");
            }
        }

        // Input file for spell check
        String spellCheckFileName = "";
        boolean validFileForSpellCheck = false;

        System.out.println("\nPlease enter the name of a file to be spell checked.");

        while (!validFileForSpellCheck) {
            spellCheckFileName = scanner.nextLine();

            try {
                File file = new File(spellCheckFileName);

                if (file.exists()) {
                    System.out.println("Spell checking for '" + spellCheckFileName + "' will be output in '" + spellCheckFileName.replace(".txt", "_chk.txt") + "'.");
                    validFileForSpellCheck = true;
                } else {
                    System.out.println("There was an error in opening that file.");
                    System.out.println("Please enter the name of a file to be spell checked.");
                }
            } catch (Exception e) {
                System.out.println("There was an error in opening that file.");
                System.out.println("Please enter the name of a file to be spell checked.");
            }
        }

        scanner.close();

        File spellCheckFile = new File(fileToCheck);
        try (Scanner scanner = new Scanner(spellCheckFile)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();

                if (!recommender.getWordSuggestions(word, 2, 0.5, 4).contains(word)) {
                    handleMisspelledWord(word);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleMisspelledWord(String misspelledWord) {
        //System.out.printf(Util.MISSPELL_NOTIFICATION, misspelledWord);

        ArrayList<String> suggestions = recommender.getWordSuggestions(misspelledWord, 2, 0.5, 4);
        if (suggestions.isEmpty()) {
            System.out.println("There are no suggestions in our dictionary for this word.");
            askForReplacement(misspelledWord);
        } else {
            System.out.println("The following suggestions are available:");
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.printf("%d. '%s'%n", i + 1, suggestions.get(i));
            }
            askForReplacement(misspelledWord);
        }
    }

    private void askForReplacement(String misspelledWord) {
        System.out.println("Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toLowerCase();

        switch (choice) {
            case "r":
                System.out.println("Your word will be replaced with the suggestion you choose.");
                System.out.println("Enter the number corresponding to the word that you want to use for replacement.");
                int suggestionIndex = scanner.nextInt();
                // Implement replacing the word in the file with the chosen suggestion
                break;
            case "a":
                // Implement accepting the misspelled word without changes
                break;
            case "t":
                System.out.println("Please type the word that will be used as the replacement in the output file.");
                String replacement = scanner.nextLine();
                // Implement replacing the word in the file with the provided replacement
                break;
            default:
                System.out.println("Please choose one of the valid options.");
                askForReplacement(misspelledWord);
                break;
        }
    }

    // Additional methods if required
}

