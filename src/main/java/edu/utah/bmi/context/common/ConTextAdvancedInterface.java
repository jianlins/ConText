package edu.utah.bmi.context.common;

import edu.utah.bmi.context.common.ContextValueSet.Assertion;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This is an extended ConText interface class, which defines two additional interface methods that reserve the evidence Span information for Assertions.
 * This interface is optional, as for most of use cases, storing evidence information is not necessory.
 * <p/>
 * Drafted by Jianlin Shi on 6/24/15.
 */
public interface ConTextAdvancedInterface extends ConTextBasicInterface {

    /**
     * This interface method has the same input as processContext in ConTextInterface, but return a LinkedHashMap with Assertions as the keys,
     * and Spans as the values.
     *
     * @param tokens
     * @param conceptStartPosition
     * @param conceptEndPosition
     * @param relativePosition
     * @param windowsize
     * @return
     */
    LinkedHashMap<Assertion, Span> processContextWEvidence(ArrayList<String> tokens, int conceptStartPosition,
                                                           int conceptEndPosition, boolean relativePosition, int windowsize);

    /**
     * This interface method has the same input as processContext in ConTextInterface, but return a LinkedHashMap with Assertions as the keys,
     * and Spans as the values.
     *
     * @param tokens
     * @param conceptStartPosition
     * @param conceptEndPosition
     * @param text
     * @param windowsize
     * @return
     */
    LinkedHashMap<Assertion, Span> processContextWEvidence(ArrayList<Span> tokens, int conceptStartPosition,
                                                           int conceptEndPosition, String text, int windowsize);

}
