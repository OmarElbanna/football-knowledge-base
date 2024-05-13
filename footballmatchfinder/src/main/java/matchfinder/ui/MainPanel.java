package matchfinder.ui;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import matchfinder.model.ChoiceModel;
import matchfinder.model.FootballOntology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;


public class MainPanel extends JPanel {

	private FootballOntology ontology;

	private ChoiceModel choiceModel;

	private ChoicesPanel choicesPanel;

	private ItemListPanel includeListPanel;

	private ItemListPanel excludeListPanel;

	private FootballApplication application;

    public MainPanel(FootballOntology ontology, FootballApplication application) {
		this.ontology = ontology;
		this.application = application;
		choiceModel = new ChoiceModel(ontology);
		createUI();
	}

	protected void createUI() {
		setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(300);
		choicesPanel = new ChoicesPanel(ontology);
		splitPane.setLeftComponent(choicesPanel);
		Box box = new Box(BoxLayout.Y_AXIS);
		includeListPanel = new IncludeItemListPanel(ontology, choicesPanel, choiceModel);
		box.add(includeListPanel);

        box.add(Box.createVerticalStrut(12));
		excludeListPanel = new ExcludeItemListPanel(ontology, choicesPanel, choiceModel);
		box.add(excludeListPanel);
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 12));
		splitPane.setRightComponent(box);
		add(splitPane);
		setupQueryPanel();
    }


    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }


    protected void setupQueryPanel() {
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        queryPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        Action queryReasonerAction = new AbstractAction("Get Matches") {
            public void actionPerformed(ActionEvent e) {
                HashSet<OWLNamedIndividual> c = ontology.getMatches(choiceModel.getIncluded(), choiceModel.getExcluded());
                application.showResultsPanel(c);
            }
        };
        queryPanel.add(new JButton(queryReasonerAction));
        add(queryPanel, BorderLayout.SOUTH);
    }
}

