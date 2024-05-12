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
	
	public void readPlayers(){
		Model ourmodel = JenaEngine.readInferencedModelFromRuleFile(model,"data/jena3_rule.txt");
		Property pname = model.getProperty(namespace + "person_name");
		ResIterator iter =ourmodel.listResourcesWithProperty(pname);
		for (; iter.hasNext();) {
			Resource i = iter.next();
			JenaEngine.readRsDataType(ourmodel, namespace, i, "person_name");
		}
	}

}
