package app;


import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

public class Jena1 {
	String filepath;
	String namespace;
	OntModel ontmodel;

	Jena1(String filepath) {
		this.filepath = filepath;
        this.ontmodel = ModelFactory.createOntologyModel();
        FileManager fileManager = FileManager.get();
        fileManager.readModel(ontmodel, this.filepath);
        this.namespace = ontmodel.getNsPrefixURI("");
	}

	public void displayAllPersons() {
		OntClass cl = ontmodel.getOntClass(namespace + "Person");
		Property person_name = ontmodel.getProperty(namespace+"person_name");
		for (ExtendedIterator i = cl.listInstances(); i.hasNext();) {
			OntResource c = (OntResource) i.next();
			System.out.println("name: " + c.getProperty(person_name).getString());
		}
	}

}

