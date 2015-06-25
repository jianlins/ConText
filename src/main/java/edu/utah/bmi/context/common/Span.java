package edu.utah.bmi.context.common;

/**
 * This a class to store the span information of either Concept or ConText.
 *
 * "id" is an optional field that can be used to store the rule id information, which is easy to be mapped to a score or rule name.
 *
 * Drafted by Jianlin Shi on 6/24/15.
 */
public class Span {
    public int begin, end,width, id;
    public Span(int begin, int end) {
        this.begin = begin;
        this.end = end;
        this.width=end-begin+1;
    }

    public Span(int begin, int end, int id) {
        this.begin = begin;
        this.end = end;
        this.width=end-begin+1;
        this.id=id;
    }
}