package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XParserRegistry;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class SamXESLoader {

	private static final String TRACE_SEPARATOR = "--";
	
	public static void main(String[] args) throws IOException {
		
		String modelName = "columba";
		// convert from xes log to simple event log
		String filename = SystemConstants.LOG_DIRECTORY + "sam/"+modelName+".xes";
		double fraction = 1.0;
		XLog xlog = readLog(filename, fraction);
		PrintWriter writer = new PrintWriter(SystemConstants.LOG_DIRECTORY + "sam/"+modelName+".txt");
		for (XTrace t : xlog) {
			for (XEvent e : t) {
				writer.println(e.getAttributes().get("concept:name"));
			}
			writer.println(TRACE_SEPARATOR);
		}
		writer.close();
	}

	protected static XLog readLog(final String logFile, final double traceFraction) throws IOException {
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		File f = new File(logFile);

		FileInputStream input = new FileInputStream(f);
		String filename = f.getName();

		XParser parser = new XesXmlParser(factory);

		Collection<XLog> logs = null;
		try {
			logs = parser.parse(input);
		} catch (Exception e) {
			logs = null;
		}
		if (logs == null) {
			// try any other parser
			for (XParser p : XParserRegistry.instance().getAvailable()) {
				if (p == parser) {
					continue;
				}
				try {
					logs = p.parse(input);
					if (logs.size() > 0) {
						break;
					}
				} catch (Exception e1) {
					// ignore and move on.
					logs = null;
				}
			}
		}

		// log sanity checks;
		// notify user if the log is awkward / does miss crucial information
		if (logs == null || logs.size() == 0) {
			System.out.println("Warning! No processes contained in log!");
		}

		XLog log = logs.iterator().next();
		if (XConceptExtension.instance().extractName(log) == null) {
			/*
			 * Log name not set. Create a default log name.
			 */
			XConceptExtension.instance().assignName(log, "Anonymous log imported from " + filename);
		}

		if (log.isEmpty()) {
			//throw new Exception("No process instances contained in log!");
			System.out.println("No process instances contained in log!");
		}


		// filter traces to create a smaller log with less branching
		Random r = new Random();
		List<XTrace> toRemove = new LinkedList<XTrace>();
		for (XTrace t : log) {
			if (r.nextDouble() > traceFraction) {
				toRemove.add(t);
			}
		}
		for (XTrace t : toRemove) log.remove(t);
		return log;
	}

}
