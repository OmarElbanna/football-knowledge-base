package matchfinder.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class BannerPanel extends JPanel {

	private Image image;
	private Dimension prefSize;

	public BannerPanel() {
		URL url = getClass().getResource("/MatchesFinderBanner.png");
		ImageIcon icon = new ImageIcon(url);
		image = icon.getImage();
		prefSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
		setBorder(BorderFactory.createEmptyBorder(7, 7, 0, 0));
	}


	public Dimension getPreferredSize() {
		return prefSize;
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}

