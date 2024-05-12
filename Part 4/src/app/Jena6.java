package app;

import org.apache.jena.rdf.model.Model;

import utils.JenaEngine;

public class Jena6 {
	
	String filepath;
	String namespace;
	Model model;

	Jena6(String filepath) {
		this.filepath = filepath;
		this.model = JenaEngine.readModel(filepath);
		this.model = JenaEngine.readInferencedModelFromRuleFile(model,"data/jena6_rules.txt");
        this.namespace = model.getNsPrefixURI("");
	}
	
	public void readPersonAge() {
		String query = "PREFIX ns: <http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?name ?age "
				+ "WHERE{"
				+ "?person rdf:type ns:PersonAge. ?person ns:person_name ?name. ?person ns:person_age ?age." + "}";
		System.out.println(JenaEngine.executeQuery(model, query));
	}

	public void readCoachMale() {
		String query = "PREFIX ns: <http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?name ?gender "
				+ "WHERE{"
				+ "?person rdf:type ns:CoachMale. ?person ns:person_name ?name. ?person ns:person_gender ?gender."
				+ "}";
		System.out.println(JenaEngine.executeQuery(model, query));
	}

	public void readBelgianPlayer() {
		String query = "PREFIX ns: <http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?name ?age ?nationality "
				+ "WHERE{"
				+ "?player rdf:type ns:BelgianPlayer. ?player ns:person_name ?name.  ?player ns:person_age ?age.  ?player ns:person_nationality ?nationality ."
				+ "}";
		System.out.println(JenaEngine.executeQuery(model, query));
	}

}
