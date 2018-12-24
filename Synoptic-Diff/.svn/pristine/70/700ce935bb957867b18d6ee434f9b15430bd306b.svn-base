/*
 * This code is in part based on Clemens Hammacher's code.
 * 
 * Source: https://ccs.hammacher.name
 * 
 * License: Eclipse Public License v1.0.
 */

package synoptic.model.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import synoptic.main.AbstractMain;
import synoptic.main.options.AbstractOptions;
import synoptic.model.DAGsTraceGraph;
import synoptic.model.EventNode;
import synoptic.model.Partition;
import synoptic.model.interfaces.IGraph;
import synoptic.model.interfaces.INode;
import synoptic.model.interfaces.ITransition;
import synoptic.util.InternalSynopticException;
import synoptic.util.time.ITime;
import daikonizer.DaikonInvariants;

/**
 * Used to export a graph object to a file.
 * 
 * <pre>
 * Currently supports:
 * - GraphViz dot file format
 * - GML file format
 * </pre>
 */
public class GraphExporter {
	static Logger			logger		= Logger.getLogger("GraphExporter");

	/**
	 * A list of common paths to try when searching for the dot executable.
	 * Directory paths to the dot executable should be added here.
	 */
	static final String[]	dotCommands	= { 
			"/usr/bin/dot",
			"/usr/local/bin/dot", 
			"C:\\Programme\\Graphviz2.26\\bin\\dot.exe",
			"C:\\Program Files (x86)\\Graphviz2.26.3\\bin\\dot.exe",
			"C:\\Program Files\\Graphviz 2.28\\bin\\dot.exe",
			"C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe" };

	/**
	 * @return Returns the dot command executable or null on error
	 * @throws InternalSynopticException
	 *             problem looking up a command line option description
	 */
	private static String getDotCommand() {
		for (String dotCommand : dotCommands) {
			File f = new File(dotCommand);
			if (f.exists()) {
				return dotCommand;
			}
		}
		if (AbstractOptions.dotExecutablePath == null) {
			logger.severe("Unable to locate the dot command executable, use cmd line option:\n\t"
					+ AbstractOptions.plumeOpts.getOptDesc("dotExecutablePath"));
		}
		return AbstractOptions.dotExecutablePath;
	}

	/**
	 * Converts a dot file as a png image file using dot. The png file will be
	 * created in the same place as the dot file.
	 * 
	 * @param dotFile
	 *            dot file filename
	 */
	public static String generatePngFileFromDotFile(String fileName) {
		File dotFile = new File(fileName);

		String dotCommand = getDotCommand();
		if (dotCommand == null) {
			// could not locate a dot executable
			return null;
		}

		String imageExt = "png";

		String execCommand = dotCommand + " -O -T" + imageExt + " "
				+ dotFile.getAbsolutePath();

		logger.info("Exporting graph to: " + dotFile.toString() + "."
				+ imageExt);

		Process dotProcess;
		try {
			dotProcess = Runtime.getRuntime().exec(execCommand);
		} catch (IOException e) {
			logger.severe("Could not run dotCommand '" + execCommand + "': "
					+ e.getMessage());
			return null;
		}
		try {
			dotProcess.waitFor();
		} catch (InterruptedException e) {
			logger.severe("Waiting for dot process interrupted '" + execCommand
					+ "': " + e.getMessage());
		}

		return dotFile.toString() + "." + imageExt;
	}

	/**
	 * Exports the graph to a format determined by Main.graphExportFormatter,
	 * writing the resulting string to a file specified by fileName.
	 */
	public static <T extends INode<T>> void exportGraph(String fileName,
			IGraph<T> graph, boolean outputEdgeLabels) throws IOException {
		File f = new File(fileName);
		logger.info("Exporting graph to: " + fileName);
		final PrintWriter writer;
		try {
			writer = new PrintWriter(f);
		} catch (final IOException e) {
			throw new RuntimeException("Error opening file for graph export: "
					+ e.getMessage(), e);
		}
		// /////////////
		exportGraph(writer, graph, outputEdgeLabels);
		// /////////////
		writer.close();
	}

	/**
	 * Exports the graph to a format determined by Main.graphExportFormatter,
	 * writing the resulting string to a file specified by fileName.
	 */
	public static <T extends INode<T>> void exportGraph(String fileName,
			IGraph<T> graph, boolean outputEdgeLabels, List<Integer> numOfTraces)
			throws IOException {
		File f = new File(fileName);
		logger.info("Exporting graph to: " + fileName);
		final PrintWriter writer;
		try {
			writer = new PrintWriter(f);
		} catch (final IOException e) {
			throw new RuntimeException("Error opening file for graph export: "
					+ e.getMessage(), e);
		}
		// /////////////
		exportGraph(writer, graph, outputEdgeLabels, numOfTraces);
		// /////////////
		writer.close();
	}

