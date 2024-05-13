package matchfinder.model;

import org.semanticweb.owlapi.model.IRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Preferences {

    public static final String ONTOLOGY_FILE_LOCATION_ELEMENT_NAME = "OntologyFileLocation";

    public static final String TEAM_CLASS_ELEMENT_NAME = "TeamClass";

    public static final String MATCH_CLASS_ELEMENT_NAME = "MatchClass";

    public static final String REFEREE_CLASS_ELEMENT_NAME = "RefereeClass";

    public static final String MATCH_DATE_ELEMENT_NAME = "MatchDate";

    public static final String MATCH_LOCATION_ELEMENT_NAME = "MatchLocation";

    public static final String MATCH_RESULT_ELEMENT_NAME = "MatchResult";

    public static final String BETWEEN_TEAMS_PROPERTY_ELEMENT_NAME = "BetweenTeamsProperty";

    public static final String HAS_REFEREE_PROPERTY_ELEMENT_NAME = "HasRefereeProperty";

    public static final IRI DEFAULT_ROOT_CLASS_IRI = IRI.create("http://www.w3.org/2002/07/owl#Thing");

    public static final IRI DEFAULT_TEAM_CLASS_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/Team");

    public static final IRI DEFAULT_MATCH_CLASS_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/Match");

    public static final IRI DEFAULT_REFEREE_CLASS_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/Referee");

    public static final IRI DEFAULT_MATCH_DATE_PROPERTY_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/match_date");

    public static final IRI DEFAULT_MATCH_LOCATION_PROPERTY_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/match_location");

    public static final IRI DEFAULT_MATCH_RESULT_PROPERTY_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/match_result");

    public static final IRI DEFAULT_BETWEEN_TEAMS_PROPERTY_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/between_teams");

    public static final IRI DEFAULT_HAS_REFEREE_PROPERTY_IRI = IRI.create("http://www.semanticweb.org/omarelbanna/ontologies/2024/3/football/has_referee");

    public static final String DEFAULT_ONTOLOGY_FILE_LOCATION = "/Volumes/D/Uni/Semester 10/CSE488 Ontologies & Web Semantics/footballmatchfinder/src/main/resources/football.rdf";

    public static final String DEFAULT_LANGUAGE = "en";

    private static Preferences instance;


    private String ontologyFileLocation = DEFAULT_ONTOLOGY_FILE_LOCATION;

    private IRI rootClassName = DEFAULT_ROOT_CLASS_IRI;

    private IRI teamClassName = DEFAULT_TEAM_CLASS_IRI;


    private IRI matchClassName = DEFAULT_MATCH_CLASS_IRI;

    private IRI refereeClassName = DEFAULT_REFEREE_CLASS_IRI;

    private IRI matchDatePropertyName = DEFAULT_MATCH_DATE_PROPERTY_IRI;

    private IRI matchLocationPropertyName = DEFAULT_MATCH_LOCATION_PROPERTY_IRI;

    private IRI matchResultPropertyName = DEFAULT_MATCH_RESULT_PROPERTY_IRI;

    private IRI betweenTeamsPropertyName = DEFAULT_BETWEEN_TEAMS_PROPERTY_IRI;


    private IRI hasRefereePropertyName = DEFAULT_HAS_REFEREE_PROPERTY_IRI;


    private String language = DEFAULT_LANGUAGE;




    public static final String FILE_NAME = "config.xml";

    private Preferences(String fileName) {
        loadPreferences(fileName);
    }

    public static synchronized Preferences getInstance() {
        if(instance == null) {
            instance = new Preferences(FILE_NAME);
        }
        return instance;
    }

    private void loadPreferences(String fileName) {
        Document doc = getDocument(fileName);
        loadOntologyFileLocation(doc);
        loadTeamClassName(doc);
        loadMatchClassName(doc);
        loadRefereeClassName(doc);
        loadMatchDatePropertyName(doc);
        loadMatchLocationPropertyName(doc);
        loadMatchResultPropertyName(doc);
        loadBetweenTeamsPropertyName(doc);
        loadHasRefereePropertyName(doc);
    }


    private void loadOntologyFileLocation(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(ONTOLOGY_FILE_LOCATION_ELEMENT_NAME).item(0);
        if (element != null) {
            ontologyFileLocation = element.getAttribute("path");
        }
    }

    private void loadTeamClassName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(TEAM_CLASS_ELEMENT_NAME).item(0);
        if (element != null) {
            teamClassName = getIRI(element);
        }
    }

    private IRI getIRI(Element element) {
        return IRI.create(element.getAttribute("name"));
    }




    private void loadMatchClassName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(MATCH_CLASS_ELEMENT_NAME).item(0);
        if (element != null) {
            matchClassName = getIRI(element);
        }
    }



    private void loadRefereeClassName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(REFEREE_CLASS_ELEMENT_NAME).item(0);
        if (element != null) {
            refereeClassName = getIRI(element);
        }
    }







    private void loadMatchDatePropertyName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(MATCH_DATE_ELEMENT_NAME).item(0);
        if (element != null) {
            matchDatePropertyName = getIRI(element);
        }
    }

    private void loadMatchLocationPropertyName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(MATCH_LOCATION_ELEMENT_NAME).item(0);
        if (element != null) {
            matchLocationPropertyName = getIRI(element);
        }
    }

    private void loadMatchResultPropertyName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(MATCH_RESULT_ELEMENT_NAME).item(0);
        if (element != null) {
            matchResultPropertyName = getIRI(element);
        }
    }





    private void loadBetweenTeamsPropertyName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(BETWEEN_TEAMS_PROPERTY_ELEMENT_NAME).item(0);
        if (element != null) {
            betweenTeamsPropertyName = getIRI(element);
        }
    }

    private void loadHasRefereePropertyName(Document doc) {
        Element element = (Element)doc.getDocumentElement().getElementsByTagName(HAS_REFEREE_PROPERTY_ELEMENT_NAME).item(0);
        if (element != null) {
            hasRefereePropertyName = getIRI(element);
        }
    }


    private Document getDocument(String fileName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            File file = new File(fileName);
            if(file.exists()) {
                return documentBuilder.parse(file);
            }
            else {
                InputStream is = getClass().getResourceAsStream("/" + fileName);
                return documentBuilder.parse(is);
            }
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
        catch(SAXException saxEx) {
            saxEx.printStackTrace();
        }
        catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getOntologyDocumentFile() {
        return new File(ontologyFileLocation);
    }

    public IRI getRootClassName() {
        return  rootClassName;
    }

    public IRI getTeamClassName() {
        return teamClassName;
    }

    public IRI getMatchClassName() {
        return matchClassName;
    }

    public IRI getRefereeClassName() {
        return refereeClassName;
    }

    public IRI getMatchDatePropertyName() {
        return matchDatePropertyName;
    }

    public IRI getMatchLocationPropertyName() {
        return matchLocationPropertyName;
    }

    public IRI getMatchResultPropertyName() {
        return matchResultPropertyName;
    }

    public IRI getBetweenTeamsPropertyName() {
        return betweenTeamsPropertyName;
    }


    public IRI getHasRefereePropertyName() {
        return hasRefereePropertyName;
    }


    public String getLanguage() {
        return language;
    }
}

