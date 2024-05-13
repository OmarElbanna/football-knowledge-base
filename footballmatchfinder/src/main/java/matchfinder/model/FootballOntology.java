package matchfinder.model;

import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AnnotationValueShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class FootballOntology {

    public static final Preferences PREFERENCES;

    private OpenlletReasoner reasoner;

    private OWLOntology ontology;

    private OWLDataFactory df;

    private OWLOntologyManager manager;

    private ShortFormProvider sfp;

    static {
        PREFERENCES = Preferences.getInstance();
    }

    public FootballOntology() {
        loadOntology();
        setupReasoner();
        setupShortFormProvider();
    }



    protected void loadOntology() {
        try {
            manager = OWLManager.createOWLOntologyManager();
            df = manager.getOWLDataFactory();
            ontology = manager.loadOntologyFromOntologyDocument(PREFERENCES.getOntologyDocumentFile());
            OpenlletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
            if (reasoner.isConsistent()){
                System.out.println("Consistent pellet initiated");
            }else {
                System.out.println("Non-consistent pellet owl");
                System.exit(1);
            }

        } catch (final Throwable e) {
            Runnable runnable = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null,
                            "Could not create the ontology.  (This probably happened\n" +
                                    "because the ontology could not be accessed.)\n" +
                                    "[" + e.getMessage() + "]",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println(JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            };
            SwingUtilities.invokeLater(runnable);
        }
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    protected void setupReasoner() {
        // run reasoner
        try {
            reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "A reasoner error has ocurred.\n" +
                            "[" + e.getMessage() + "]",
                    "Reasoner Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupShortFormProvider() {
        final Map<OWLAnnotationProperty, List<String>> langMap = new HashMap<OWLAnnotationProperty, List<String>>();
        langMap.put(df.getRDFSLabel(), Arrays.asList(PREFERENCES.getLanguage()));
        sfp = new AnnotationValueShortFormProvider(Arrays.asList(df.getRDFSLabel()),
                langMap, manager);
    }


    public OWLReasoner getReasoner() {
        return reasoner;
    }


    public Collection<OWLClass> getOntologyMainClasses() {
        Set<OWLClass> parentClasses = new HashSet<OWLClass>();
        parentClasses.add(df.getOWLClass(PREFERENCES.getTeamClassName()));
        parentClasses.add(df.getOWLClass(PREFERENCES.getRefereeClassName()));

        OWLClass rootCls = df.getOWLClass(PREFERENCES.getRootClassName());
        Set<OWLClass> classes = reasoner.getSubClasses(rootCls, true).getFlattened();
        classes.remove(df.getOWLClass(PREFERENCES.getMatchClassName()));
        return parentClasses;
    }

    public String render(OWLEntity entity) {
        String shortForm = sfp.getShortForm(entity);
        StringBuilder sb = new StringBuilder();
        char last = 0;
        for(int i = 0; i < shortForm.length(); i++) {
            char ch = shortForm.charAt(i);
            if(Character.isUpperCase(ch) && last != 0 && last != ' ') {
               sb.append(" ");
            }
            if(ch=='_'){
                ch = ' ';
            }
            sb.append(ch);
            last = ch;
        }
        return sb.toString();
    }


    public HashSet<OWLNamedIndividual> getMatches(Set<OWLNamedIndividual> includeItems, Set<OWLNamedIndividual> excludeItems){
        // get matches for the given include and exclude teams
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLClass matchClass = factory.getOWLClass(PREFERENCES.getMatchClassName());
        OWLClass teamClass = factory.getOWLClass(PREFERENCES.getTeamClassName());
        OWLClass refereeClass = factory.getOWLClass(PREFERENCES.getRefereeClassName());
        Set<OWLNamedIndividual> matches = reasoner.getInstances(matchClass, false).getFlattened();
        HashSet<OWLNamedIndividual> validMatches = new HashSet<>();
        
        for (OWLNamedIndividual match : matches) {
            boolean valid = false;
            for (OWLNamedIndividual includeItem : includeItems) {
                if (reasoner.getTypes(includeItem).containsEntity(teamClass)){
                    if (reasoner.getObjectPropertyValues(match, factory.getOWLObjectProperty(PREFERENCES.getBetweenTeamsPropertyName())).getFlattened().contains(includeItem)) {
                        valid = true;
                        break;
                    }
                }
                else if (reasoner.getTypes(includeItem).containsEntity(refereeClass)){
                    if (reasoner.getObjectPropertyValues(match, factory.getOWLObjectProperty(PREFERENCES.getHasRefereePropertyName())).getFlattened().contains(includeItem)) {
                        valid = true;
                        break;
                    }
                }
            }
            for (OWLNamedIndividual excludeItem : excludeItems) {
                // check item class
                if(reasoner.getTypes(excludeItem).containsEntity(teamClass)){
                    if (reasoner.getObjectPropertyValues(match, factory.getOWLObjectProperty(PREFERENCES.getBetweenTeamsPropertyName())).getFlattened().contains(excludeItem)) {
                        valid = false;
                        break;
                    }
                }
                else if (reasoner.getTypes(excludeItem).containsEntity(refereeClass)){
                    if (reasoner.getObjectPropertyValues(match, factory.getOWLObjectProperty(PREFERENCES.getHasRefereePropertyName())).getFlattened().contains(excludeItem)) {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid) {
                validMatches.add(match);
            }
        }
        System.out.println("Valid: " + validMatches);
        return validMatches;
    }




    public HashMap<String, String> getMatchDetails(OWLNamedIndividual match) {
        HashMap<String, String> details = new HashMap<>();
        // get the details of the match
        OWLDataFactory factory = manager.getOWLDataFactory();

        OWLDataProperty dateProperty = factory.getOWLDataProperty(PREFERENCES.getMatchDatePropertyName());
        Set<OWLLiteral> matchDate = reasoner.getDataPropertyValues(match, dateProperty);
        matchDate.forEach(date -> details.put("Date", date.getLiteral()));

        OWLDataProperty locationProperty = factory.getOWLDataProperty(PREFERENCES.getMatchLocationPropertyName());
        Set<OWLLiteral> matchLocation = reasoner.getDataPropertyValues(match, locationProperty);
        matchLocation.forEach(location -> details.put("Location", location.getLiteral()));

        OWLDataProperty resultProperty = factory.getOWLDataProperty(PREFERENCES.getMatchResultPropertyName());
        Set<OWLLiteral> matchResult = reasoner.getDataPropertyValues(match, resultProperty);
        matchResult.forEach(result -> details.put("Result", result.getLiteral()));

        OWLObjectProperty betweenTeamsProperty = factory.getOWLObjectProperty(PREFERENCES.getBetweenTeamsPropertyName());
        Set<OWLNamedIndividual> teams = reasoner.getObjectPropertyValues(match, betweenTeamsProperty).getFlattened();
        String teamNames = teams.stream().map(team -> render(team)).collect(Collectors.joining(" x "));
        details.put("Between", teamNames);

        OWLObjectProperty hasRefereeProperty = factory.getOWLObjectProperty(PREFERENCES.getHasRefereePropertyName());
        Set<OWLNamedIndividual> referees = reasoner.getObjectPropertyValues(match, hasRefereeProperty).getFlattened();
        String refereeNames = referees.stream().map(referee -> render(referee)).collect(Collectors.joining(" "));
        details.put("Referee", refereeNames);

        return details;
    }
}

