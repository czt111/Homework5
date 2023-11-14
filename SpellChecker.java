import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class SpellChecker {
    private WordRecommender recommender;
    private String dictionaryFile;
    public SpellChecker() {
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        // Input dictionary file
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

        do {
            spellCheckFileName = scanner.nextLine();

            try {
                File spellCheckFile = new File(spellCheckFileName);

                if (spellCheckFile.exists()) {
                    String outputFileName = getOutputFileName(spellCheckFileName);
                    spellCheckFile(spellCheckFile, outputFileName);
                    validFileForSpellCheck = true;
                    System.out.println("Spell checking for '" + spellCheckFileName + "' will be output in '" + outputFileName + "'.");
                } else {
                    System.out.println("There was an error in opening that file.");
                    System.out.println("Please enter the name of a file to be spell checked.");
                }
            } catch (Exception e) {
                System.out.println("There was an error in opening that file.");
                System.out.println("Please enter the name of a file to be spell checked.");
            }
        } while (!validFileForSpellCheck);

        scanner.close();
    }
    private String getOutputFileName(String inputFileName) {
        // Append "_chk" to the input file name to create the output file name
        int dotIndex = inputFileName.lastIndexOf('.');
        if (dotIndex != -1) {
            return inputFileName.substring(0, dotIndex) + "_chk" + inputFileName.substring(dotIndex);
        } else {
            return inputFileName + "_chk";
        }
    }

    private void spellCheckFile(File input, String outputFileName) {
        try (Scanner fileScanner = new Scanner(input);
             PrintWriter writer = new PrintWriter(outputFileName)) {

            while (fileScanner.hasNext()) {
                String word = fileScanner.next().toLowerCase();
                if (!recommender.isValid(word)) {
                    ArrayList<String> suggestions = recommender.getWordSuggestions(word, 2, 0.5, 4);
                    if (!suggestions.isEmpty()) {
                        handleMisspelledWord(word, suggestions, writer);
                    } else {
                        writer.print(word + " ");
                    }
                } else {
                    writer.print(word + " ");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }
//}
private void handleMisspelledWord(String misspelledWord, ArrayList<String> suggestions, PrintWriter writer) {
    System.out.println("The word '" + misspelledWord + "' is misspelled.");
    System.out.println("The following suggestions are available:");

    for (int i = 0; i < suggestions.size(); i++) {
        System.out.printf("%d. '%s'%n", i + 1, suggestions.get(i));
    }

    String choice = "";
    while (!isValidChoice(choice)) {
        if (choice == "") {
            System.out.println("Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");
        }
        else {
            System.out.println("Invalid choice. Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");
        }
        Scanner scanner = new Scanner(System.in);
        choice = scanner.nextLine().toLowerCase();
    }
    //System.out.println("Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");

    Scanner scanner1 = new Scanner(System.in);
    //String choice = scanner.nextLine().toLowerCase();

    switch (choice) {
        case "r":
            System.out.println("Your word will be replaced with the suggestion you choose.");
            System.out.println("Enter the number corresponding to the word that you want to use for replacement.");

            int replacementIndex = scanner1.nextInt();

            if (replacementIndex >= 1 && replacementIndex <= suggestions.size()) {
                replaceWord(misspelledWord, suggestions.get(replacementIndex - 1), writer);
            } else {
                System.out.println("Invalid choice. Word will be accepted as is.");
                writer.print(misspelledWord + " ");
            }
            break;
        case "a":
            System.out.println("Word accepted as is.");
            writer.print(misspelledWord + " ");
            break;
        case "t":
            System.out.println("Please type the word that will be used as the replacement in the output file.");

            scanner1 = new Scanner(System.in);
            String replacementWord = scanner1.nextLine();

            replaceWord(misspelledWord, replacementWord, writer);
            break;
        default:
            System.out.println("Please choose one of the valid options.");
            handleMisspelledWord(misspelledWord, suggestions, writer);
            break;
    }
}
    private boolean isValidChoice(String choice) {
        return choice.equals("r") || choice.equals("a") || choice.equals("t");
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
    private void replaceWord(String originalWord, String replacementWord, PrintWriter writer) {
        System.out.println("Replacing '" + originalWord + "' with '" + replacementWord + "'.");
        writer.print(replacementWord + " ");
    }
    // Additional methods if required
}

