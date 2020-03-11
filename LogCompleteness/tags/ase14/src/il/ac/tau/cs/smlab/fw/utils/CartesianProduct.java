package il.ac.tau.cs.smlab.fw.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartesianProduct<T> {

	private List<T> list1 = Collections.emptyList();
	private List<T> list2 = Collections.emptyList();

	
	public CartesianProduct(List<T> list1, List<T> list2) {
		this.list1 = list1;
		this.list2 = list2;
	}
	
	public List<OrderedPair<T>> getOrderedPairs() {
		List<OrderedPair<T>> pairs = new ArrayList<OrderedPair<T>>(list1.size()*list2.size());
		
		for (T element1: list1) {
			for (T element2: list2) {
				pairs.add(new OrderedPair<T>(element1, element2));
			}
		}
		
		return pairs;
	}
}
