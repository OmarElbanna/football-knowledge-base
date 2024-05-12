package app;

import utils.JenaEngine;

import org.apache.jena.rdf.model.Model;

public class Jena5 {
	String filepath;
	String namespace;
	Model model;

	Jena5(String filepath) {
		this.filepath = filepath;
		this.model = JenaEngine.readModel(filepath);
        this.namespace = model.getNsPrefixURI("");
	}
	
	public void readPlayerCoach(){
		this.model = JenaEngine.readInferencedModelFromRuleFile(model, "data/jena5_rule.txt");
		
		String query = "PREFIX ns: <http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?person  "
				+ "WHERE{"
				+ "?person rdf:type ns:PlayerCoach. " + "}";
		System.out.println(JenaEngine.executeQuery(model, query));
	}

}
