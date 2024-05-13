package matchfinder.ui;


import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class AboutPanel extends JPanel {

	public static final String COPYRIGHT = "Copyright Ain Shams University";
	public static final String ACK_1 = "Youssef George - Omar Elbanna - Kerollos Wageeh - Marc Nagy";
    public static final String ACK_2 = "Team 1";
	public static final String ACK_3 = "19P9824 - 19Pxxxx - 19P3468 - 19Pxxxx";


	public AboutPanel() {
		setupUI();
	}

	protected void setupUI() {
		setLayout(new BorderLayout(7, 7));
		setupInformationPanel();
	}

	protected void setupInformationPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
		                                                GridBagConstraints.NONE,
		                                                new Insets(7, 7, 7, 7),
		                                                0, 0);
		JPanel box = new JPanel(new GridBagLayout());
		box.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		box.add(new ImagePanel(), gbc);
		gbc.gridy = 1;
		box.add(new JLabel("Ontology: football.rdf"), gbc);
		gbc.gridy = 2;
		box.add(new JLabel(COPYRIGHT), gbc);
		gbc.gridy = 3;
		box.add(new JLabel(ACK_1), gbc);
		gbc.gridy = 4;
		box.add(new JLabel(ACK_3), gbc);
		gbc.gridy = 5;
		box.add(new JLabel(ACK_2), gbc);
		gbc.gridy = 5;
		add(box, BorderLayout.SOUTH);
	}

	protected class ImagePanel extends JPanel {
		private ImageIcon image;

		public ImagePanel() {
			try {
				URL url = getClass().getResource("/soccer-player.png");
				image = new ImageIcon(url);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		public Dimension getPreferredSize() {
			return new Dimension(image.getIconWidth(), image.getIconHeight());
		}


		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.getImage(), 0, 0, this);
		}
	}
}

