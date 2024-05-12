package app;

import java.util.Scanner;
import utils.JenaEngine;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Jena4 {
	String filepath;
	String namespace;
	Model model;

	Jena4(String filepath) {
		this.filepath = filepath;
		this.model = JenaEngine.readModel(filepath);
        this.namespace = model.getNsPrefixURI("");
	}

	public String getInput() {
		System.out.println("Enter a name of a match: ");
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}

	public void searchMatch(String title) {
		if (hasMatch(title)) {
			Resource rs = model.getResource(namespace + title);
			System.out.println(rs.getLocalName());
			JenaEngine.readRsDataType(model, namespace, rs, "match_result");
			JenaEngine.readRsDataType(model, namespace, rs, "match_location");
			JenaEngine.readObjectType(model, namespace, title, "between_teams");
			JenaEngine.readObjectType(model, namespace, title, "has_referee");
		} else {
			System.out.println("Error: Wrong title!");
		}
	}

	public boolean hasMatch(String title) {
		Resource rs = model.getResource(namespace + title);
		Property ptitle = model.getProperty(namespace + "match_title");
		if (rs != null && ptitle != null) {
			if(rs.getProperty(ptitle) != null )
				return true;
			else
				return false;
		}
		return false;
	}
}
