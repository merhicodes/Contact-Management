package main;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;

public class ContactManagementApp extends JFrame {
	private Model model;
	private JPanel mainPanel;

	public ContactManagementApp() {
		super("Project NFA035");
		ImageIcon icon = new ImageIcon("source/call.png");
		setIconImage(icon.getImage());
		mainPanel = new JPanel(new CardLayout());
		// --------
		model = new Model(mainPanel);
//		controller = new Controller(model, mainPanel);
		// main panel
		// -------
		// frame settings
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				model.save();
				System.exit(0);
			}
		});
		this.setSize(500, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.add(mainPanel);
		this.setVisible(true);
	}
}
