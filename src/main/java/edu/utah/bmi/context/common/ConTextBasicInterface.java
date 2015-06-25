package edu.utah.bmi.context.common;

import edu.utah.bmi.context.common.ContextValueSet.Assertion;

import java.util.ArrayList;

/**
 * This is a ConText interface class, which defines the common methods that applicable for child classes.
 * In this way, all child classes can be plugged into same code with minimum modification.
 * <p/>
 * Drafted by Jianlin Shi on 6/24/15.
 */
public interface ConTextBasicInterface {

    /**
     * This interface method assume that the input has been tokenized.  "conceptStartPosition" and "conceptEndPosition" is relative position within ArrayList,
     * which is easier to be called. Setting windowsize in every run allows adjusting window size on rule bases.
     *
     * @param tokens
     * @param conceptStartPosition
     * @param conceptEndPosition
     * @param windowsize
     * @return
     */
    ArrayList<Assertion> processContext(ArrayList<String> tokens, int conceptStartPosition, int conceptEndPosition, int windowsize);

    /**
     * This interface method assume that the input has been tokenized as a Span ArrayList.  "conceptStartPosition" and "conceptEndPosition" is relative,
     * while Spans is assumed to be absolute offsets. Extended class can override this default by directly implementation. Setting windowsize in every
     * run allows adjusting window size on rule bases.
     *
     * @param tokens
     * @param conceptStartPosition
     * @param conceptEndPosition
     * @param text
     * @param windowsize
     * @return
     */
    ArrayList<Assertion> processContext(ArrayList<Span> tokens, int conceptStartPosition, int conceptEndPosition, String text, int windowsize);

    /**
     * This interface method is to back compatible with previous implementations.  "conceptBegin" and "conceptEnd" is absolute offset within sentence string.
     * Setting windowsize in every run allows adjusting window size on rule bases.
     *
     * @param sentence
     * @param conceptBegin
     * @param conceptEnd
     * @param text
     * @param windowsize
     * @return
     */
    ArrayList<Assertion> processContext(String sentence, int conceptBegin, int conceptEnd, String text, int windowsize);
}
