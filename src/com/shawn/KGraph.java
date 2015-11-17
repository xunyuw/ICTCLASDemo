package com.shawn;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.xml.sax.SAXException;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/20 11:18
 * Last Modified Time: 2015/10/27 08:23
 *
 * Class Name:         KGraph
 * Class Function:
 *                     This class implements a Knowledge Graph(or the so called Knowledge Base),
 *                     provides a HashMap from concepts(String) to a set of related concepts(
 *                     also Strings).
 *                     And yet, the graph is a directed graph.
 */

public class KGraph {
    public HashMap<String, HashSet<String>> kGraphFile = new HashMap<>();
    private String xmlFileName = "k-graph.xml";
    private HashMap<String, Concept> kGraphXML = new HashMap<>();

    private void generateKGraphFromFile() {
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
                if (kGraphFile.containsKey(concept)) {
                    kGraphFile.get(concept).add(lineString.split(splitChar)[1].trim());
                }
                else {
                    relatedSet = new HashSet<>();
                    relatedSet.add(lineString.split(splitChar)[1].trim());
                    kGraphFile.put(concept, relatedSet);
                }

                concept = lineString.split(splitChar)[1].trim();
                if (kGraphFile.containsKey(concept)) {
                    kGraphFile.get(concept).add(lineString.split(splitChar)[0].trim());
                }
                else {
                    relatedSet = new HashSet<>();
                    relatedSet.add(lineString.split(splitChar)[0].trim());
                    kGraphFile.put(concept, relatedSet);
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

    private void generateKGraphFromXML() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new File(xmlFileName));

            DOMReader domReader = new DOMReader();
            Document document = domReader.read(doc);

            Element rootElement = document.getRootElement();
//            System.out.println("Root: " + rootElement.getName());

            for (Iterator concepts = rootElement.elementIterator(); concepts.hasNext();) {
                Element concept = (Element) concepts.next();
                Concept myConcept = new Concept();

                myConcept.setName(concept.element("Name").getTextTrim());
                myConcept.setCategory(concept.attributeValue("category"));
                myConcept.setDescription(concept.element("Description").getTextTrim());

                for (Iterator iterofRC = concept.element("RelatedConcepts").elementIterator(); iterofRC.hasNext();) {
                    Element element = (Element) iterofRC.next();
                    myConcept.setRelatedConcepts(element.getTextTrim(), element.attributeValue("relation"));
                }

                kGraphXML.put(myConcept.getName(), myConcept);

//                System.out.println(concept.getName() + ": ");
//                System.out.println("\tCategory: " + concept.attributeValue("category"));
//                for(Iterator iter = concept.elementIterator(); iter.hasNext();) {
//                    Element element = (Element) iter.next();
//                    if (element.getName() != "RelatedConcepts") {
//                        System.out.println("\t" + element.getName() + ": " + element.getData());
//                    }
//                    else {
//                        System.out.println("\tRelated Concepts:");
//                        for (Iterator iterofRC = element.elementIterator(); iterofRC.hasNext();) {
//                            Element relatedRC = (Element) iterofRC.next();
//                            System.out.println("\t\t" + relatedRC.getName() + ": " + relatedRC.getData() +
//                                                "\trelation:" +relatedRC.attributeValue("relation"));
//                        }
//                    }

//                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, HashSet<String>> getKGraphFromFile() {
        generateKGraphFromFile();
        return kGraphFile;
    }

    public HashMap<String, Concept> getkGraphFromXML() {
        generateKGraphFromXML();
        return kGraphXML;
    }

    public static void main(String[] args) {
        KGraph self = new KGraph();
        self.generateKGraphFromXML();
        System.out.println(self.kGraphXML);
    }
}