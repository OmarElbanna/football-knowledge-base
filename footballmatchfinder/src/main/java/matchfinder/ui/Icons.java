package matchfinder.ui;

import javax.swing.*;
import java.net.URL;

public class Icons {
	public static ImageIcon getPlayerIcon() {
		URL url = Icons.class.getResource("/football-player.png");
		return new ImageIcon(url);
	}

	public static ImageIcon getChampionshipIcon() {
		URL url = Icons.class.getResource("/championship.png");
		return new ImageIcon(url);
	}

	public static ImageIcon getFootballClubIcon() {
		URL url = Icons.class.getResource("/football-club.png");
		return new ImageIcon(url);
	}
}