	/**
	 * Exports the graph to a format determined by Main.graphExportFormatter,
	 * writing the resulting string to writer. The export is done canonically --
	 * two isomorphic graphs will have equivalent outputs. The generated dot/gml
	 * files may then be diff-ed to check if they represent the same graphs.
	 * 
	 * @param <T>
	 *            Graph node type
	 * @param writer
	 *            The writer to use for dot output
	 * @param graph
	 *            The graph to export
	 * @param outputEdgeLabels
	 *            Whether or not to output edge labels
	 * @throws IOException
	 *             In case there is a problem using the writer
	 */
	@SuppressWarnings("unchecked")
	public static <T extends INode<T>> void exportGraph(Writer writer,
			IGraph<T> graph, boolean outputEdgeLabels) throws IOException {
		exportGraph(writer, graph, outputEdgeLabels, new ArrayList<Integer>(-1));
	}

	/**
	 * Exports the graph to a format determined by Main.graphExportFormatter,
	 * writing the resulting string to writer. The export is done canonically --
	 * two isomorphic graphs will have equivalent outputs. The generated dot/gml
	 * files may then be diff-ed to check if they represent the same graphs.
	 * 
	 * @param <T>
	 *            Graph node type
	 * @param writer
	 *            The writer to use for dot output
	 * @param graph
	 *            The graph to export
	 * @param outputEdgeLabels
	 *            Whether or not to output edge labels
	 * @throws IOException
	 *             In case there is a problem using the writer
	 */
	@SuppressWarnings("unchecked")
	public static <T extends INode<T>> void exportGraph(Writer writer,
			IGraph<T> graph, boolean outputEdgeLabels, List<Integer> numOfTraces)
			throws IOException {

		AbstractMain main = AbstractMain.getInstance();
		try {
			// Begin graph.
			writer.write(main.graphExportFormatter.beginGraphString());

			// ////////////////////////// Write out graph body.

			// A mapping between nodes in the graph and the their integer
			// identifiers in the dot output.
			LinkedHashMap<T, Integer> nodeToInt = new LinkedHashMap<T, Integer>();

			// A unique identifier used to represent nodes in the exported file.
			int nodeCnt = 0;

			// NOTE: we must create a new collection so that we do not modify
			// the set maintained by the graph!
			List<T> nodes = new ArrayList<T>(graph.getNodes());
			Collections.sort(nodes);

			// /////////////////////
			// EXPORT NODES:
			Iterator<T> nodesIter = nodes.iterator();
			while (nodesIter.hasNext()) {
				T node = nodesIter.next();

				// On user request, do not show the initial/terminal nodes.
				if ((!main.options.showInitialNode && node.isInitial())
						|| (!main.options.showTerminalNode && node.isTerminal())) {
					// Remove the node from nodes to export (so that we do not
					// show the edges corresponding to the nodes).
					nodesIter.remove();
					continue;
				}

				// Output the node record -- its id along with its attributes.
				writer.write(main.graphExportFormatter.nodeToString(nodeCnt,
						node, node.isInitial(), node.isTerminal()));
				// Remember the identifier assigned to this node (used for
				// outputting transitions between nodes).
				nodeToInt.put(node, nodeCnt);
				nodeCnt += 1;
			}

			// /////////////////////
			// EXPORT EDGES:
			// Export all the edges corresponding to the nodes in the graph.
			for (INode<T> node : nodes) {
				List<? extends ITransition<T>> transitions;
				if (main.options.stateProcessing && node instanceof Partition) {
					// We need to do these castings because INode<T> doesn't
					// have getTransitionsWithDaikonInvariants method, but
					// Partition has.
					Partition partition = (Partition) node;
					transitions = (List<? extends ITransition<T>>) partition
							.getTransitionsWithDaikonInvariants();
				}
				// If state processing isn't enabled, then output weights, else
				// add the edge labels later.
				else if (outputEdgeLabels && !main.options.stateProcessing) {
					transitions = node.getWeightedTransitions();
				} else {
					transitions = node.getAllTransitions();
				}
				// Sort the transitions for canonical output.
				Collections.sort(transitions);

				for (ITransition<T> trans : transitions) {
					// If for some reason we don't have a unique identifier for
					// the source or the target node then we skip this
					// transition. For example, this may occur if the target is
					// a terminal node and Main.showTerminalNode is false.
					if (!nodeToInt.containsKey(trans.getSource())
							|| !nodeToInt.containsKey(trans.getTarget())) {
						continue;
					}
					int nodeSrc = nodeToInt.get(trans.getSource());
					int nodeDst = nodeToInt.get(trans.getTarget());
					String s = "";

					// FIXME: special casing to handle PO trace graphs
					// correctly (trace graphs are composed of EventNodes).

					if (graph.getClass() == DAGsTraceGraph.class) {

						// NOTE: The extra casts are necessary for this to work
						// in Java 1.6, see here:
						// http://bugs.sun.com/view_bug.do?bug_id=6932571
						assert (((INode<?>) (trans.getSource())) instanceof EventNode);
						s = main.graphExportFormatter.edgeToStringWithTraceId(
								nodeSrc, nodeDst,
								((EventNode) ((INode<?>) trans.getSource()))
										.getTraceID(), trans.getRelation());
					} else {
						// Set edge and edge label for Perfume
						if (main.options.usePerformanceInfo) {
							// Calculate the min, max, and median time deltas
							ITime timeMin = null;
							ITime timeMax = null;
							ITime timeMedian = null;
							if (trans.getDeltaSeries() != null) {
								timeMin = trans.getDeltaSeries().computeMin();
								timeMax = trans.getDeltaSeries().computeMax();

								// Compute median only if requested
								if (main.options.showMedian) {
									timeMedian = trans.getDeltaSeries()
											.computeMed();
								}
							}

							if (outputEdgeLabels) {
								// Show both metrics and probabilities on edges
								double prob = trans.getProbability();
								s = main.graphExportFormatter
										.edgeToStringWithITimesAndProb(nodeSrc,
												nodeDst, timeMin, timeMax,
												timeMedian, prob,
												trans.getRelation());
							}

							else {
								// Show only metrics on edges
								s = main.graphExportFormatter
										.edgeToStringWithITimes(nodeSrc,
												nodeDst, timeMin, timeMax,
												timeMedian, trans.getRelation());
							}
						} else if (outputEdgeLabels) {

							if (main.options.stateProcessing) {
								// Label Daikon invariants on this transition.
								DaikonInvariants daikonInvs = trans.getLabels()
										.getDaikonInvariants();
								assert (daikonInvs != null);
								s = main.graphExportFormatter
										.edgeToStringWithDaikonInvs(nodeSrc,
												nodeDst, daikonInvs,
												trans.getRelation());

							} else {
								double prob = trans.getProbability();

								int counter = trans.getCount();

								try {
									Partition nodeSource = (Partition) trans
											.getSource();

									Partition nodeDest = (Partition) trans
											.getTarget();

									Set<Integer> srctraceIdSet = new HashSet<Integer>();
									Set<Integer> dsttraceIdSet = new HashSet<Integer>();

									Set<String> stringSet = new HashSet<String>();

									for (EventNode enode : nodeSource
											.getEventNodes()) {
										srctraceIdSet.add(enode.getTraceID());
									}

									for (EventNode enode : nodeDest
											.getEventNodes()) {

										dsttraceIdSet.add(enode.getTraceID());
									}

									srctraceIdSet.clear();
									for (EventNode enode : nodeSource
											.getEventNodes()) {
										for (EventNode dst : enode
												.getAllSuccessors()) {
											if (dst.getEType()
													.toString()
													.equals(nodeDest.getEType()
															.toString())) {
												if (nodeSource.isInitial()) {
													srctraceIdSet.add(dst
															.getTraceID());
												} else {
													srctraceIdSet.add(enode
															.getTraceID());
												}
											}
										}
									}

									// } else {
									// srctraceIdSet.retainAll(dsttraceIdSet);
									// }

									// for (EventNode enode : nodeSource
									// .getEventNodes()) {
									//
									// if (enode.isInitial()) {
									// // continue;
									// }
									//
									// Set<EventNode> eventNodeList = enode
									// .getAllSuccessors();
									//
									// for (EventNode e : eventNodeList) {
									// if (nodeDest.getEventNodes()
									// .contains(e)) {
									// srctraceIdSet.add(e
									// .getTraceID());
									// }
									//
									// if (e.isTerminal()
									// && nodeDest.isTerminal()) {
									// srctraceIdSet.add(enode
									// .getTraceID());
									// }
									//
									// }
									// }

									// int maxLogFirst = numOfTraces - 1;

									boolean srcContainFirst = false;
									boolean srcContainSec = false;

									for (Integer id : srctraceIdSet) {
										int logIndex = getLogIndex(id,
												numOfTraces);
										// if (id <= maxLogFirst) {
										// srcContainFirst = true;
										// } else {
										// srcContainSec = true;
										// }

										stringSet.add(Integer
												.toString(logIndex));
									}

									// if (srcContainFirst ^ srcContainSec) {
									// if (srcContainFirst) {
									// stringSet.add("1");
									// }
									//
									// if (srcContainSec) {
									// stringSet.add("2");
									// }
									// }

									// Set<Integer> dsttraceIdSet = new
									// HashSet<Integer>();

									// for (EventNode enode : nodeDest
									// .getEventNodes()) {
									//
									// if (enode.isTerminal()) {
									// continue;
									// }
									//
									// // dsttraceIdSet.add(enode.getTraceID());
									//
									// Set<EventNode> eventNodeList = enode
									// .getal();
									//
									// for (EventNode e : eventNodeList) {
									// if (nodeDest.getEventNodes()
									// .contains(e)) {
									// srctraceIdSet.add(e
									// .getTraceID());
									// }
									// }
									// }

									// boolean dstcontainFirst = false;
									// boolean dstcontainSec = false;
									//
									// for (Integer id : dsttraceIdSet) {
									// if (id <= maxLogFirst) {
									// dstcontainFirst = true;
									// } else {
									// dstcontainSec = true;
									// }
									// }
									//
									// Set<String> stringSet = new
									// HashSet<String>();
									//
									// if (srctraceIdSet.isEmpty()) {
									// if (dstcontainFirst) {
									// stringSet.add("1");
									// }
									//
									// if (dstcontainSec) {
									// stringSet.add("2");
									// }
									// } else if (dsttraceIdSet.isEmpty()) {
									// if (srContainFirst) {
									// stringSet.add("1");
									// }
									//
									// if (srcContainSec) {
									// stringSet.add("2");
									// }
									// } else {
									// if (dsttraceIdSet.size() == 1) {
									// Integer traceId = dsttraceIdSet
									// .iterator().next();
									// String log = traceId <= maxLogFirst ? "1"
									// : "2";
									// stringSet.add(log);
									// } else if (srctraceIdSet.size() == 1) {
									// Integer traceId = srctraceIdSet
									// .iterator().next();
									// String log = traceId <= maxLogFirst ? "1"
									// : "2";
									// stringSet.add(log);
									// } else {
									// if (dstcontainFirst) {
									// stringSet.add("1");
									// }
									//
									// if (dstcontainSec) {
									// stringSet.add("2");
									// }
									// }
									// }

									s = main.graphExportFormatter
											.edgeToStringWithProb(nodeSrc,
													nodeDst, prob, stringSet);

								} catch (Exception e) {
									e.printStackTrace();
								}
								// s = main.graphExportFormatter
								// .edgeToStringWithProb(nodeSrc, nodeDst,
								// prob, trans.getRelation());
							}
						} else {
							s = main.graphExportFormatter
									.edgeToStringWithNoProb(nodeSrc, nodeDst,
											trans.getRelation());
						}
					}
					writer.write(s);

				}
			}

			// //////////////////////////

			// End graph.
			writer.write(main.graphExportFormatter.endGraphString());

		} catch (IOException e) {
			throw new RuntimeException(
					"Error writing to file during graph export: "
							+ e.getMessage(), e);
		}
		return;
	}

