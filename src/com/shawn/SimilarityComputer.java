package com.shawn;

import java.util.HashSet;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/21 14:33
 * Last Modified Time: 2015/10/21 15:03
 *
 * Class Name:         SimilarityComputer
 * Class Function:
 *                     Compute the similarity between two sets of related concepts of
 *                     each option.
 */

public class SimilarityComputer {
    public double getSimilarity(HashSet<String> target, HashSet<String> source) {
        HashSet<String> joinSet = new HashSet<>();
        HashSet<String> unionSet = new HashSet<>();

        joinSet.addAll(target);
        joinSet.retainAll(source);

        unionSet.addAll(target);
        unionSet.addAll(source);

        int joinSize = joinSet.size();

        //Java的语言特性，如果除数和被除数都是整数，结果也是整数，所以必须强制类型转换。
        return ((double) joinSize) / unionSet.size();
    }
}
