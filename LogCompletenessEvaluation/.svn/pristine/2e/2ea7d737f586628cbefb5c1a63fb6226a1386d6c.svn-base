package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.lsc.LSCAlgorithm;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.DemoResults;
import il.ac.tau.cs.smlab.fw.property.impl.ChartConfidenceProperty;
import il.ac.tau.cs.smlab.fw.property.log.AggregatedLogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

/*
 *  goal is to find a pre chart s.t. confidence is
 *  high in full but confidence is low in partial, and
 *  #mined effects in partial != #mined effects in full
 */
public class ExampleFinder {

	int partialLogLength = 30;
	double confidenceDifferenceThreshold = 0.1;
	String model = "columba";
	SpecMiningAlgorithm algorithm = new LSCAlgorithm(new LSCInputParams());
	List<LogProperty<?,?>> properties;
	Alphabet ab;

	public boolean run() throws SpecMiningAlgorithmException, InvalidModelException {
		properties = algorithm.getAlgorithmLogProperties();
		TraceProvider traceProvider;


		ChartConfidenceProperty confidenceProperty = (ChartConfidenceProperty)properties.get(0);

		traceProvider = getTraceProvider();
		System.out.println(partialLogLength);
		ab = traceProvider.getAlphabetInTrace();
		Alphabet interestingEvents = new Alphabet();
		interestingEvents.addEvent("SendableMessage org.columba.mail.composer.MessageComposer.compose(IWorkerStatusController, boolean)");
		interestingEvents.addEvent("void org.columba.mail.smtp.SMTPServer.sendMessage(SendableMessage, IWorkerStatusController)");
		interestingEvents.addEvent("List org.columba.mail.composer.SendableMessage.getRecipients()");
		interestingEvents.addEvent("void org.columba.mail.smtp.SMTPServer.closeConnection()");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3ServerCollection.saveAll()");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Server.save()");
		interestingEvents.addEvent("void org.columba.mail.mailchecking.MailCheckingManager.checkAll()");
		interestingEvents.addEvent("boolean org.columba.mail.mailchecking.POP3MailCheckingAction.isCheckAll()");
		interestingEvents.addEvent("boolean org.columba.mail.pop3.POP3Server.tryToGetLock(Object)");
		interestingEvents.addEvent("List org.columba.mail.pop3.POP3Server.synchronize()");
		interestingEvents.addEvent("int org.columba.mail.pop3.POP3Server.getMessageSize(Object)");
		interestingEvents.addEvent("ColumbaMessage org.columba.mail.pop3.POP3Server.getMessage(Object, IWorkerStatusController)");
		interestingEvents.addEvent("InputStream org.columba.mail.pop3.POP3Store.fetchMessage(int)");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Server.deleteMessage(Object)");
		interestingEvents.addEvent("boolean org.columba.mail.pop3.POP3Store.deleteMessage(Object)");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Server.cleanUpServer()");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Server.logout()");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Store.logout()");
		interestingEvents.addEvent("void org.columba.mail.pop3.POP3Server.releaseLock(Object)");
		
		// try all possible pre charts of length 2
		for (EventType e : interestingEvents.getEventTypes()) {
			for (EventType e2 : interestingEvents.getEventTypes()) {
				if (e.equals(e2)) continue;

				EvaluationResults results = new DemoResults();
				SingleModelEvaluationResults modelEvaluationResults = results.newModel(model);
				confidenceProperty.setChart(Arrays.asList(e.getEvent(),e2.getEvent()));
				traceProvider = getTraceProvider();
				int effectsFull = computeConfidence(true,modelEvaluationResults, traceProvider);
				traceProvider = getTraceProvider();
				int effectsPartial = computeConfidence(false,modelEvaluationResults, traceProvider);

				EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
				exporter.setShowIntermediateConfidence(false);

				if ((modelEvaluationResults.getTrials().get(0).getConfidence() -
						modelEvaluationResults.getTrials().get(1).getConfidence() > confidenceDifferenceThreshold) &&
						(effectsFull != effectsPartial) && 
						modelEvaluationResults.getTrials().get(0).getConfidence() > 0.3 &&
						modelEvaluationResults.getTrials().get(1).getConfidence() >= 0.0) {
					System.out.println("Found example with prechart: [" + e.getEvent()+","+ e2.getEvent()+"]");
					System.out.println(effectsFull + " effects in full");
					System.out.println(effectsPartial + " effects in partial");
					System.out.println("Partial Trace:" + ((SynopticTraceProvider)traceProvider).getTraceOrder());
					exporter.printToScreen();
					//return true;
				}
			}
		}
		return false;
	}

	private int computeConfidence(boolean full,
			SingleModelEvaluationResults modelEvaluationResults, TraceProvider traceProvider)
					throws SpecMiningAlgorithmException, InvalidModelException {

		SingleTrialSingleModelEvaluationResults singleTrialResults = modelEvaluationResults.newTrial(properties);
		double logConfidence = 0.0D;
		Log log = new Log();
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, ab);
		while (traceProvider.hasNext()) {
			ExecutionTrace t = traceProvider.next();
			singleTrialResults.addEventsCount(t.size());
			log.addTrace(t);
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);
			logConfidence = estimator.estimate();
			if (!full && log.size() == partialLogLength) break;
		}

		// should do this through demoresults:
		int count = 0;
		AggregatedLogPropertyValues<?> aggVals = estimator.getPropEstimators().get(0).getAggregatedPropertyValues();
		for (Entry<EventTypeSeq, ?> e:aggVals.getAggLogPropVals().entrySet()) {
			if (e.getValue().equals(true)) {
			//	System.out.println(e.getKey() + " is true in log");
				++count;
			}
		}
		//System.out.println("full: " + full + ", "+count + " effects");
		singleTrialResults.setConfidence(logConfidence);
		singleTrialResults.setN(log.size());
		return count;
	}


	private TraceProvider getTraceProvider() throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams("columba"));
	}


	private SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("sam/" + model, "--", "(?<TYPE>.*)", null);
	}

	@Test
	public void runMultipleTimes() throws SpecMiningAlgorithmException, InvalidModelException {
		boolean result = false;
		partialLogLength = 10;
		for (int j = 0; j<7 ; ++j) {
			int times = 3;
			for (int i=0 ; i<times; ++i) {
				result = run();
				if (result) break;
			}
			if (result) break;
			partialLogLength = partialLogLength + 10;
		}
	}

}
