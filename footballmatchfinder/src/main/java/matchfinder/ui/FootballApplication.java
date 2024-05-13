package matchfinder.ui;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import matchfinder.model.FootballOntology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;


public class FootballApplication extends JFrame {

	private FootballOntology ontology;

	private ResultsPanel resultsPanel;

	private JPanel cardPanel;

	private JPanel mainPanel;

    private Action aboutAction = new AbstractAction("About...") {
		public void actionPerformed(ActionEvent e) {
			AboutDialog dlg = new AboutDialog(FootballApplication.this);
			dlg.setVisible(true);
		}
	};

	public FootballApplication() {
		setupMenuBar();
		setupFrame();
		setupMainPanel();
		final LoaderPanel loaderPanel = new LoaderPanel();
		loaderPanel.startLoadingAnimation(mainPanel, BorderLayout.CENTER);
		Runnable r = new Runnable() {
			public void run() {
				// Create and load the Pizza Ontology
				ontology = new FootballOntology();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setupCardPanel();
						loaderPanel.stopLoadingAnimation();
					}
				});
			}
		};
		Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                loaderPanel.displayError(throwable.getMessage());
            }
        });
		t.start();
	}

	protected void setupMainPanel() {
		mainPanel = new JPanel(new BorderLayout(7, 7));
		mainPanel.add(new BannerPanel(), BorderLayout.NORTH);
        getContentPane().add(mainPanel);
	}

	protected void setupCardPanel() {
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
        cardPanel.add(new MainPanel(ontology, this), "ItemsChooserPanel");
        cardPanel.add(resultsPanel = new ResultsPanel(ontology, this), "ResultsPanel");
		mainPanel.add(cardPanel);
	}

	protected void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menu = new JMenu("Football Ontology");
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem(aboutAction);
		menu.add(menuItem);
	}

	protected void setupFrame() {
		setSize(1600, 1200);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public void showMatchPanel() {
		((CardLayout)cardPanel.getLayout()).first(cardPanel);
	}

	public void showResultsPanel(HashSet<OWLNamedIndividual> matches) {
		resultsPanel.setMatches(matches);
		((CardLayout)cardPanel.getLayout()).last(cardPanel);
	}

	public static void main(String [] args) {
		System.out.println("Starting app...");
		FootballApplication panel = new FootballApplication();
		System.out.println("...created football ontology app.");
		panel.setVisible(true);

	}
}

