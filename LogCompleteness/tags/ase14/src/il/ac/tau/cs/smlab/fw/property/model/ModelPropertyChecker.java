package il.ac.tau.cs.smlab.fw.property.model;

public interface ModelPropertyChecker<M,D> {
	
	ModelPropertyValues<D> check(ModelProperty<M,D> p, M model);
	
}
