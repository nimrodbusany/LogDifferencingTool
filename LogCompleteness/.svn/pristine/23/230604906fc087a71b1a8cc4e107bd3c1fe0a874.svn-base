package il.ac.tau.cs.smlab.fw;

import il.ac.tau.cs.smlab.fw.utils.KPermutations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.distribution.BinomialDistribution;

public class ExpectationCalculator {

	public static void main(String[] args) {
		int n = 8;

		for (int i = 1 ; i <= n ; i ++) {
	//		System.out.println(calc(i)-calc(i-1));
		}
		for (int i = 1 ; i <= n ; i ++) {
				System.out.println(diff(i+1));
		}
//		System.out.println(diff(n+1));
	}


	public static double calc(int n) {
		BinomialDistribution X = new BinomialDistribution(n,2D/3);
		double sum = 0;
		for (int k = 1 ; k <= n ; ++k) {
			sum = sum + (Math.pow(1 - ((double)k / n),n) * X.probability(k));
		}
		X = new BinomialDistribution(n,0.5);
		for (int k = 1 ; k <= n ; ++k) {
			sum = sum + (Math.pow(1 - ((double)k / n),n) * X.probability(k));
		}
		return 1- sum;
	}



	public static double diff(int n) {
		double e = 0;
		double total = 0;
		double c1,c2;
		for (int sco = 0 ; sco <= n ; sco++) {
			for (int srr = 0 ; srr <= n ; srr++) {
				double sum = 0;
				Collection<List<Integer>> tco = generateTrialsResults(sco,n);
				Collection<List<Integer>> trr = generateTrialsResults(srr,n);
				for (List<Integer> rco : tco) {
					for (List<Integer> rrr : trr) {
						c1 = 0;
						c2 = 0;
						if (sco > 0) {
							if (rco.get(rco.size() - 1) == 1) {
								if ((sco - 1) > 0) {
									c1 = singleProb(sco-1,n-1);
								}
							}
							else {
								c1 = singleProb(sco,n-1);
							}
						}
						if (srr > 0) {
							if (rrr.get(rrr.size() - 1) == 1) {
								if ((srr - 1) > 0) {
									c1 += singleProb(srr-1,n-1);
								}
							}
							else {
								c1 = singleProb(srr,n-1);
							}
						}
						if (sco > 0) {
							c2 = singleProb(sco,n);
						}
						if (srr > 0) {
							c2 += singleProb(srr,n);
						}

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
						if (c1 < c2) {
							e = e + (q*(c1-c2));
						}
						sum = sum + q;
	/*					System.out.println("P[sco n+1=" + sco + ", srr n+1=" + srr +
								",  sco n= "+ (rco.get(rco.size() - 1) == 1? sco - 1: sco) + ",  srr n= "+ (rrr.get(rrr.size() - 1) == 1? srr - 1: srr)
								+ "   p = "+q + "   conf_delta = " + (c1-c2) + " e = " + (q*(c1-c2)));*/
					}

				}
				total = total + sum;

			}
		}
	//	System.out.println("total: " + total);
		return e;
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
		return seqs;
	}

	public static double singleProb(int S, int n) {
		return Math.pow(1-((double)S/n), n);
	}


}
