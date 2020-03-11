package il.ac.tau.cs.smlab.fw.utils;

public class OrderedPair<T> {
	T element1;
	T element2;
	
	OrderedPair(T element1, T element2) {
		this.element1 = element1;
		this.element2 = element2;
	}

	public T getElement1() {
		return element1;
	}

	public void setElement1(T element1) {
		this.element1 = element1;
	}

	public T getElement2() {
		return element2;
	}

	public void setElement2(T element2) {
		this.element2 = element2;
	}
	
}
