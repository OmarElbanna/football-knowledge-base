package matchfinder.ui;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import matchfinder.model.ChoiceModel;
import matchfinder.model.FootballOntology;
import matchfinder.selection.Selectable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Set;


public class IncludeItemListPanel extends ItemListPanel {

	private ChoiceModel choiceModel;

	private Selectable selectable;

	private Action addAction = new AbstractAction("Add") {
			public void actionPerformed(ActionEvent e) {
				Object selObj = selectable.getSelection();
				Set<OWLNamedIndividual> excluded = choiceModel.getExcluded();
				if(selObj != null && selObj instanceof OWLNamedIndividual && !excluded.contains(selObj)){
					choiceModel.addIncluded((OWLNamedIndividual) selObj);
				}
				if(excluded.contains(selObj)){
					JOptionPane.showMessageDialog(null,
							"Cannot include item from the Excluded List!",
							"Action Restricted",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

	private Action removeAction = new AbstractAction("Remove") {
			public void actionPerformed(ActionEvent e) {
				Object selObj = getSelection();
				if(selObj != null && selObj instanceof OWLNamedIndividual) {
					choiceModel.removeIncluded((OWLNamedIndividual) selObj);
				}
			}
		};

	public IncludeItemListPanel(final FootballOntology ontology,
								final Selectable selectable,
								final ChoiceModel choiceModel) {
		super(ontology, "Included Items:", selectable, choiceModel);
		this.choiceModel = choiceModel;
		this.selectable = selectable;
		createUI();
	}


	protected Collection getListItems() {
		return choiceModel.getIncluded();
	}


	protected Action getAddAction() {
		return addAction;
	}


	protected Action getRemoveAction() {
		return removeAction;
	}
}

