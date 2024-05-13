package matchfinder.ui;


import org.semanticweb.owlapi.model.OWLNamedIndividual;
import matchfinder.model.FootballOntology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Iterator;


public class ResultsPanel extends JPanel {

	private FootballOntology ontology;

	private FootballApplication application;

	private Box box;


	public ResultsPanel(FootballOntology ontology, FootballApplication application) {
		this.ontology = ontology;
		this.application = application;
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout(7, 7));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		Action backAction = new AbstractAction("Back") {
			public void actionPerformed(ActionEvent e) {
				application.showMatchPanel();
			}
		};
		buttonPanel.add(new JButton(backAction));
		add(buttonPanel, BorderLayout.SOUTH);
		box = new Box(BoxLayout.Y_AXIS);
		box.setBackground(Color.WHITE);
		add(new JScrollPane(box));
	}

	public void setMatches(HashSet<OWLNamedIndividual> matches) {
		setMatchPanels(createMatchPanels(matches));
	}


	private MatchPanel[] createMatchPanels(HashSet<OWLNamedIndividual> matches) {
		if(matches.isEmpty()) {
			MatchPanel[] panels = new MatchPanel[1];
			panels[0] = new MatchPanel(ontology, null);
			return panels;
		}
		MatchPanel[] panels = new MatchPanel[matches.size()];
		int counter = 0;
		for(Iterator<OWLNamedIndividual> it = matches.iterator(); it.hasNext(); counter++) {
			OWLNamedIndividual match = it.next();
			panels[counter] = new MatchPanel(ontology, match);
		}
		return panels;
	}

	private void setMatchPanels(MatchPanel[] panels) {
		box.removeAll();
		for(int i = 0; i < panels.length; i++) {
			box.add(panels[i]);
			box.add(Box.createVerticalStrut(10));
		}
		revalidate();
	}
}

