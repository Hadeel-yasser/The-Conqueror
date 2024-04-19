package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class WorldMapView extends JPanel {
	private JPanel cities; //or buildings
	private JPanel armies;
	//private JTextArea playerDetails;
	private JTextArea armyDetails;
	//private JButton attack;
   // button end turn //attack button or lay siege // transition between views
	
	public WorldMapView(int w, int h) {
		
		this.setLayout(null);
		this.setBounds(0,0, w-10, h-10);
		cities = new JPanel();
		armies = new JPanel();
		//playerDetails = new JTextArea();
		armyDetails = new JTextArea();
		cities.setLayout(new GridLayout(1,0));
		cities.setBounds(0, 0, (int)( 2*w/3), (int)( 0.5 * h));
		this.add(cities);
		
		armies.setLayout(new GridLayout(2,0));
		armies.setBounds(0, (int) (h * 0.5), (int) (2*w/3), (int)( 0.5 * h));
		this.add(armies);
		
		
		armyDetails.setBounds((int)(w*2/3), (int) (0.5 * h), (int)( w/3), (int) (0.5 * h));
		armyDetails.setEditable(false);
		this.add(armyDetails);
		
		
		
		this.revalidate();
		this.repaint();
	}
	

	public JTextArea getArmyDetails() {
		return armyDetails;
	}


	public JPanel getCities() {
		return cities;
	}

	public JPanel getArmies() {
		return armies;
	}



	
}
