package synopticdynamic.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import synopticdynamic.algorithms.graphops.PartitionMultiMerge;
import synopticdynamic.model.ChainsTraceGraph;
import synopticdynamic.model.Partition;
import synopticdynamic.model.PartitionGraph;
import synopticdynamic.model.event.EventType;
import synopticdynamic.model.interfaces.INode;
import synopticdynamic.util.Pair;

/**
 * Implements the KTails algorithm as defined in Biermann & Feldman '72.
 */
public class KTails {
	public static Logger	logger;
	static {
		logger = Logger.getLogger("KTails");
	}

	/**
	 * Constructs and returns a PartitionGraph generated by applying kTails with
	 * the given k value to the given trace graph
	 */
	public static PartitionGraph performKTails(ChainsTraceGraph g, int k) {
		// Note: at k == 0, all "states" should be considered equal, but an
		// event-based model cannot express this, thus the assert.
		assert (k > 0);

		double timeStart = System.currentTimeMillis();

		PartitionGraph pGraph = new PartitionGraph(g, false, null);

		System.out.println("Create partition graph took: " + (System.currentTimeMillis() - timeStart));

		timeStart = System.currentTimeMillis();

		kTails(pGraph, k);

		System.out.println("Time ktails took "
				+ Double.toString(System.currentTimeMillis() - timeStart));
		return pGraph;
	}
	
	/**
	 * Constructs and returns a PartitionGraph generated by applying kTails with
	 * the given k value and k prime to states which contains the sensitive labels to the given trace graph
	 */
	public static PartitionGraph performSensitiveKTails(ChainsTraceGraph g, int k, int maxSensitiveK,
			ArrayList<Pair<Integer, List<String>>> sensitiveLabelsAndK) {
		// Note: at k == 0, all "states" should be considered equal, but an
		// event-based model cannot express this, thus the assert.
		assert (k > 0);

		double timeStart = System.currentTimeMillis();

		PartitionGraph pGraph = new PartitionGraph(g, false, null);

		System.out.println("Create partition graph took: " + (System.currentTimeMillis() - timeStart));

		timeStart = System.currentTimeMillis();

		sensitiveKTails(pGraph, k, maxSensitiveK, sensitiveLabelsAndK);

		System.out.println("Time sensative ktails took "
				+ Double.toString(System.currentTimeMillis() - timeStart));
		return pGraph;
	}

	/**
	 * Finds and executes all possible k-equivalent merges in pGraph.
	 */
	private static void kTails(PartitionGraph pGraph, int k) {
		// Note: at k == 0, all "states" should be considered equal, but an
		// event-based model cannot express this, thus the assert.
		assert (k > 0);

		// Keeps track of the merges that we want to perform.
		Set<PartitionMultiMerge> merges = new LinkedHashSet<PartitionMultiMerge>();

		// List of all partitions -- needed for ordering partitions in the loops
		// below.
		List<Partition> partitions = new ArrayList<Partition>(pGraph.getNodes());

		// Partitions that belong to a merge.
		List<Partition> mergedPartitions = new ArrayList<Partition>();

		// Maps a partition to the set of strings of length <= k reachable from
		// the partition.
		// TODO: this is inefficient, ideally we would Map sets of strings to
		// partitions with those sets!
		Map<Partition, Set<List<EventType>>> kStringsMap = new LinkedHashMap<Partition, Set<List<EventType>>>();

		// int remaining = partitions.size();
		// Build the kStringsMap
		logger.fine("Pre-computing [node -> ktail set] map");
		double timeStart = System.currentTimeMillis();
		for (Partition P : partitions) {
			// logger.info("Remaining kTails pre-mining = " + remaining);
			// remaining -= 1;
			Set<List<EventType>> ret = getNodeKStrings(P, k);
			int maxListLen = 0;
			for (List<EventType> l : ret) {
				maxListLen = Math.max(l.size(), maxListLen);
			}
			P.setkParam(maxListLen);
			kStringsMap.put(P, ret);
		}

		logger.fine("Pre-computed map: " + kStringsMap.toString());

		logger.fine("Finding sets of nodes that are k-equivalent.");

		System.out.println("Partition size :" + partitions.size() + "\n");
		for (int i = 0; i < partitions.size(); i++) {
			if ((i % 100) == 0)
				logger.fine("Remaining kTails n^2 checking = " + (partitions.size() - i));
			
			Partition Pi = partitions.get(i);

			// Skip partition Pi if it has already been merged previously.
			// Since k-equivalence is transitive, this merge already contains
			// all the k-equivalent partitions.
			if (mergedPartitions.contains(Pi)) {
				continue;
			}

			// m will track all partitions to be merged with Pi.
			PartitionMultiMerge m = null;

			Set<List<EventType>> PiKStrings = kStringsMap.get(Pi);

			// Can't merge a partition with itself. So skip i=j. Also, merging
			// is commutative so if we've tried merge(p1,p2), then we don't have
			// to try/check merge(p2,p1). So skip j < i.
			for (int j = i + 1; j < partitions.size(); j++) {
				Partition Pj = partitions.get(j);
				// If we merged p1 and p2 previously, and now we are merging
				// p2 and p3, then p3 _must_ be in the p1+p2 partition, since we
				// must have already compared the ktails of p1 and p3
				// previously.
				if (mergedPartitions.contains(Pj)) {
					continue;
				}

				Set<List<EventType>> PjKStrings = kStringsMap.get(Pj);
				if (!PiKStrings.equals(PjKStrings)) {
					continue;
				}

				logger.fine("Merging " + Pi + " and " + Pj);

				if (m == null) {
					List<Partition> list = new ArrayList<Partition>();
					list.add(Pj);
					m = new PartitionMultiMerge(Pi, list);
					merges.add(m);
				} else {
					m.addToMerge(Pj);
				}

				// We don't need to add Pi to mergedPartitions, since we
				// will never come back to it in the check above.
				mergedPartitions.add(Pj);
			}
		}

		System.out.println("Time first part took "
				+ Double.toString(System.currentTimeMillis() - timeStart));

		logger.fine("Applying merges.");

		logger.info("# merges : " + merges.size());
		for (PartitionMultiMerge merge : merges) {
			pGraph.apply(merge);

		}

		return;

	}