	public static int getLogIndex(int id, List<Integer> logsTraces) {
		int result = 1;
		int sum = 0;
		for (int logTrace : logsTraces) {
			sum += logTrace;

			if (id < sum) {
				return result;
			}

			result++;
		}

		return result;
	}
	// private static void exportSCCsWithInvariants(GraphVizExporter e,
	// PartitionGraph pg) throws Exception {
	// StronglyConnectedComponents<Partition> sccs = new
	// StronglyConnectedComponents<Partition>(
	// pg);
	// int partN = 0;
	// for (Set<Partition> scc : sccs) {
	// Graph<Partition> graph = new Graph<Partition>();
	// Graph<LogEvent> messageGraph = new Graph<LogEvent>();
	// for (Partition p : scc) {
	// graph.add(p);
	// for (LogEvent m : p.getMessages()) {
	// messageGraph.add(m);
	// }
	// }
	// String prefix = "";
	// e.exportAsDotAndPngFast(
	// prefix + "output/peterson/messageGraph.dot", messageGraph);
	// e.exportAsDotAndPngFast(prefix + "output/peterson/partition-"
	// + partN + ".dot", graph);
	// System.out.println(scc);
	// TemporalInvariantSet.generateStructuralInvariants = true;
	// TemporalInvariantSet s2 = TemporalInvariantSet
	// .computeInvariantsUsingTC(messageGraph);
	// e.exportAsDotAndPngFast(prefix + "output/peterson/partition-"
	// + partN + "-synoptic.invariants.dot",
	// s2.getInvariantGraph("AP"));
	// TemporalInvariantSet.generateStructuralInvariants = false;
	// partN++;
	// }
	// }
}
