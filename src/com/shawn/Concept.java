package com.shawn;

import java.util.*;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/26 17:18
 * Last Modified Time: 2015/10/27 08:37
 *
 * Class Name:         Concept
 * Class Function:
 *                     This class implements the concepts of KGraph(short for Knowledge Graph),
 *                     it's a reflection of XML files' concepts.
 */

public class Concept {
    private String name = null;
    private String category = null;
    private String description = null;
    private HashMap<String, List<String>> relatedConcepts= new HashMap<>();

    public void setName (String input) {
        name = input;
    }

    public void setCategory (String input) {
        category = input;
    }

    public void setDescription (String input) {
        description = input;
    }

    public void setRelatedConcepts(String relatedName, String relation) {
        if (relatedConcepts.containsKey(relation)) {
            relatedConcepts.get(relation).add(relatedName);
        }
        else {
            List<String> relationConcepts = new ArrayList<>();
            relationConcepts.add(relatedName);
            relatedConcepts.put(relation, relationConcepts);
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRelatedConceptsByRelation (String relation) {
        return relatedConcepts.get(relation);
    }

    public HashSet<String> getAllRelatedConcepts () {
        HashSet<String> retSet = new HashSet<>();
        for (String key : relatedConcepts.keySet()) {
            retSet.addAll(relatedConcepts.get(key));
        }
        return retSet;
    }

    public String toString () {
        String retStr = "";

        retStr += "\n";
        retStr += ("\t\tName: " + name + "\n");
        retStr += ("\t\tCategory: " + category + "\n");
        retStr += ("\t\tDescription: " + description + "\n");

        retStr += "\t\tRelated Concepts:\n";
        for (String key : relatedConcepts.keySet()) {
            retStr += ("\t\t\tRelation: " + key);
            retStr += ("\tConcepts: " + relatedConcepts.get(key).toString() + "\n");
        }

        return retStr;
    }
}
