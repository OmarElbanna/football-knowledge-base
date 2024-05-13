package matchfinder.model;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.*;


public class ChoiceModel {

    private final FootballOntology ontology;

    private Set<OWLNamedIndividual> included = new TreeSet<OWLNamedIndividual>();

    private Set<OWLNamedIndividual> excluded = new TreeSet<OWLNamedIndividual>();

    private List<ChoiceModelListener> listeners = new ArrayList<ChoiceModelListener>();

    public ChoiceModel(FootballOntology ontology) {
        this.ontology = ontology;
    }

    public Set<OWLNamedIndividual> getIncluded() {
        return new TreeSet<OWLNamedIndividual>(included);
    }

    public Set<OWLNamedIndividual> getExcluded() {
        return new TreeSet<OWLNamedIndividual>(excluded);
    }


    public void addIncluded(OWLNamedIndividual cls) {
        boolean changed = included.add(cls);
        if (changed) {
            fireModelChangedEvent();
        }
    }

    public void removeIncluded(OWLNamedIndividual cls) {
        if (included.remove(cls)) {
            fireModelChangedEvent();
        }
    }


    public void addExcluded(OWLNamedIndividual individual) {
        OWLReasoner reasoner = getReasoner();
        boolean changed = excluded.add(individual);
        changed |= included.remove(individual);
        if (changed) {
            fireModelChangedEvent();
        }
    }

    public void removeExcluded(OWLNamedIndividual cls) {
        if (excluded.remove(cls)) {
            fireModelChangedEvent();
        }
    }

    public void addChoiceModelListener(ChoiceModelListener lsnr) {
        listeners.add(lsnr);
    }

    public void removeChoiceModelListener(ChoiceModelListener lsnr) {
        listeners.remove(lsnr);
    }

    protected void fireModelChangedEvent() {
        for(ChoiceModelListener listener : new ArrayList<ChoiceModelListener>(listeners)) {
            listener.modelChanged(new ChoiceModelChangedEvent(this));
        }
    }

    private OWLReasoner getReasoner() {
        return ontology.getReasoner();
    }
}

