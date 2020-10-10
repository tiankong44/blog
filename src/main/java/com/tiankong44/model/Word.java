package com.tiankong44.model;

import com.tiankong44.util.CosineSimilarity;
import lombok.Data;

import java.util.Objects;

/**
 * @ClassName Word
 * @Description TODO
 * @Author 12481
 * @Date 18:40
 * @Version 1.0
 **/

/**
 * @ClassName Word
 * @Description TODO
 * @Author 12481
 * @Date 18:40
 * @Version 1.0
 **/
@Data
public class Word implements Comparable{


    // 词名
    private String name;
    // 词性
    private String pos;

    // 权重，用于词向量分析
    private Float weight;

    public static void main(String[] args) {
        System.out.println(CosineSimilarity.getSimilarity("什么是构造函数","什么是反射机制"));
    }

    public Word(String name, String pos) {
        this.name = name;
        this.pos = pos;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (name != null) {
            str.append(name);
        }
        if (pos != null) {
            str.append("/").append(pos);
        }

        return str.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }
        if (this.name == null) {
            return -1;
        }
        if (o == null) {
            return 1;
        }
        if (!(o instanceof Word)) {
            return 1;
        }
        String t = ((Word) o).getName();
        if (t == null) {
            return 1;
        }
        return this.name.compareTo(t);

    }
}
