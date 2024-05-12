package app;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import utils.FileTool;

public class Jena2 {
	String filepath;
	String namespace;
	OntModel ontmodel;

	Jena2(String filepath) {
		this.filepath = filepath;
        this.ontmodel = ModelFactory.createOntologyModel();
        FileManager fileManager = FileManager.get();
        fileManager.readModel(ontmodel, this.filepath);
        this.namespace = ontmodel.getNsPrefixURI("");
	}
	
	public String getQuery(String file){
		File queryFile = new File(file);
		InputStream in = FileManager.get().open(file);
		String queryString = FileTool.getContents(queryFile);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryString;
	}
	
	public void displayAllPerson(String file){
		String str_query = getQuery(file);
		Query query = QueryFactory.create(str_query);
		QueryExecution qe = QueryExecutionFactory.create(query, ontmodel);
		ResultSet results = qe.execSelect();
		OutputStream output = new OutputStream() {
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			public String toString() {
				return this.string.toString();
			}
		};
		ResultSetFormatter.out(output, results, query);
		System.out.print(output.toString());
	}

}

