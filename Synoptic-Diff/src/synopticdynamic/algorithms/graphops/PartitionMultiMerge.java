package synopticdynamic.algorithms.graphops;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import synopticdynamic.model.EventNode;
import synopticdynamic.model.Partition;
import synopticdynamic.model.PartitionGraph;

/**
 * An operation that provides a multi-merge, i.e. merging multiple partitions
 * into another partition.
 * 
 */
public class PartitionMultiMerge implements IOperation {
	private final Partition retainedPartition;
	private final List<Partition> partitionsToMerge;

	/**
	 * Creates a partition multi-merge.
	 * 
	 * @param partition
	 *            the partition to merge into
	 * @param partitionsToMerge
	 *            the partitions to merge into {@code partition}
	 */
	public PartitionMultiMerge(Partition partition,
			List<Partition> partitionsToMerge) {
		retainedPartition = partition;
		this.partitionsToMerge = partitionsToMerge;
	}

	public void addToMerge(Partition p) {
		assert !this.partitionsToMerge.contains(p);

		this.partitionsToMerge.add(p);
	}

	@Override
	public IOperation commit(PartitionGraph g) {
		ArrayList<Set<EventNode>> newSets = new ArrayList<Set<EventNode>>();
		Set<EventNode> eNodes = new LinkedHashSet<EventNode>();
		eNodes.addAll(retainedPartition.getEventNodes());
		newSets.add(eNodes);
		for (Partition removed : partitionsToMerge) {
			retainedPartition.addEventNodes(removed.getEventNodes());
			eNodes = new LinkedHashSet<EventNode>();
			eNodes.addAll(removed.getEventNodes());
			newSets.add(eNodes);
			removed.removeAllEventNodes();
			g.removePartition(removed);

			// //////////////
			// Invalidate the appropriate elements in the graph's
			// transitionCache

			g.clearNodeAdjacentsCache(removed);
			// //////////////
		}

		g.removeFromCache(retainedPartition);

		// TODO: Provide undo
		return new PartitionMultiSplit(retainedPartition, newSets);
	}

	public Partition getRetainedPartition() {
		return retainedPartition;
	}

	public List<Partition> getPartitionsToMerge() {
		return partitionsToMerge;
	}
	
	
}
