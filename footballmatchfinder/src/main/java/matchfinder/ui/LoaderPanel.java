package matchfinder.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;


public class LoaderPanel extends JPanel {

	private JLabel label;
	private Timer timer;
	private JComponent owner;

	public LoaderPanel() {
		setupUI();
	}

	protected void setupUI() {
		setLayout(new BorderLayout());
		label = new JLabel();
		timer = new Timer(300, new ActionListener() {
			private int counter;
			private String text = "Please wait.  Loading ";

			public void actionPerformed(ActionEvent e) {
				String labelText = text;
				for(int i = 0; i < counter; i++) {
					labelText += ".";
				}
				label.setText(labelText);
				counter++;
				if(counter > 3) {
					counter = 0;
				}
			}
		});
		label.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 20));
		add(label);
	}

    public void displayError(String message) {
        timer.stop();
        label.setText(message);
    }

	public void startLoadingAnimation(JComponent component, Object constraints) {
		owner = component;
		component.add(this, constraints);
		startAnimation();
	}

	public void stopLoadingAnimation() {
		stopAnimation();
		owner.remove(this);
		owner.revalidate();
	}

	private void startAnimation() {
		timer.start();
	}

	private void stopAnimation() {
		timer.stop();
	}
}

