package app;

import utils.JenaEngine;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

public class Jena3 {
	String filepath;
	String namespace;
	Model model;

	Jena3(String filepath) {
		this.filepath = filepath;
		this.model = JenaEngine.readModel(filepath);
        this.namespace = model.getNsPrefixURI("");
	}
	
	public void readCoaches(){
		Model ourmodel = JenaEngine.readInferencedModelFromRuleFile(model,"data/jena3_rule.txt");
		Property rdfType = ourmodel.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type");
		Resource A = ourmodel.getResource(namespace+'A');
		ResIterator iter =ourmodel.listResourcesWithProperty(rdfType,A);
		for (; iter.hasNext();) {
			Resource i = iter.next();
			JenaEngine.readRsDataType(ourmodel, namespace, i, "person_name");
		}
	}

}
