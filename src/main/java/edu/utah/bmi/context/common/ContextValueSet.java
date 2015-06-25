package edu.utah.bmi.context.common;

/**
 * Classic ConText Assertions and Determinants. Dynamic generated values can be implemented through external classes.
 * <p/>
 * Drafted by Jianlin Shi on 6/24/15.
 */
public class ContextValueSet {

    public static enum Assertion {
        Affirmed, Negated, Possible, Recent, Historical, Hypothetical, Patient, Other;
    }

    public static enum Determinants {
        exp_pre, exp_termin, hist_pre, hist_pseudo, hist_termin, histexp_termin, hypo_pre, hypo_pseudo, hypo_termin, hypoexp_termin, neg_imm, neg_post, neg_pre, neg_pseudo, neg_termin, pos_pre, poss_imm, poss_post, poss_pre, poss_pseudo, pseudo_physical, time_men, time_since
    }
}
