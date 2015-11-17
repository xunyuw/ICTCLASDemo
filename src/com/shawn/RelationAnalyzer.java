package com.shawn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/20 13:40
 * Last Modified Time: 2015/10/26 10:43
 *
 * Class Name:         RelationAnalyzer
 * Class Function:
 *                     This class is to analyze the relations between two options.
 */

public class RelationAnalyzer {

    public HashSet<String> relatedConceptSet = null;

    private int min3(final int a, final int b, final int c) {
        return Math.min(Math.min(a, b), c);
    }

    private double levenshteinDistance(final String str1, final String str2) {
        int dis[][];
        int len1 = str1.length();
        int len2 = str2.length();
        int i, j;
        char ch1, ch2;
        int temp;

        if (len1 == 0) {
            return len2;
        }

        if (len2 == 0) {
            return len1;
        }

        dis = new int[len1+1][len2+1];
        for (i = 0; i <= len1; i++) {
            dis[i][0] = i;
        }
        for (j = 0; j <= len2; j++) {
            dis[0][j] = j;
        }

        for (i = 1; i <= len1; i++) {
            ch1 = str1.charAt(i - 1);
            for (j = 1; j <= len2; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                }
                else {
                    temp = 1;
                }
                dis[i][j] = min3(dis[i-1][j] + 1,
                                 dis[i][j-1] + 1,
                                 dis[i-1][j-1] + temp);
            }
        }

        return dis[len1][len2] / Math.min(len1, len2);
    }

    private void generateRelatedConceptSetWithLD(Vector<String> inputVec,
                                                HashMap<String, Concept> kGraph, double maxDis) {
        for (String keyWord : inputVec) {
            for (String key : kGraph.keySet()) {
                if (levenshteinDistance(keyWord, key) <= maxDis) {
                    relatedConceptSet.addAll(kGraph.get(key).getAllRelatedConcepts());
                }
            }
        }
    }

    private void generateRelatedConceptSet(Vector<String> inputVec, HashMap<String, HashSet<String>> kGraph) {
        for (String key : inputVec) {
            //This is because the knowledge graph is a directed graph.
            if (!kGraph.containsKey(key)) {
                continue;
            }
            else {
                relatedConceptSet.addAll(kGraph.get(key));
            }
        }
    }

    public HashSet<String> getRelatedConceptSet(Vector<String> inputVec, HashMap<String, Concept> kGraph) {
        //System.out.print("RelationAnalyzer getRelatedConceptSet: " + inputVec);

        relatedConceptSet = new HashSet<>();

//        generateRelatedConceptSet(inputVec, kGraph);

        double maxDis = 0.5;
        generateRelatedConceptSetWithLD(inputVec, kGraph, maxDis);

        return relatedConceptSet;
    }
}