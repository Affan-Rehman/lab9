package poet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class GraphPoet {

    private final Map<String, Map<String, Integer>> graph = new HashMap<>();

    public GraphPoet(File corpus) throws IOException {
        List<String> words = Files.readAllLines(corpus.toPath());
        buildGraph(words);
    }

    public String poem(String input) {
        String[] inputWords = input.split("\\s+");
        List<String> poemWords = new ArrayList<>();

        for (int i = 0; i < inputWords.length - 1; i++) {
            String currentWord = inputWords[i].toLowerCase();
            String nextWord = inputWords[i + 1].toLowerCase();

            poemWords.add(currentWord);
            String bridgeWord = findBridgeWord(currentWord, nextWord);
            if (!bridgeWord.isEmpty()) {
                poemWords.add(bridgeWord.toLowerCase());
            }
        }
        poemWords.add(inputWords[inputWords.length - 1].toLowerCase());

        return String.join(" ", poemWords);
    }

    private String findBridgeWord(String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "";
        }

        Map<String, Integer> word1Connections = graph.get(word1);
        String bridgeWord = "";
        int maxWeight = -1;

        for (Map.Entry<String, Integer> entry : word1Connections.entrySet()) {
            String target = entry.getKey();
            int weight = entry.getValue();

            if (graph.containsKey(target) && graph.get(target).containsKey(word2)) {
                if (weight > maxWeight) {
                    maxWeight = weight;
                    bridgeWord = target;
                }
            }
        }
        return bridgeWord;
    }

    private void buildGraph(List<String> words) {
        for (int i = 0; i < words.size() - 1; i++) {
            String currentWord = words.get(i).toLowerCase();
            String nextWord = words.get(i + 1).toLowerCase();

            graph.putIfAbsent(currentWord, new HashMap<>());
            Map<String, Integer> connections = graph.get(currentWord);
            connections.put(nextWord, connections.getOrDefault(nextWord, 0) + 1);
        }
    }
}
