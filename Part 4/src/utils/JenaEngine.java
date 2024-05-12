/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

import java.io.IOException;
import java.io.OutputStream;


public class JenaEngine {
	static public String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	static public Model readModel(String inputDataFile) {

		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open(inputDataFile);
		if (in == null) {
			System.out
					.println("Ontology file: " + inputDataFile + " not found");
			return null;
		}

		model.read(in, "");
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return model;
	}


	static public Model readInferencedModelFromRuleFile(Model model,
			String inputRuleFile) {
		InputStream in = FileManager.get().open(inputRuleFile);
		if (in == null) {
			System.out.println("Rule File: " + inputRuleFile + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}

		List rules = Rule.rulesFromURL(inputRuleFile);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setDerivationLogging(true);
		reasoner.setOWLTranslation(true); // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		return inf;
	}

	static public String executeQuery(Model model, String queryString) {
		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);
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
		return output.toString();
	}


	static public String executeQueryFile(Model model, String filepath) {
		File queryFile = new File(filepath);
		InputStream in = FileManager.get().open(filepath);
		if (in == null) {
			System.out.println("Query file: " + filepath + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		String queryString = FileTool.getContents(queryFile);
		return executeQuery(model, queryString);
	}


	static public String executeQueryFileWithParameter(Model model,
			String filepath, String parameter) {
		File queryFile = new File(filepath);

		InputStream in = FileManager.get().open(filepath);
		if (in == null) {
			System.out.println("Query file: " + filepath + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		String queryString = FileTool.getContents(queryFile);
		queryString = queryString.replace("%PARAMETER%", parameter);
		return executeQuery(model, queryString);
	}


	static public boolean createInstanceOfClass(Model model, String namespace,
			String className, String instanceName) {
		Resource rs = model.getResource(namespace + instanceName);
		if (rs == null)
			rs = model.createResource(namespace + instanceName);
		Property p = model.getProperty(RDF + "type");
		Resource rs2 = model.getResource(namespace + className);
		if ((rs2 != null) && (rs != null) && (p != null)) {
			// add new value
			rs.addProperty(p, rs2);
			return true;
		}
		return false;
	}


	static public boolean updateValueOfObjectProperty(Model model,
			String namespace, String object1Name, String propertyName,
			String object2Name) {
		Resource rs1 = model.getResource(namespace + object1Name);
		Resource rs2 = model.getResource(namespace + object2Name);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs1 != null) && (rs2 != null) && (p != null)) {
			rs1.removeAll(p);
			rs1.addProperty(p, rs2);
			return true;
		}
		return false;
	}


	static public boolean addValueOfObjectProperty(Model model,
			String namespace, String instance1Name, String propertyName,
			String instance2Name) {
		Resource rs1 = model.getResource(namespace + instance1Name);
		Resource rs2 = model.getResource(namespace + instance2Name);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs1 != null) && (rs2 != null) && (p != null)) {
			rs1.addProperty(p, rs2);
			return true;
		}
		return false;
	}

	static public boolean updateValueOfDataTypeProperty(Model model,
			String namespace, String instanceName, String propertyName,
			Object value) {
		Resource rs = model.getResource(namespace + instanceName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			rs.removeAll(p);
			rs.addLiteral(p, value);
			return true;
		}
		return false;
	}

	static public boolean addValueOfDataTypeProperty(Model model,
			String namespace, String instanceName, String propertyName,
			Object value) {
		Resource rs = model.getResource(namespace + instanceName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			rs.addLiteral(p, value);
			return true;
		}
		return false;
	}

	static public boolean removeAllValuesOfProperty(Model model,
			String namespace, String objectName, String propertyName) {
		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			
			rs.removeAll(p);

			return true;
		}
		return false;
	}
	
	
	static public OntModel readOntmodel(String inputfile) {
		OntModel model = ModelFactory.createOntologyModel();

		InputStream in = FileManager.get().open(inputfile);
		if (in == null) {
			System.out
					.println("Ontology file: " + inputfile + " not found");
			return null;
		}
		model.read(in, "");
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return model;
	}
	
	static public void readObjectType(Model model, String namespace,
			String objectName, String propertyName) {

		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			StmtIterator it = rs.listProperties(p);
			if (!it.hasNext()) {
				System.out.println(objectName + " " + propertyName + ": "
						+ null);
			} else {
				System.out.println(objectName + " " + propertyName + ": ");
				while (it.hasNext()) {

					Statement s = it.next();
					Resource re = s.getResource();
					readRsDataType(model, namespace, re, "team_name");
					readRsDataType(model,namespace,re,"person_name");
				}
			}
		}
	}

	static public void readDataType(Model model, String namespace,
			String objectName, String propertyName) {
		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			if (rs.getProperty(p) != null) {
				System.out.println(propertyName + " of " + objectName + " is: "
						+ rs.getProperty(p).getString());
			} else {
				System.out.println(propertyName + " of " + objectName + " is: "
						+ null);
			}
		}
	}

	static public void readRsDataType(Model model, String namespace,
			Resource rs, String propertyName) {
		Property p = model.getProperty(namespace + propertyName);
		if (p != null) {
			if (rs.getProperty(p) != null) {
				System.out.println(propertyName + ": "
						+ rs.getProperty(p).getString());
			}
		}
	}

}
