package com.shawn;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/16 09:40
 * Last Modified Time: 2015/10/26 10:07
 *
 * Class Name:         ICTCLASDemo
 * Class Function:
 *                     目前完成了基本框架的开发，在目录下读入question.txt中的问题，然后自动
 *                     将其切分为题干和选项；而后由关系分析组件分析出先关概念词组；最后计算题
 *                     干词组与选项词组的相似度，选择相似度最大的作为答案。
 *                     关系分析以“知识图谱”作为搜索基础，通过在知识图谱中宽搜来得到相关概念。
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ICTCLASDemo {

    public Options[] options = new Options[5];
    public HashMap<String, Concept> kGraph = new HashMap<>();

    public void init() throws IOException, ClassNotFoundException {
        String inputString = BasicIO.readFile2String("question.txt");
        inputString += "E. ";

        for (int i = 0; i < 5; i++) {
            options[i] = new Options(inputString.split((char)Integer.sum(65, i) + ".")[0]);
            inputString = inputString.split((char)Integer.sum(65, i) + ".")[1];
        }

    }

    public void printAnswer(int i) {
        if (i == 0) {
            System.out.println("C" + "(未能给出答案，该答案是蒙的。)");
        }
        else {
            System.out.println( (char)Integer.sum(64, i) );
        }
    }

    public static void main (String[] args) throws Exception {
        ICTCLASDemo self = new ICTCLASDemo();
        self.init();
        KGraph kgraph = new KGraph();
        self.kGraph = kgraph.getkGraphFromXML();
        //System.out.println(self.kGraph);



        SimilarityComputer similarityComputer = new SimilarityComputer();
        RelationAnalyzer relationAnalyzer = new RelationAnalyzer();
        int maxSimilarityIndex = 0;
        double maxSimilarityValue = 0.0;
        double similarity = 0.0;

        HashSet<String> compareSet0 = new HashSet<>(self.options[0].words);
        compareSet0.addAll(self.options[0].relatedConcepts);

        for (int i = 0; i < 5; i++) {

            self.options[i].relatedConcepts = relationAnalyzer.getRelatedConceptSet(self.options[i].words, self.kGraph);
            HashSet<String> compareSet = new HashSet<>(self.options[i].words);
            compareSet.addAll(self.options[i].relatedConcepts);

            similarity = similarityComputer.getSimilarity(compareSet0, compareSet);

            if(i != 0 && similarity > maxSimilarityValue) {
                maxSimilarityIndex = i;
                maxSimilarityValue = similarity;
            }

//            System.out.println("Option #" + i + ":");
//            System.out.println("\tDictionary Keys：" + self.options[i].words);
//            System.out.println("\tRelated Concepts: " + self.options[i].relatedConcepts);
//            System.out.println("\tCompare Set: " + compareSet);
//            System.out.println("\tSimilarity to question: " + similarity);
        }

        self.printAnswer(maxSimilarityIndex);

    }
}
