package matchfinder.ui;

import org.semanticweb.owlapi.model.*;
import matchfinder.model.FootballOntology;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class MatchPanel extends JPanel {


    private HashMap<String, String> matchDetails;

    private final String matchName;

    private Dimension prefSize;

    public static final int LINE_SPACING = 5;

    public static final int SEPARATOR_SPACING = 5;

    public static final int VERTICAL_OFFSET = 7;

    public static final int DETAIL_FONT_SIZE = 14;

    public static final int MATCH_NAME_FONT_SIZE = 17;

    public static final Font MATCH_NAME_FONT = new Font("SansSerif", Font.BOLD, MATCH_NAME_FONT_SIZE);

    public static final Font DETAIL_FONT = new Font("SansSerif", Font.PLAIN, DETAIL_FONT_SIZE);


    public MatchPanel(FootballOntology ontology, OWLNamedIndividual match) {
        this.matchDetails = new HashMap<String, String>();
        if (match == null) {
            matchName = "No matches Found";
        }
        else {
            matchName = ontology.render(match);
            this.matchDetails = ontology.getMatchDetails(match);
        }
        int width = 300;
        int height = 30;
        height += VERTICAL_OFFSET;
        height += pointsToPixels(MATCH_NAME_FONT_SIZE);
        height += SEPARATOR_SPACING * 2 + 1;
        height += (matchDetails.size() + 1) * (pointsToPixels(DETAIL_FONT_SIZE) + LINE_SPACING);
        prefSize = new Dimension(width, height);
    }

    protected int pointsToPixels(int points) {
        return (int) (((points / 72.0) * getToolkit().getScreenResolution()));
    }

    public Dimension getPreferredSize() {
        return prefSize;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xPos = 20;
        int yPos = VERTICAL_OFFSET;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Shape clip = g2.getClip();
        g2.setColor(Color.WHITE);
        g2.fill(clip);
        g2.setColor(getForeground());
        g2.setFont(MATCH_NAME_FONT);
        yPos += g2.getFontMetrics().getHeight();
        g2.drawString(matchName, xPos, yPos);
        yPos += SEPARATOR_SPACING;
        g2.drawLine(xPos, yPos, 300, yPos);
        xPos += 30;
        yPos += SEPARATOR_SPACING + 1;
        g2.setFont(DETAIL_FONT);
        for (Map.Entry<String, String> entry : matchDetails.entrySet()) {
            yPos += g2.getFontMetrics().getHeight() + LINE_SPACING;
            String detail = entry.getKey() + ": " + entry.getValue();
            g2.drawString(detail, xPos, yPos);
        }
    }
}

