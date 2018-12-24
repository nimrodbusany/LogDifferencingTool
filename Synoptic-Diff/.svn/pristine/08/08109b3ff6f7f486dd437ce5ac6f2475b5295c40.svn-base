package synopticdiff.invariants.constraints;

import synopticdiff.util.time.ITime;

/**
 * Represents a time threshold constraint on some temporal invariant.
 *
 * 
 */
public interface IThresholdConstraint {
    /**
     * @return time constraint
     */
    ITime getThreshold();

    /**
     * @return true if given time satisfies internal inequality with respect to
     *         the threshold.
     */
    boolean evaluate(ITime t);

}
