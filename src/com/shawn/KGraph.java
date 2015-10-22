package com.shawn;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/20 11:18
 * Last Modified Time: 2015/10/20 16:21
 *
 * Class Name:         KGraph
 * Class Function:
 *                     This class implements a Knowledge Graph(or the so called Knowledge Base),
 *                     provides a HashMap from concepts(String) to a set of related concepts(
 *                     also Strings).
 *                     And yet, the graph is a directed graph.
 */

public class KGraph {
    public HashMap<String, HashSet<String>> kGraph = new HashMap<>();

    public void generateKGraph() {
        File inputFile = new File("k-graph.txt");
        FileInputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String lineString = null;
        String concept = null;
        HashSet<String> relatedSet = null;

        try {
            inputStream = new FileInputStream(inputFile);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while ( (lineString = bufferedReader.readLine()) != null) {
                String splitChar = "\\s+";
                concept = lineString.split(splitChar)[0].trim();
                if (kGraph.containsKey(concept)) {
                    kGraph.get(concept).add(lineString.split(splitChar)[1].trim());
                }
                else {
                    relatedSet = new HashSet<>();
                    relatedSet.add(lineString.split(splitChar)[1].trim());
                    kGraph.put(concept, relatedSet);
                }

                concept = lineString.split(splitChar)[1].trim();
                if (kGraph.containsKey(concept)) {
                    kGraph.get(concept).add(lineString.split(splitChar)[0].trim());
                }
                else {
                    relatedSet = new HashSet<>();
                    relatedSet.add(lineString.split(splitChar)[0].trim());
                    kGraph.put(concept, relatedSet);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("KGraph: 找不到指定文件！");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("KGraph: 读取文件失败!");
            System.exit(1);
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, HashSet<String>> getKGraph() {
        generateKGraph();
        return kGraph;
    }
}
