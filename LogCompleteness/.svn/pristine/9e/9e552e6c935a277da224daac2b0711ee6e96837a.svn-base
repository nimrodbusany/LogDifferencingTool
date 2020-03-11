package il.ac.tau.cs.smlab.fw;

import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.utils.KPermutations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.junit.Test;

public class MonotonicityCalculator {
	
	
	
	public static void main(String[] args) {
		int n = 8;
		for (int i = 1 ; i <= n ; i ++) {
			calc(i);
		}
	}
	
	public static double calc(int n) {
		// calculate for n=3
		double dropProb = 0;
		double incrProb = 0;
		double eqProb = 0;
		double t1,t2;// n=3
		double expected = 0; 
		double expectedDrop = 0;
		double expectedInc = 0;
		
		double test = 0;
		for (int sco = 0 ; sco <= n ; sco++) {
			for (int srr = 0 ; srr <= n ; srr++) {
				for (int x1 = 0 ; x1 <= 1 ; x1++) {
					for (int x2 = 0 ; x2 <= 1 ; x2++) {
						t1 = 0;
						t2 = 0;
						
						if (sco != 0) {
							t1 += singleProb(sco,n);
						}
						if (srr != 0) {
							t1 += singleProb(srr, n);
						}
						if (sco + x1 != 0) {
							t2 += singleProb(sco + x1, n+1);
						}
						if (srr + x2 != 0) {
							t2 += singleProb(srr + x2, n+1);
						}
						double p = calculateProb(sco, srr, x1, x2, n);
				//		System.out.println(sco + " " + srr + " " + x1 + " " + x2 + " " + p);
						test += p;
						if (t1 < t2) {
							dropProb += p; // OR p
							expectedDrop += (p * (t2 - t1));
				//			System.out.println("prob. is " + p);
					//		System.out.println("confidence drops when Sc,r = " + sco + " , Sr,r = " + srr + " , x1 = " + x1 + " , x2 = " + x2);
						}
						else if (t1 > t2) {
							incrProb += p; // OR p
							expectedInc += p * (t1 - t2);
		
						//	System.out.println("prob. is " + p);
					//		System.out.println("t1 = " + t1 + ", t2 = " + t2);
					//		System.out.println("confidence increases when n = " + n + ", Sc,r = " + sco + " , Sr,r = " + srr + " , x1 = " + x1 + " , x2 = " + x2);
						}
						else {
							eqProb += p;
						}
						expected += p * (1 - t1);
						
					}
				}
			}
		}
//		System.out.println(test);
//		System.out.println("drop prob.: " + dropProb);
//		System.out.println("increase prob.: " + incrProb);
//		System.out.println("equality prob.: " + eqProb);
//		System.out.println("E[conf] = " + expected);
		System.out.println("expected drop: " + expectedDrop);
//		System.out.println("expected increase: " + expectedInc);
//		System.out.println("expected change: " + ( expectedInc - expectedDrop));
		return expected;
	}
	
	
	private static double calculateProb(int sco, int srr, int x1, int x2, int n) {
		double p = 1;
		// P(X=sco && Y=srr) = P(Y=srr|X=sco) * P(X=sco)
		BinomialDistribution X;
		// P(X = sco), X~Bin(n,1/2)
	//	X = new BinomialDistribution(n,0.5);
	//	p *= X.probability(sco);
		
		double sum = 0;  // P(Y=srr|X=sco)
		Collection<List<Integer>> tco = generateTrialsResults(sco,n);
		Collection<List<Integer>> trr = generateTrialsResults(srr,n);
		for (List<Integer> rco : tco) {
			for (List<Integer> rrr : trr) {
				double q = 1;
				for (int i = 0 ; i < n ; i++) {
					if (rco.get(i) == 1) {
						if (rrr.get(i) == 1) {
						q *= 5D/6;
						}
						else {
							q *= 1D/6;
						}
					}
					else { // rco.get(i) == 0
						q *= 0.5;
					}
					q *= 0.5;
				}
		//		q *= Math.pow(0.5, n);
				sum += q;
			}
		}
	//	System.out.println(sum + " with sco " + sco + " assuming srr " + srr);
		p *= sum;
	
		
		// P(X = x1), X~Bin(1,1/2)
		X = new BinomialDistribution(1,0.5);
		p *= X.probability(x1);

		// P(X = x2|Y=x1)
		if (x1 == 0) {
			X = new BinomialDistribution(1,0.5);
			p *= X.probability(x2);
		}
		else {
			X = new BinomialDistribution(1,5D/12);
			p *= X.probability(x2);
		}
		return p;
	}


	public static double singleProb(int S, int n) {
		return Math.pow(1-((double)S/n), n);
	}
	
	public static Collection<List<Integer>> generateTrialsResults(int success, int n) {
		List<Integer> srrList = new ArrayList<Integer>(n);
		for (int i = 0 ; i < success ; i++) {
			srrList.add(1);
		}
		for (int i = 0 ; i < n-success ; i++) {
			srrList.add(0);
		}
		 Collection<List<Integer>> seqs = new KPermutations<Integer>().permutations(srrList, srrList.size());
//		 System.out.println(seqs);
		 return seqs;
	}
	
	@Test
	public void test() {
		System.out.println(calculateProb(0, 0, 0, 0, 2));
	}

}
