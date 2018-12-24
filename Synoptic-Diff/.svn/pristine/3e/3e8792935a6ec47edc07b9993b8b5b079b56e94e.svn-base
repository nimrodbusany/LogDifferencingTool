package synopticdiff.main;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import daikonizer.DaikonInvariants;
import synopticdiff.model.Partition;
import synopticdiff.model.PartitionGraph;
import synopticdiff.model.Transition;
import synopticdiff.model.TransitionLabelType;
import synopticdiff.model.interfaces.ITransition;
import synopticdiff.model.testgeneration.AbstractTestCase;
import synopticdiff.model.testgeneration.Action;

/**
 * SynopticTestGeneration derives abstract test cases from a Synoptic model,
 * i.e., a final partition graph.
 */
public class SynopticTestGeneration {
    /**
     * A dummy relation string for action transitions.
     */
    private static final String timeRelation = "time".intern();

    /**
     * Derives an abstract test suite from a given model.
     * 
     * @return a set of abstract test cases derived from model.
     */
    public static Set<AbstractTestCase> deriveAbstractTests(PartitionGraph model) {
        Set<AbstractTestCase> testSuite = new LinkedHashSet<AbstractTestCase>();
        Set<List<Partition>> paths = model.getAllBoundedPredictedPaths();
        for (List<Partition> path : paths) {
            AbstractTestCase testCase = convertPathToAbstractTest(path);
            testSuite.add(testCase);
        }
        return testSuite;
    }

    /**
     * Converts a path in the model to its corresponding abstract test case.
     * 
     * @return a corresponding abstract test case.
     */
    public static AbstractTestCase convertPathToAbstractTest(
            List<Partition> path) {
        assert !path.isEmpty();
        AbstractMain main = AbstractMain.getInstance();

        Action currAction = new Action(path.get(0).getEType());
        AbstractTestCase testCase = new AbstractTestCase(currAction);

        for (int i = 0; i < path.size() - 1; i++) {
            Partition next = path.get(i + 1);

            Action nextAction = new Action(next.getEType());
            testCase.add(nextAction);
            ITransition<Action> actionTrans = new Transition<Action>(
                    currAction, nextAction, timeRelation);

            if (main.options.stateProcessing) {
                Partition curr = path.get(i);
                List<? extends ITransition<Partition>> transitions = curr
                        .getTransitionsWithDaikonInvariants();

                for (ITransition<Partition> trans : transitions) {
                    if (trans.getTarget().compareTo(next) == 0) {
                        DaikonInvariants invs = trans.getLabels()
                                .getDaikonInvariants();
                        actionTrans.getLabels().setLabel(
                                TransitionLabelType.DAIKON_INVARIANTS_LABEL,
                                invs);
                        break;
                    }
                }
            }
            currAction.addTransition(actionTrans);
            currAction = nextAction;
        }
        return testCase;
    }
}
