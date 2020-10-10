package com.tiankong44.util;/**
 * @ClassName WordSegment
 * @Description TODO
 * @Author 12481
 * @Date 18:15
 * @Version 1.0
 **/

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.*;

/**
 * @ClassName WordSegment
 * @Description TODO
 * @Author 12481
 * @Date 18:15
 * @Version 1.0
 **/
public class WordSegment {
    public static List<String> TextToWord(StringBuffer text) {
        Result result = ToAnalysis.parse(text.toString());
        List<Term> terms = result.getTerms();
        List<String> word = new ArrayList<>();
        for (Term term : terms) {
            word.add(term.getName());
        }
        return word;
    }

    //集合去重
    public static List<String> distinctBySetOrder(List<String> list) {
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (String t : list) {
            if (set.add(t)) {
                newList.add(t);
            }
        }
        return newList;
    }



}
