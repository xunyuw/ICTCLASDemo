package com.shawn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/20 13:40
 * Last Modified Time: 2015/10/21 14:52
 *
 * Class Name:         RelationAnalyzer
 * Class Function:
 *                     This class is to analyze the relations between two options.
 */

public class RelationAnalyzer {

    public HashSet<String> relatedConceptSet = null;

    public void generateRelatedConceptSet(Vector<String> inputVec, HashMap<String, HashSet<String>> kGraph) {
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

    public HashSet<String> getRelatedConceptSet(Vector<String> inputVec, HashMap<String, HashSet<String>> kGraph) {
        //System.out.print("RelationAnalyzer getRelatedConceptSet: " + inputVec);
        relatedConceptSet = new HashSet<>();
        generateRelatedConceptSet(inputVec, kGraph);
        return relatedConceptSet;
    }
}
