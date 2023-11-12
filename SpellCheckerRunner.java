import java.io.File;
import java.util.Scanner;

public class SpellCheckerRunner {
    public static void main(String[] args) {
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
    }
}


