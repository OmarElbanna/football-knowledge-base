package matchfinder.ui;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import matchfinder.model.ChoiceModel;
import matchfinder.model.FootballOntology;
import matchfinder.selection.Selectable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Set;

public class ExcludeItemListPanel extends ItemListPanel {

    public static final String TITLE = "Excluded:";

    private ChoiceModel choiceModel;

    private Selectable selectable;

    private Action addAction = new AbstractAction("Add") {
        public void actionPerformed(ActionEvent e) {
            Object selObj = selectable.getSelection();
            Set<OWLNamedIndividual> included = choiceModel.getIncluded();
            if (selObj != null && selObj instanceof OWLNamedIndividual&& !included.contains(selObj)) {
                choiceModel.addExcluded((OWLNamedIndividual) selObj);
            }
            if(included.contains(selObj)){
                JOptionPane.showMessageDialog(null,
                        "Cannot exclude item from the Included List!",
                        "Action Restricted",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    private Action removeAction = new AbstractAction("Remove") {
        public void actionPerformed(ActionEvent e) {
            removeExcluded();
        }
    };

    public ExcludeItemListPanel(final FootballOntology ontology,
                                final Selectable selectable,
                                final ChoiceModel choiceModel) {
        super(ontology, TITLE, selectable, choiceModel);
        this.selectable = selectable;
        this.choiceModel = choiceModel;
        createUI();
    }

    protected Collection getListItems() {
        return choiceModel.getExcluded();
    }

    private void removeExcluded() {
        Object selObj = getSelection();
        if (selObj != null && selObj instanceof OWLNamedIndividual) {
            choiceModel.removeExcluded((OWLNamedIndividual) selObj);
        }
    }

    protected Action getAddAction() {
        return addAction;
    }

    protected Action getRemoveAction() {
        return removeAction;
    }
}

