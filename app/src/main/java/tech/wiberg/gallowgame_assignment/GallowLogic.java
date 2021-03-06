package tech.wiberg.gallowgame_assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class GallowLogic {
    private ArrayList<String> possibleWords = new ArrayList<>();
    private String word;
    private ArrayList<String> usedLetters = new ArrayList<>();
    private String visibleWord;
    private int wrongLettersCount;
    private boolean lastGuessedLetterIsCorrect;
    private boolean gameHasBeenWon;
    private boolean gameHasBeenLost;

    public  String getWord() {
        return this.word;
    }
    public String getVisibleWord() {
        return this.visibleWord;
    }
    public ArrayList<String> getPossibleWords() {
        return this.possibleWords;
    }
    public ArrayList<String> getUsedLetters() {
        return this.usedLetters;
    }
    public int getWrongLettersCount() {
        return this.wrongLettersCount;
    }
    public boolean isLastLetterCorrect() {
        return this.lastGuessedLetterIsCorrect;
    }
    public boolean isGameWon() {
        return this.gameHasBeenWon;
    }
    public boolean isGameLost() {
        return this.gameHasBeenLost;
    }
    public boolean isGameOver() { return this.gameHasBeenLost || this.gameHasBeenWon; }

    public GallowLogic() {
        possibleWords.add("car");
        possibleWords.add("computer");
        possibleWords.add("programming");
        possibleWords.add("highway");
        possibleWords.add("route");
        possibleWords.add("walkway");
        possibleWords.add("snail");
        possibleWords.add("bird");

        reset();
    }

    public void reset() {
        usedLetters.clear();
        wrongLettersCount = 0;
        gameHasBeenWon = false;
        gameHasBeenLost = false;
        word = possibleWords.get(new Random().nextInt(possibleWords.size()));
        updateVisibleWord();
    }

    private void updateVisibleWord() {
        visibleWord = "";
        gameHasBeenWon = true;
        for (int n = 0; n < word.length(); n++) {
            String letter = word.substring(n, n + 1);
            if (usedLetters.contains(letter)) {
                visibleWord = visibleWord + letter;
            } else {
                visibleWord = visibleWord + "*";
                gameHasBeenWon = false;
            }
        }
    }

    public void guess(String letter) {
        letter = letter.toLowerCase();

        if (letter.length() != 1) return;
        System.out.println("User guess: " + letter);
        if(usedLetters.contains(letter)) return;
        if (gameHasBeenWon || gameHasBeenLost) return;

        usedLetters.add(letter);

        if (word.contains(letter)) {
            lastGuessedLetterIsCorrect = true;
            System.out.println("Letter was correct: " + letter);
        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            lastGuessedLetterIsCorrect = false;
            System.out.println("Letter was NOT correct:" + letter);
            wrongLettersCount = wrongLettersCount + 1;
            if (wrongLettersCount > 6) {
                gameHasBeenLost = true;
            }
        }
        updateVisibleWord();
    }

    public void logStatus() {
        System.out.println("---------- ");
        System.out.println("- word (hidden) = " + word);
        System.out.println("- visibleWord = " + visibleWord);
        System.out.println("- wrongLettersCount = " + wrongLettersCount);
        System.out.println("- usedLetters = " + usedLetters);
        if (gameHasBeenLost) System.out.println("- GAME LOST");
        if (gameHasBeenWon) System.out.println("- GAME WON");
        System.out.println("---------- ");
    }

    public static String getUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = br.readLine();
        }
        return sb.toString();
    }

    public void getWordFromDR() throws IOException {
        String data = getUrl("https://dr.dk");
        //System.out.println("data = " + data);

        data = data.substring(data.indexOf("<body")). // fjern headere
                replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
                replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
                replaceAll(" [a-zæøå] "," "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] "," "); // fjern 2-bogstavsord

        System.out.println("data = " + data);
        possibleWords.clear();
        possibleWords.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));

        System.out.println("possibleWords = " + possibleWords);
        reset();
    }
}
