package com.tiankong44.util;/**
 * @ClassName Tokenizer
 * @Description TODO
 * @Author 12481
 * @Date 18:48
 * @Version 1.0
 **/

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.tiankong44.model.Word;

import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

    /**
     * 分词
     * @return*/
    public static List<Word> segment(String sentence) {

        //1、 采用HanLP中文自然语言处理中标准分词进行分词
        List<Term> termList = HanLP.segment(sentence);
        //上面控制台打印信息就是这里输出的

        //2、重新封装到Word对象中（term.word代表分词后的词语，term.nature代表改词的词性）
        return termList.stream().map(term -> new Word(term.word, term.nature.toString())).collect(Collectors.toList());
    }
}
