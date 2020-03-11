package il.ac.tau.cs.smlab.diff.evaluation;

import java.util.Hashtable;

import com.alexmerz.graphviz.objects.Edge;


public class EdgeImp
{
	Edge edge;
	
	public EdgeImp(Edge edge)
	{
		this.edge = edge;
	}
	
	public boolean equals(Object obj) 
	{
		EdgeImp edgeObj = (EdgeImp) obj;
		
		Hashtable<String, String> source = this.edge.getSource().getNode().getAttributes();
		Hashtable<String, String> destination = this.edge.getTarget().getNode().getAttributes();
		
		Hashtable<String, String> sourceEdge = edgeObj.edge.getSource().getNode().getAttributes();
		Hashtable<String, String> destinationEdge = edgeObj.edge.getTarget().getNode().getAttributes();

		return source.equals(sourceEdge) && destination.equals(destinationEdge);
		
	}
}
