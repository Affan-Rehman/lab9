/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import graph.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {

    private final Map<String, Map<String, Integer>> wordGraph = new HashMap<>();

    public GraphPoet(File corpus) throws IOException {
        List<String> words = Files.readAllLines(corpus.toPath());
        buildGraph(words);
    }

    public String poem(String input) {
        List<String> inputWords = Arrays.asList(input.split("\\s+"));
        StringBuilder poemBuilder = new StringBuilder();

        for (int i = 0; i < inputWords.size() - 1; i++) {
            String currentWord = inputWords.get(i).toLowerCase();
            String nextWord = inputWords.get(i + 1).toLowerCase();

            poemBuilder.append(inputWords.get(i)).append(" ");
            String bridgeWord = findBridgeWord(currentWord, nextWord);
            if (!bridgeWord.isEmpty()) {
                poemBuilder.append(bridgeWord).append(" ");
            }
        }
        poemBuilder.append(inputWords.get(inputWords.size() - 1));

        return poemBuilder.toString();
    }

    private String findBridgeWord(String word1, String word2) {
        if (!wordGraph.containsKey(word1) || !wordGraph.containsKey(word2)) {
            return "";
        }

        Map<String, Integer> connections = wordGraph.get(word1);
        int maxWeight = 0;
        String bridgeWord = "";

        for (String word : connections.keySet()) {
            if (wordGraph.containsKey(word) && wordGraph.get(word).containsKey(word2)) {
                int weight = connections.get(word);
                if (weight > maxWeight) {
                    maxWeight = weight;
                    bridgeWord = word;
                }
            }
        }

        return bridgeWord;
    }

    private void buildGraph(List<String> words) {
        for (int i = 0; i < words.size() - 1; i++) {
            String currentWord = words.get(i).toLowerCase();
            String nextWord = words.get(i + 1).toLowerCase();

            wordGraph.putIfAbsent(currentWord, new HashMap<>());
            Map<String, Integer> connections = wordGraph.get(currentWord);
            connections.put(nextWord, connections.getOrDefault(nextWord, 0) + 1);
        }
    }
}