	/**
	 * Finds and executes all possible k-equivalent merges in pGraph.
	 */
	private static void sensitiveKTails(PartitionGraph pGraph, int k, int maxSensitiveK, 
			ArrayList<Pair<Integer, List<String>>> sensitiveLabelsAndK) {
		// Note: at k == 0, all "states" should be considered equal, but an
		// event-based model cannot express this, thus the assert.
		assert (k > 0);

		// Keeps track of the merges that we want to perform.
		Set<PartitionMultiMerge> merges = new LinkedHashSet<PartitionMultiMerge>();

		// List of all partitions -- needed for ordering partitions in the loops
		// below.
		List<Partition> partitions = new ArrayList<Partition>(pGraph.getNodes());

		// Partitions that belong to a merge.
		List<Partition> mergedPartitions = new ArrayList<Partition>();

		// Maps a partition to the set of strings of length <= k reachable from
		// the partition.
		// TODO: this is inefficient, ideally we would Map sets of strings to
		// partitions with those sets!
		Map<Partition, Set<List<EventType>>> kStringsMap = new LinkedHashMap<Partition, Set<List<EventType>>>();

		// int remaining = partitions.size();
		// Build the kStringsMap
		logger.fine("Pre-computing [node -> ktail set] map");
		double timeStart = System.currentTimeMillis();
		for (Partition P : partitions) {
			//Calculate to all as if they are contains the sensitive labels
			Set<List<EventType>> ret = getNodeKStrings(P, maxSensitiveK);
			
			//TODO - for all my futures need to to get the highest k that I m intersect with the corresponding list
			int actualK = getActualKBaseOnMyFuture(ret, sensitiveLabelsAndK, k);
			//Need to remove redundant lists (size > k)
			Iterator<List<EventType>> iter = ret.iterator();
			while (iter.hasNext()) {
				if(iter.next().size() > actualK){
			        iter.remove();
			    }
			}
			
			int maxListLen = 0;
			for (List<EventType> l : ret) {
				maxListLen = Math.max(l.size(), maxListLen);
			}
			P.setkParam(maxListLen);
			kStringsMap.put(P, ret);
		}

		logger.fine("Pre-computed map: " + kStringsMap.toString());

		logger.fine("Finding sets of nodes that are k-equivalent.");

		System.out.println("Partition size :" + partitions.size() + "\n");
		for (int i = 0; i < partitions.size(); i++) {
			if ((i % 100) == 0)
				logger.fine("Remaining kTails n^2 checking = " + (partitions.size() - i));
			
			Partition Pi = partitions.get(i);

			// Skip partition Pi if it has already been merged previously.
			// Since k-equivalence is transitive, this merge already contains
			// all the k-equivalent partitions.
			if (mergedPartitions.contains(Pi)) {
				continue;
			}

			// m will track all partitions to be merged with Pi.
			PartitionMultiMerge m = null;

			Set<List<EventType>> PiKStrings = kStringsMap.get(Pi);

			// Can't merge a partition with itself. So skip i=j. Also, merging
			// is commutative so if we've tried merge(p1,p2), then we don't have
			// to try/check merge(p2,p1). So skip j < i.
			for (int j = i + 1; j < partitions.size(); j++) {
				Partition Pj = partitions.get(j);
				// If we merged p1 and p2 previously, and now we are merging
				// p2 and p3, then p3 _must_ be in the p1+p2 partition, since we
				// must have already compared the ktails of p1 and p3
				// previously.
				if (mergedPartitions.contains(Pj)) {
					continue;
				}

				Set<List<EventType>> PjKStrings = kStringsMap.get(Pj);
				if (!PiKStrings.equals(PjKStrings)) {
					continue;
				}

				logger.fine("Merging " + Pi + " and " + Pj);

				if (m == null) {
					List<Partition> list = new ArrayList<Partition>();
					list.add(Pj);
					m = new PartitionMultiMerge(Pi, list);
					merges.add(m);
				} else {
					m.addToMerge(Pj);
				}

				// We don't need to add Pi to mergedPartitions, since we
				// will never come back to it in the check above.
				mergedPartitions.add(Pj);
			}
		}

		System.out.println("Time first part took "
				+ Double.toString(System.currentTimeMillis() - timeStart));

		logger.fine("Applying merges.");

		logger.info("# merges : " + merges.size());
		for (PartitionMultiMerge merge : merges) {
			pGraph.apply(merge);

		}

		return;

	}
	
