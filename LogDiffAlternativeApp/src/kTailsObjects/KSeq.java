package kTailsObjects;

import java.util.ArrayList;

public class KSeq {

	ArrayList<String> k_seq = new ArrayList<String>();

	public KSeq(ArrayList<String> seq) {
		this.k_seq = seq;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != KSeq.class) {
			return false;
		}
		KSeq other = (KSeq) obj;
		return this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public String toString() {
		String seq = "<";
		for (String e : k_seq) {
			seq += e + ",";
		}
		seq = seq.substring(0,seq.length()-1) + ">";
		return seq;
	}

	public ArrayList<String> getKSeqString() {
		return k_seq;
	}

}
