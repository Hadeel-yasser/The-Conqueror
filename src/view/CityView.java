package view;

import java.awt.*;

import javax.swing.*;


public class CityView extends JPanel {

	
	private JPanel buildings;
	private JPanel unitsButtons; //unitsButtons in cityView
	private JTextArea buildingDetails;
	private JTextArea unitsDetails;  //unit details after pressing unit
	private JPanel armies;
	private JTextArea cArmyUnitDetails;
//	private JLabel l;
//	private ImageIcon icon;

//	private MainView mainView;

	public CityView(int w, int h) {
		this.setLayout(null);
		this.setBounds(0, 0, w - 10, h - 10);
		buildings = new JPanel();
		unitsButtons = new JPanel();
		buildingDetails = new JTextArea();
	
		unitsDetails= new JTextArea();
		armies=new JPanel();
		cArmyUnitDetails = new JTextArea();
		
//		icon= new ImageIcon(new ImageIcon("Images/Game map1.jpg").getImage().getScaledInstance(w-10, h-10, Image.SCALE_DEFAULT));
//		l= new JLabel(icon);
//		l.setSize(w, h);
//		this.add(l);

		
        this.setBackground(Color.LIGHT_GRAY);
		buildings.setLayout(new GridLayout(2, 0));
		buildings.setBounds(0, 0, (int)( w/3), (int) (0.25 * h));
		this.add(buildings);
		
		unitsButtons.setLayout(new GridLayout(2, 0));
		unitsButtons.setBounds((int)( w/4), (int)(h*0.5+100), (int)( w/2.375), (int) (0.35 * h));
		this.add(unitsButtons);

		buildingDetails.setBounds((int) (w * 2 / 3),(int)(h*0.5+100), (int) (w / 3), (int) (0.35 * h));
		buildingDetails.setEditable(false);
		buildingDetails.setBackground(new Color(25, 25, 112));
		buildingDetails.setForeground(Color.WHITE);
		this.add(buildingDetails);
		
		
		unitsDetails.setBounds((int)(w/8), (int)(0.5*h), (int)(w/8), (int)(h*0.25));
		unitsDetails.setEditable(false);
		this.add(unitsDetails);
		
		armies.setBounds(0, (int)(0.25*h), (int)(w/4), (int)(h*0.25));
		armies.setLayout(new GridLayout(2, 0));
		this.add(armies);
		
		cArmyUnitDetails.setBounds(0,(int)(h*0.75), (int) (w / 4), (int) (0.25 * h));
		cArmyUnitDetails.setEditable(false);
		cArmyUnitDetails.setBackground(new Color(25, 25, 112));
		cArmyUnitDetails.setForeground(Color.WHITE);
		this.add(cArmyUnitDetails);

		this.revalidate();
		this.repaint();
	}
	


	public JPanel getUnitsButtons() {
		return unitsButtons;
	}

	public JTextArea getcArmyUnitDetails() {
		return cArmyUnitDetails;
	}


	public JPanel getArmies() {
		return armies;
	}


	public JTextArea getUnitsDetails() {
		return unitsDetails;
	}


	public JPanel getBuildings() {
		return buildings;
	}

	public JTextArea getBuildingDetails() {
		return buildingDetails;
	}
}
