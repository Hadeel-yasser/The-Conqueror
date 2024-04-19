package view;

import java.awt.Image;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MainView  extends JFrame{
 
	private ImageIcon icon;
	private JLabel myLabel;
	
	public MainView(int h, int w) {
		
		icon= new ImageIcon(new ImageIcon("Images/Game map1.jpg").getImage().getScaledInstance(w-10, h-10, Image.SCALE_DEFAULT));
		
		myLabel= new JLabel(icon);
		myLabel.setSize(w, h);
		this.add(myLabel);
		this.setBounds(0, 0, w-10, h-10);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setVisible(true);
		
	}

	public JLabel getMyLabel() {
		return myLabel;
	}
	
}