	private static int getActualKBaseOnMyFuture(Set<List<EventType>> ret,
			ArrayList<Pair<Integer, List<String>>> sensitiveLabelsAndK, int normalK) {
		for (Pair<Integer, List<String>> p : sensitiveLabelsAndK) {
			if (isFutureConatainsSensitiveLabels(ret, p.getRight())) {
				return p.getLeft();
			}
		}
		return normalK;
	}

	private static boolean isFutureConatainsSensitiveLabels(Set<List<EventType>> ret, List<String> sensitiveLabels) {
		for (List<EventType> l : ret) {
			if (l.stream().anyMatch(event -> sensitiveLabels.contains(event.getETypeLabel())))
				return true;
		}
		return false;
	}

	static public <NodeType extends INode<NodeType>> boolean kEquals(
			NodeType n1, NodeType n2, int k) {
		// Note: at k == 0, all "states" should be considered equal, but an
		// event-based model cannot express this, thus the assert.
		assert (k > 0);

		if (k == 1) {
			return n1.getEType().equals(n2.getEType());
		}

		// Optimization.
		if (!n1.getEType().equals(n2.getEType())) {
			return false;
		}

		Set<List<EventType>> n1Strings = getNodeKStrings(n1, k);
		Set<List<EventType>> n2Strings = getNodeKStrings(n2, k);
		if (n1Strings.size() < k)
			System.out.println("n1 reached max depth" + k + "\n");
		if (n2Strings.size() < k)
			System.out.println("n2 reached max depth" + k + "\n");
		
		if (n1Strings.equals(n2Strings)) {
			return true;
		}
		return false;
	}

	/**
	 * @param k
	 * @param P
	 * @return
	 */
	private static <NodeType extends INode<NodeType>> Set<List<EventType>> getNodeKStrings(
			NodeType P, int k) {
		assert (k >= 0);

		if (k == 0) {
			return Collections.emptySet();
		}

		Set<List<EventType>> prefixes = new LinkedHashSet<List<EventType>>();
		List<EventType> prefix = new ArrayList<EventType>();
		prefix.add(P.getEType());
		prefixes.add(prefix);

		Set<List<EventType>> ret = new LinkedHashSet<List<EventType>>();
		ret.addAll(prefixes);
		for (NodeType child : P.getAllSuccessors()) {
			// note: prefixes cannot mutate during this loop.
			ret.addAll(getNodeKStringHelper(child, k - 1, prefixes));
		}
		return ret;
	}

	/**
	 * Helper for getNodeKString. Returns a set of lists of EventTypes, which
	 * represents a set of strings that are of length <= k and which can be
	 * constructed by starting at P.
	 * 
	 * @param P
	 * @param k
	 * @param parentPrefixes
	 *            : cannot be modified.
	 * @return
	 */
	private static <NodeType extends INode<NodeType>> Set<List<EventType>> getNodeKStringHelper(
			NodeType P, int k, Set<List<EventType>> parentPrefixes) {
		assert (k >= 0);

		if (k == 0) {
			return Collections.emptySet();
		}

		Set<List<EventType>> newPrefixes = new LinkedHashSet<List<EventType>>();

		for (List<EventType> prefix : parentPrefixes) {
			// Have to copy the prefix into newPrefixes, completely!
			List<EventType> prefixCopy = new ArrayList<EventType>();
			prefixCopy.addAll(prefix);
			prefixCopy.add(P.getEType());
			newPrefixes.add(prefixCopy);
		}

		if (k == 1) {
			return newPrefixes;
		}

		// We always return at least the new prefixes we've constructed.
		Set<List<EventType>> ret = newPrefixes;
		for (NodeType child : P.getAllSuccessors()) {
			ret.addAll(getNodeKStringHelper(child, k - 1, newPrefixes));
		}

		return ret;
	}
	
	/**
	 * Returns an integer describing the maximal depth from a node (until we have no more child).
	 * We assume that we have no circles and only one child.
	 * 
	 * @param n
	 * @return the max depth from a node
	 */
	static public <NodeType extends INode<NodeType>> int getMaxK(NodeType n) {

		if (n.getAllSuccessors().size() == 0)
			return 1;
		
		assert n.getAllSuccessors().size() == 1;
		
		return 1 + getMaxK(n.getAllSuccessors().iterator().next());
	}

	/**
	 * Returns a list of the max lists of string describing the maximal depth from a node (until we have no more child).
	 * We assume that we have no circles and only one child.
	 * 
	 * @param n
	 * @returnfull list os string for all possible depth 
	 */
	static public <NodeType extends INode<NodeType>> Set<List<EventType>> getMaxKStringList(NodeType n) {
		return getNodeKStrings(n, Integer.MAX_VALUE);
	}
}
