import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class SpellChecker {
    private WordRecommender recommender;
    private String dictionaryFile;
    public SpellChecker() {
        //this.recommender = new WordRecommender(dictionaryFile);
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        // Input dictionary file
        //String dictionaryFileName = "";
        boolean validDictionary = false;

        System.out.println("Please enter the name of a file to use as a dictionary.");

        while (!validDictionary) {
            dictionaryFile = scanner.nextLine();

            try {
                File file = new File(dictionaryFile);

                if (file.exists()) {
                    System.out.println("Using the dictionary at '" + dictionaryFile + "'.");
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
        this.recommender = new WordRecommender(dictionaryFile);

        // Input file for spell check
        String spellCheckFileName = "";
        boolean validFileForSpellCheck = false;

        System.out.println("\nPlease enter the name of a file to be spell checked.");

        spellCheckFileName = scanner.nextLine();

        try (Scanner fileScanner = new Scanner(new File(spellCheckFileName))) {
            ArrayList<String> words = new ArrayList<>();

            while (fileScanner.hasNext()) {
                words.add(fileScanner.next().toLowerCase());
            }

            System.out.println("words" + words);

            ArrayList<String> checkedWords = checkWords(words);

            String outputFileName = spellCheckFileName.replace(".txt", "_chk.txt");
            writeCheckedWordsToFile(checkedWords, outputFileName);

            System.out.println("Spell checking for '" + spellCheckFileName + "' will be output in '" + outputFileName + "'.");
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }

        while (!validFileForSpellCheck) {
            spellCheckFileName = scanner.nextLine();

            try {
                File spellCheckFile = new File(spellCheckFileName);

                if (spellCheckFile.exists()) {
                    System.out.println("Spell checking for '" + spellCheckFileName + "' will be output in '" + spellCheckFileName.replace(".txt", "_chk.txt") + "'.");
                    validFileForSpellCheck = true;

                    try (Scanner scanner1 = new Scanner(spellCheckFile)) {
                        System.out.println("spellCheckFileName" + spellCheckFileName);
                        while (scanner1.hasNext()) {
                            String word = scanner1.next().toLowerCase();
                            System.out.println("word" + word);

                            if (!recommender.getWordSuggestions(word, 2, 0.5, 4).contains(word)) {
                                handleMisspelledWord(word);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
    private ArrayList<String> checkWords(ArrayList<String> words) {
        ArrayList<String> checkedWords = new ArrayList<>();

        for (String word : words) {
          //  if (!recommender.contains(word) && !checkedWords.contains(word)) {
                spellCheckWord(word);
                checkedWords.add(word);
                System.out.println("word" + word);
           // }
        }
        System.out.println("Checked words" + checkedWords);
        return checkedWords;
    }
    private void spellCheckWord(String word) {
        //System.out.printf(Util.MISSPELL_NOTIFICATION, word);
        ArrayList<String> suggestions = recommender.getWordSuggestions(word, 2, 0.5, 4);

        if (!suggestions.isEmpty()) {
            System.out.println("The following suggestions are available:");
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.println((i + 1) + ". '" + suggestions.get(i) + "'");
            }

            System.out.println("Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            handleUserChoice(word, choice, suggestions);
        } else {
            System.out.println("There are no suggestions in our dictionary for this word.");
            System.out.println("Press 'a' to accept, or press 't' to enter a replacement manually.");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            handleUserChoice(word, choice, new ArrayList<>());
        }
    }
    private void writeCheckedWordsToFile(ArrayList<String> checkedWords, String outputFileName) {
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            for (String word : checkedWords) {
                writer.print(word + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    private void handleUserChoice(String word, String choice, ArrayList<String> suggestions) {
        switch (choice.toLowerCase()) {
            case "r":
                System.out.println("Your word will be replaced with the suggestion you choose.");
                System.out.println("Enter the number corresponding to the word that you want to use for replacement.");

                Scanner scanner = new Scanner(System.in);
                int replacementIndex = scanner.nextInt();

                if (replacementIndex >= 1 && replacementIndex <= suggestions.size()) {
                    replaceWord(word, suggestions.get(replacementIndex - 1));
                } else {
                    System.out.println("Invalid choice. Word will be accepted as is.");
                }
                break;
            case "a":
                System.out.println("Word accepted as is.");
                break;
            case "t":
                System.out.println("Please type the word that will be used as the replacement in the output file.");

                scanner = new Scanner(System.in);
                String replacementWord = scanner.nextLine();

                replaceWord(word, replacementWord);
                break;
            default:
                System.out.println("Please choose one of the valid options.");
                handleUserChoice(word, choice, suggestions);
                break;
        }
    }
    private void replaceWord(String originalWord, String replacementWord) {
        System.out.println("Replacing '" + originalWord + "' with '" + replacementWord + "'.");
    }
    // Additional methods if required
}

