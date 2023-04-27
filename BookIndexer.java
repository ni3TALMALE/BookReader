package Exlat;

import java.io.*;
import java.util.*;

public class BookIndexer {
    private static final String[] PAGE_FILES = {"C:\\Users\\chetan\\Downloads\\Page1.txt", "C:\\Users\\chetan\\Downloads\\Page2.txt", "C:\\Users\\chetan\\Downloads\\Page3.txt"};
    private static final String STOP_WORDS_FILE = "C:\\Users\\chetan\\Downloads\\exclude-words.txt";
    private static final String INDEX_FILE = "C:\\Users\\chetan\\Downloads\\index3.txt";

    public static void main(String[] args) {
        List<String> stopWords = readStopWords(STOP_WORDS_FILE);
        Map<String, Set<Integer>> index = new HashMap<>();
        for (int i = 0; i < PAGE_FILES.length; i++) {
            List<String> words = readWordsFromFile(PAGE_FILES[i], stopWords);
            for (String word : words) {
                Set<Integer> pages = index.getOrDefault(word, new HashSet<>());
                pages.add(i + 1); 
                index.put(word, pages);
            }
        }
        writeIndexToFile(index, INDEX_FILE);
    }

    private static List<String> readWordsFromFile(String fileName, List<String> stopWords) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	 StringTokenizer tokenizer = new StringTokenizer(line, " ,.?!:;\"()[]{}-/0123456789");
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken().toLowerCase();
                    if (!stopWords.contains(word)) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private static List<String> readStopWords(String fileName) {
        List<String> stopWords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    private static void writeIndexToFile(Map<String, Set<Integer>> index, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            List<String> words = new ArrayList<>(index.keySet());
            Collections.sort(words);
            for (String word : words) {
                Set<Integer> pages = index.get(word);
                List<Integer> uniquePages = new ArrayList<>(pages);
                Collections.sort(uniquePages);
                writer.printf("%s : %s\n", word, uniquePages.toString().replaceAll("[\\[\\],]", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

