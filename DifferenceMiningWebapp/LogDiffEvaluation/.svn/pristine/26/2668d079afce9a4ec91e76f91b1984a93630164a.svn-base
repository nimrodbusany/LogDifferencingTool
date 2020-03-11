package il.ac.tau.cs.smlab.diff.evaluation;

import java.util.Hashtable;

import com.alexmerz.graphviz.objects.Node;

public class Vertex
{
	Node node;
	public Vertex(Node node)
	{
		this.node = node;
	}
	
	public boolean equals(Object obj) 
	{
		Vertex vertex = (Vertex) obj;
		Hashtable<String, String> first = this.node.getAttributes();
		Hashtable<String, String> second =  vertex.node.getAttributes();
		
		if (first.equals(second))
		{
			//System.out.println("They are equals " + first.toString() + " " + second.toString());
			
			return true;
		}
		
		//System.out.println("They are not equals " + first.toString() + " " + second.toString());
		
		return false;
	}
}
