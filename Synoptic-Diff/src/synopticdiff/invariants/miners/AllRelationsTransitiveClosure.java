package synopticdiff.invariants.miners;

import java.util.LinkedHashMap;
import java.util.Set;

import synopticdiff.algorithms.TransitiveClosure;
import synopticdiff.model.EventNode;
import synopticdiff.model.TraceGraph;

/**
 * This class keeps a set of transitive closures for an IGraph, one transitive
 * closure per relation in the IGraph.
 */
public class AllRelationsTransitiveClosure {
    private final LinkedHashMap<String, TransitiveClosure> tcs = new LinkedHashMap<String, TransitiveClosure>();

    public AllRelationsTransitiveClosure(TraceGraph<?> g) {
        for (String relation : g.getRelations()) {
            tcs.put(relation, g.getTransitiveClosure(relation));
        }
    }

    public boolean isReachable(EventNode m, EventNode n, String relation) {
        if (!tcs.containsKey(relation)) {
            return false;
        }
        return tcs.get(relation).isReachable(m, n);
    }

    public TransitiveClosure get(String relation) {
        return tcs.get(relation);
    }

    public Set<String> getRelations() {
        return tcs.keySet();
    }
}
