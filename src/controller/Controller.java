package controller;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.swing.*;
import javax.swing.text.View;

import buildings.ArcheryRange;
import buildings.Barracks;
import buildings.Building;
import buildings.EconomicBuilding;
import buildings.Farm;
import buildings.Market;
import buildings.MilitaryBuilding;
import buildings.Stable;
import engine.City;
import engine.Game;
import exceptions.BuildingInCoolDownException;
import exceptions.FriendlyCityException;
import exceptions.MaxCapacityException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import exceptions.NotEnoughGoldException;
import exceptions.TargetNotReachedException;
import units.Archer;
import units.Army;
import units.Cavalry;
import units.Infantry;
import units.Status;
import units.Unit;
import view.CityView;
import view.MainView;
import view.WorldMapView;

@SuppressWarnings("unused")
public class Controller implements ActionListener {
	private Game game;
	private MainView mainView;
	private ArrayList<JButton> citiesButtons;
	private WorldMapView mapView;
	private CityView cityView;
	
	private ArrayList<JButton> armiesButtonsW=new ArrayList<JButton>();
	private ArrayList<JButton> economicButtons=new ArrayList<JButton>();
	private ArrayList<JButton> militaryButtons=new ArrayList<JButton>();
	private ArrayList<JButton> armiesButtonsC=new ArrayList<JButton>(); //controlled buttons
	private ArrayList<JButton> unitsButtons=new ArrayList<JButton>();
	private ArrayList<Army> currentArmies= new ArrayList<Army>(); //controlled Armies in cityView 
	ArrayList<JButton> allButtons = new ArrayList<JButton>();

	private JButton endButton=new JButton("End Turn");
	//private JButton selectedArmy;
	
	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	private final int width = (int)size.getWidth();
	private final int height = (int)size.getHeight();
	
	private JTextArea playerDetails;
	private City currentCity;
	private City selectedCity; //when laysieging
	private Army currentArmy;
	private Army chosenArmy; //used to lay siege
	
	private JButton selectedArmy;
	private JButton lastSelectedUnit;
	//private JButton selectedCity;
	String buildType;
	JLabel l;
	
	String details;
	String details2 = " Army Details\n --------------\n"; 
    String buildingDetails = "\n Buildings Details\n --------------\n";
    String unitsDetails= "\n Units Details\n --------------\n";
    String cArmyUnitDetails= " Controlled Army's Details \n---------------\n";
    
    
	public Controller() throws IOException {
		String name=JOptionPane.showInputDialog(null,"Enter Your Name");
		
		String[] list = {"Cairo", "Rome", "Sparta"};
		String cityName = (String)JOptionPane.showInputDialog(null, "Select CityName", null, JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
	    game = new Game(name, cityName);
	    mainView= new MainView(height, width);
	    citiesButtons = new ArrayList<JButton>();
	    armiesButtonsW=new ArrayList<JButton>();
	    
	    playerDetails = new JTextArea();
	    playerDetails.setBounds((int)(width*2/3), 0, (int)( width/3), (int) (0.25 * height));
	    playerDetails.setBackground(new Color(25, 25, 112));
	    playerDetails.setForeground(Color.WHITE);
		playerDetails.setEditable(false);
		
		 
		endButton.setBounds((int)(width*2/3), (int)(height*0.25), (int)( width/6), (int) (0.125 * height));
		endButton.setBackground(new Color(160, 175, 183));
		endButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		endButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		endButton.addActionListener(this);
		endButton.setName("endButton");
		
	    this.drawWorldMap();
	    
	    
		
	}
	  
	public String playerDetails()
	{
		details = " Player Details\n------------\n";
		details+=" Player Name: " + game.getPlayer().getName();
		details+="\n Current Turn Count: "+ game.getCurrentTurnCount();
		details+="\n Food: "+game.getPlayer().getFood();
		details+="\n Treasury: " + game.getPlayer().getTreasury();
		details+="\n Max Turn Count: " + game.getMaxTurnCount();
		return details;
	}
	
	public String unitsDetails(Army m)
	{  // for controlled armies
		for(Unit u: m.getUnits()) {
			
			  if(u instanceof Archer)
			  {
				  cArmyUnitDetails+="\nUnit Type: "+"Archer\n";
				  cArmyUnitDetails+="Unit Level:"+u.getLevel()+"\n";
				  cArmyUnitDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
				  cArmyUnitDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			  }
			 else if(u instanceof Cavalry)
			 {
				 cArmyUnitDetails+="\nUnit Type: "+"Cavalry\n";
				 cArmyUnitDetails+="Unit Level:"+u.getLevel()+"\n";
				 cArmyUnitDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
				 cArmyUnitDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			 }
			else
			{
				cArmyUnitDetails+="\nUnit Type: "+"Infantry\n";
				cArmyUnitDetails+="Unit Level:"+u.getLevel()+"\n";
				cArmyUnitDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
				cArmyUnitDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			}

			}
		return cArmyUnitDetails;
	}
	
	public String defendingUnitsDetails(Unit u) {
		//for defending army 
		for(Unit unit: currentCity.getDefendingArmy().getUnits())
		{
			if(u instanceof Archer)
			{
			   unitsDetails+="\n Defending Army's Units\n";
			   unitsDetails+="\nUnit Type: "+"Archer\n";
			   unitsDetails+="Unit Level:"+u.getLevel()+"\n";
			   unitsDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
			   unitsDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			}
			else if(u instanceof Cavalry)
			{
				unitsDetails+="\n Defending Army's Units\n";
				unitsDetails+="\nUnit Type: "+"Cavalry\n";
				unitsDetails+="Unit Level:"+u.getLevel()+"\n";
				unitsDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
				unitsDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			}
			else
			{
				unitsDetails+="\n Defending Army's Units\n";
				unitsDetails+="\nUnit Type: "+"Infantry\n";
				unitsDetails+="Unit Level:"+u.getLevel()+"\n";
				unitsDetails+="Current Soldier Count: "+u.getCurrentSoldierCount()+"\n";
				unitsDetails+="Max Solider Count: "+u.getMaxSoldierCount()+"\n";
			}

			
		}
		return unitsDetails;
	}
	
	public void displaybuildButton(String buildType){
		
		for (EconomicBuilding e: currentCity.getEconomicalBuildings() ) {

			if (e instanceof Farm && buildType.equals("Farm")) {
				JButton b = new JButton("Farm");
				b.setBackground(new Color(160, 175, 183));
				b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				b.addActionListener(this);
				economicButtons.add(b);
				b.setName("ecoButton");
				cityView.getBuildings().add(b);
				
				buildingDetails="Type: "+ "Farm\n";
				buildingDetails+="Level: "+ e.getLevel()+"\n";
				buildingDetails+="Upgrade Cost: "+e.getUpgradeCost()+"\n";
				
						
			}
			else if ((e instanceof Market && buildType.equals("Market"))){
                JButton b = new JButton("Market");
				b.setBackground(new Color(160, 175, 183));
				b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				b.addActionListener(this);
				economicButtons.add(b);
				b.setName("ecoButton");
				cityView.getBuildings().add(b);
				
				buildingDetails="Type: "+ "Market\n";
				buildingDetails+="Level: "+ e.getLevel() +"\n";
				buildingDetails+="Upgrade Cost: "+e.getUpgradeCost()+"\n";
			}
		}
		
		for(MilitaryBuilding m : currentCity.getMilitaryBuildings()) {
			if (m instanceof ArcheryRange && buildType.equals("ArcheryRange")) {
				JButton b = new JButton("ArcheryRange");
				b.setBackground(new Color(160, 175, 183));
				b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				b.addActionListener(this);
				militaryButtons.add(b);
				b.setName("militaryButton");
				cityView.getBuildings().add(b);
				
				buildingDetails="Type: "+ "ArcheryRange\n";
				buildingDetails+="Level: "+ m.getLevel()+"\n";
				buildingDetails+="Upgrade Cost: "+m.getUpgradeCost()+"\n";
				}
			else if (m instanceof Barracks && buildType.equals("Barracks")) {
				JButton b = new JButton("Barracks");
				b.setBackground(new Color(160, 175, 183));
				b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				b.addActionListener(this);
				militaryButtons.add(b);
				b.setName("militaryButton");
				cityView.getBuildings().add(b);
				
				buildingDetails="Type: "+ "Barracks\n";
				buildingDetails+="Level: "+ m.getLevel()+"\n";
				buildingDetails+="Upgrade Cost: "+m.getUpgradeCost()+"\n";
			}
			else if (m instanceof Stable && buildType.equals("Stable")) {
				JButton b = new JButton("Stable");
				b.setBackground(new Color(160, 175, 183));
				b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				b.addActionListener(this);
				militaryButtons.add(b);
				b.setName("militaryButton");
				cityView.getBuildings().add(b);
				
				buildingDetails="Type: "+ "Stable\n";
				buildingDetails+="Level: "+ m.getLevel()+"\n";
				buildingDetails+="Upgrade Cost: "+m.getUpgradeCost()+"\n";
			}
		}
			cityView.repaint();
			cityView.revalidate();
			
		
	}
	
	
	
	public void drawWorldMap()
	{
		
		citiesButtons.clear();
		mainView.getContentPane().removeAll();
		mapView= new WorldMapView(width, height);
		
//		l=mainView.getMyLabel();
//		mapView.add(l);
		
		mapView.add(playerDetails);
		mapView.add(endButton);
		
		
		JButton targetCityButton = new JButton("Target City");
		targetCityButton.setBackground(new Color(160, 175, 183));
		targetCityButton.setBounds((int)(width*0.75+90), (int)(height*0.375), (int)( width/6), (int) (0.125 * height));
		targetCityButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		targetCityButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		targetCityButton.addActionListener(this);
		targetCityButton.setName("targetCityButton");
		mapView.add(targetCityButton);
		
		
	
		
		
		for (City c: game.getAvailableCities()) {
			JButton b = new JButton(c.getName());
			b.setBackground(new Color(160, 175, 183));
			b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
			b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
			b.addActionListener(this);
			citiesButtons.add(b);
			b.setName("cityButton");
			mapView.getCities().add(b);
			
		}

		String s=this.playerDetails();
		playerDetails.setText(s);
		
		
//		details2+="Target City: "+"Cairo"+"\n";
//		details2+="Turns Left To Target: "+ 1 +"\n";
//		mainView.add(mapView);
		
		JButton laySiege=new JButton();
		laySiege= new JButton("Lay Siege");
		laySiege.setBounds((int)(width*0.75+90), (int)(height*0.25), (int)( width/6), (int) (0.125 * height));
		laySiege.setBackground(new Color(160, 175, 183));
		laySiege.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		laySiege.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		laySiege.addActionListener(this);
		laySiege.setName("laySiegeButton");
		mapView.add(laySiege);
		
		
		int counter = 1;
		
		for (Army a : game.getPlayer().getControlledArmies()) {
			if (a.getCurrentLocation().equals("onRoad")) {
			
				JButton b2 = new JButton("Army " + counter +" \n " +(a.getCurrentStatus()));
				b2.setBackground(new Color(160, 175, 183));
				b2.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				b2.setFont(new Font(Font.MONOSPACED,Font.BOLD,16));
				counter++;
				b2.addActionListener(this);
				b2.setName("armyButton");
				armiesButtonsW.add(b2);
				mapView.getArmies().add(b2);
			}	
				
				details2+="Target City: "+a.getTarget()+"\n";
				details2+="Turns Left To Target: "+a.getDistancetoTarget()+"\n";
				
				
				details2+="City Under Seige: "+a.getCurrentLocation()+"\n";
				for (City c : game.getAvailableCities()) {
					if (c.getName().equals(a.getCurrentLocation())) 
					{
						details2+="Turns Under Seige:"+c.getTurnsUnderSiege()+"\n";
					}
				}
				
				
				for(int i=0;i<a.getUnits().size();i++) {
					if(a.getUnits().get(i) instanceof Archer)
					{
					   details2+="\nUnit Type: "+"Archer\n";
					   details2+="Unit Level:"+a.getUnits().get(i).getLevel()+"\n";
					   details2+="Current Soldier Count: "+a.getUnits().get(i).getCurrentSoldierCount()+"\n";
					   details2+="Max Solider Count: "+a.getUnits().get(i).getMaxSoldierCount()+"\n";
					}
					else if(a.getUnits().get(i)instanceof Cavalry)
					{
						 details2+="\nUnit Type: "+"Cavalry\n";
						 details2+="Unit Level:"+a.getUnits().get(i).getLevel()+"\n";
						 details2+="Current Soldier Count: "+a.getUnits().get(i).getCurrentSoldierCount()+"\n";
						 details2+="Max Solider Count: "+a.getUnits().get(i).getMaxSoldierCount()+"\n";
					}
					else
					{
						 details2+="\nUnit Type: "+"Infantry\n";
						 details2+="Unit Level:"+a.getUnits().get(i).getLevel()+"\n";
						 details2+="Current Soldier Count: "+a.getUnits().get(i).getCurrentSoldierCount()+"\n";
						 details2+="Max Solider Count: "+a.getUnits().get(i).getMaxSoldierCount()+"\n";
					}
					}
			
		}
		
		//mapView.getArmyDetails().setText(details2);
		
        mapView.revalidate();
        mapView.repaint();
		mainView.add(mapView);
		mainView.revalidate();
     	mainView.repaint();
		
	}
	
	public void drawCityView(City c)
	{
		if((economicButtons!=null))
		   economicButtons.clear();
		if(militaryButtons!=null)
		    militaryButtons.clear();
		if (unitsButtons!=null)
			unitsButtons.clear();
		if (armiesButtonsC!= null)
			armiesButtonsC.clear();
		if(currentArmies!=null)
			currentArmies.clear();
		
		
		mainView.getContentPane().removeAll();
		cityView= new CityView(width, height);
		
//		l=mainView.getMyLabel();
//		cityView.add(l);
		
		cityView.add(playerDetails);
		cityView.add(endButton);
		
		
		
		String s=this.playerDetails();
		playerDetails.setText(s);
		playerDetails.setVisible(true);
		playerDetails.setSelectionColor(new Color(110));
		
		playerDetails.repaint();
		playerDetails.revalidate();
		
		
		JButton gotoMap = new JButton("Go to World Map");
		gotoMap.setBackground(new Color(160, 175, 183));
		gotoMap.setBounds((int)(width*0.75+90), (int)(height*0.375), (int)( width/6), (int) (0.125 * height));
		gotoMap.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		gotoMap.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		gotoMap.addActionListener(this);
		gotoMap.setName("gotoMapButton");
		cityView.add(gotoMap);
		
//		JButton relocateButton = new JButton("Relocate Unit");
//		relocateButton.setBackground(new Color(160, 175, 183));
//		relocateButton.setBounds((int)(width*2/3), (int)(height*0.375), (int)( width/6), (int) (0.125 * height));
//		relocateButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
//		relocateButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
//		relocateButton.addActionListener(this);
//		relocateButton.setName("relocateButton");
//		cityView.add(relocateButton);
		
		
//		JButton initiateArmy = new JButton("Initiate Army"); 
//		initiateArmy.setBounds((int)(width*0.75+90), (int)(height*0.375), (int)( width/6), (int) (0.125 * height));
//		initiateArmy.setBackground(new Color(160, 175, 183));
//		initiateArmy.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
//		initiateArmy.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
//		initiateArmy.addActionListener(this);
//		initiateArmy.setName("initiateButton");
//		cityView.add(initiateArmy);
		
		JButton build = new JButton("Build"); 
		build.setBounds((int)(width*0.75+90), (int)(height*0.25), (int)( width/6), (int) (0.125 * height));
		build.setBackground(new Color(160, 175, 183));
		build.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		build.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		build.addActionListener(this);
		build.setName("buildButton");
		cityView.add(build);
		
		JButton defendbutton= new JButton("Defending Army");
		defendbutton.setBounds(0, (int) (0.5 * height), (int)(width/8), (int)(height*0.25));
		defendbutton.setBackground(new Color(160, 175, 183));
		defendbutton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		defendbutton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
		defendbutton.addActionListener(this);
		defendbutton.setName("defendButton");
		cityView.add(defendbutton);
		
		if (currentCity.getDefendingArmy().getUnits().size() > 0) {
		
			for (Unit u : currentCity.getDefendingArmy().getUnits()){
				if (u instanceof Archer) {
				JButton unitButton= new JButton ("Archer" + u.getLevel());
				unitButton.setBackground(new Color(160, 175, 183));
				unitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
				unitButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
				unitButton.addActionListener(this);
				unitButton.setName("unitButton");
				unitsButtons.add(unitButton);
				cityView.getUnitsButtons().add(unitButton);
				}
				else if (u instanceof Infantry) {
					JButton unitButton= new JButton ("Infantry" + u.getLevel());
					unitButton.setBackground(new Color(160, 175, 183));
					unitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
					unitButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
					unitButton.addActionListener(this);
					unitButton.setName("unitButton");
					unitsButtons.add(unitButton);
					
					cityView.getUnitsButtons().add(unitButton);
				}
				else if (u instanceof Cavalry) {
					JButton unitButton= new JButton ("Cavalry" + u.getLevel());
					unitButton.setBackground(new Color(160, 175, 183));
					unitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
					unitButton.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
					unitButton.addActionListener(this);
					unitButton.setName("unitButton");
					unitsButtons.add(unitButton);
					
					cityView.getUnitsButtons().add(unitButton);
				}
			}
		}
		int counter=1;
		for(Army m: game.getPlayer().getControlledArmies())
		{
			
			if(m.getCurrentLocation().toLowerCase().equals(currentCity.getName().toLowerCase()))
			{

			JButton b = new JButton("Army " + counter);
			counter++;
			
			b.setBackground(new Color(160, 175, 183));
			b.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
			b.setFont(new Font(Font.MONOSPACED,Font.ITALIC,16));
			b.addActionListener(this);
			currentArmies.add(m);
			armiesButtonsC.add(b);
			b.setName("armyButtonC");
			cityView.getArmies().add(b);
			
			
			}
		}
		String string="";
		for(EconomicBuilding b: currentCity.getEconomicalBuildings())
		{
			
			if(b instanceof Farm)
			{
				string="Farm";
				this.displaybuildButton(string);
			}
			else
			{
				string="Market";
				this.displaybuildButton(string);
			}

		}
		
	 
		for(MilitaryBuilding m: currentCity.getMilitaryBuildings())
		{

			if(m instanceof ArcheryRange)
			{
				
				string="ArcheryRange";
				this.displaybuildButton(string);
			}
			else if(m instanceof Stable)
			{
				string="Stable";
				this.displaybuildButton(string);
			}
			else
			{
				string="Barracks";
				this.displaybuildButton(string);

			}
		}
	 
		
		cityView.setVisible(true);

		
		cityView.revalidate();
		cityView.repaint();
		mainView.add(cityView);
		mainView.revalidate();
	    mainView.repaint();
	}
	
	

	public void actionPerformed(ActionEvent e) {
		JButton source = ((JButton)(e.getSource()));
		
		if (source.getName().equals("gotoMapButton")) {
			this.drawWorldMap();
		}
		
		
		if (source.getName().equals("unitButton")) {
			//lastSelectedUnit = source;
			Unit u= currentCity.getDefendingArmy().getUnits().get(unitsButtons.indexOf(e.getSource()));
			
			Object[] options = {"Initiate Army","Relocate Unit"};
			int n = JOptionPane.showOptionDialog(mainView,"Please Choose an Action",null, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);

			if (n==0) {
				
				this.game.getPlayer().initiateArmy(currentCity, u);
				System.out.println(game.getPlayer().getControlledArmies().size());
				this.drawCityView(currentCity);
			}
			else if (n==1) {
				int counter =1;
				String [] list = new String[currentArmies.size()] ;
				for (int i=0; i<currentArmies.size(); i++) {
					list[i] = "Army" + counter;
					counter++;
				}
				
				int j = JOptionPane.showOptionDialog(mainView,"Please Choose an Army",null, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,list, list[0]);
				try {
				currentArmies.get(j).relocateUnit(u);
				this.drawCityView(currentCity);
				}
				catch (MaxCapacityException mc) {
					JOptionPane.showMessageDialog(mainView, "Army has reached the max capacity");
				}
			}
			
			
			String s = this.defendingUnitsDetails(u);
			cityView.getUnitsDetails().setText(s);
			
		}
		
//	
		
		
		if (source.getName().equals("targetCityButton") && selectedArmy!=null)
		{
			String[] list = { "Cairo", "Rome", "Sparta" };
			String selectedCityName = (String) JOptionPane.showInputDialog(null, "Select CityName", null,JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
			
		int armyIndex = armiesButtonsW.indexOf(selectedArmy);
		Army chosenArmy = game.getPlayer().getControlledArmies().get(armyIndex);
		game.targetCity(chosenArmy, selectedCityName);
		
		System.out.println("test2");
		selectedArmy = null;

		}
//		else {
//			if (!source.getName().equals("targetCityButton")) {
//				if (selectedArmy == null) {
//					selectedArmy = source;
//				} else {
//					if ((JButton) (e.getSource()) == selectedArmy) {
//						selectedArmy = null;
//					} else {
//						selectedArmy = source;
//					}
//				}
//			}
//		}
		

		

		if (((JButton) (e.getSource())).getName().equals("laySiegeButton") && selectedArmy != null) {
			String[] list = { "Cairo", "Rome", "Sparta" };
			String selectedCityName = (String) JOptionPane.showInputDialog(null, "Select CityName", null,JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
			
			for (City c : game.getAvailableCities()) {
				if (c.getName().equals(selectedCityName))
					selectedCity = c;
			}

			int armyIndex = armiesButtonsW.indexOf(selectedArmy);
			Army chosenArmy = game.getPlayer().getControlledArmies().get(armyIndex);

			try {
				game.getPlayer().laySiege(chosenArmy, selectedCity);

			} catch (FriendlyCityException f) {
				JOptionPane.showMessageDialog(mainView, "Can't Lay Siege On The City!");

			} catch (TargetNotReachedException t) {
				JOptionPane.showMessageDialog(mainView, "City Not Reached Yet!");
			}

			selectedArmy = null;

			// int cityIndex = citiesButtons.indexOf(selectedCity);
			// City chosenCity = game.getAvailableCities().get(cityIndex);
			// selectedCity =null;

		} else {
			if (!((JButton) (e.getSource())).getName().equals("laySiegeButton")) {
				if (selectedArmy == null) {
					selectedArmy = (JButton) (e.getSource());
				} else {
					if ((JButton) (e.getSource()) == selectedArmy) {
						selectedArmy = null;
					} else {
						selectedArmy = (JButton) (e.getSource());
					}
				}
			}
		}
		

		if( ((JButton)(e.getSource())).getName().equals("cityButton")) {
			currentCity = game.getAvailableCities().get(citiesButtons.indexOf(e.getSource())); 
			if (game.getPlayer().getControlledCities().contains(currentCity)) {
			    this.drawCityView(currentCity);
			}
			else 
				JOptionPane.showMessageDialog(mainView,"Can't view The City!");
				
		}
		
		//defending army el details btb2a set bel controlled // World map view
		
		if (((JButton) (e.getSource())).getName().equals("armyButton")) {
				mapView.getArmyDetails().setText(details2);	
			}
		

		if(((JButton)(e.getSource())).getName().equals("endButton"))
		{
			//handling 3 turns besieiging
			this.game.endTurn();
			this.drawWorldMap();
			String s=this.playerDetails();
			playerDetails.setText(s);
			
		}
		
		
		
		if(((JButton)(e.getSource())).getName().equals("buildButton"))
		{
			String[] list = {"Farm", "Market", "ArcheryRange","Barracks","Stable"};
			buildType = (String)JOptionPane.showInputDialog(null, "Select a Building", null, JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
		    
			try
			{
				if (buildType.equals("Farm") || buildType.equals("Market")){
				if(currentCity.getEconomicalBuildings().size()==0)
				{	
					game.getPlayer().build(buildType, currentCity.getName());
					this.drawCityView(currentCity);
				}
				else 
				{
				for (EconomicBuilding eb : currentCity.getEconomicalBuildings()) {
					if (eb instanceof Farm && buildType.equals("Farm"))  {
						JOptionPane.showMessageDialog(mainView,"You Can't Build Again");
					}
					else if (eb instanceof Market && buildType.equals("Market"))
					{
						JOptionPane.showMessageDialog(mainView,"You Can't Build Again");
					}
					else
					{
					  int size=currentCity.getEconomicalBuildings().size();
					  game.getPlayer().build(buildType, currentCity.getName());
					  this.drawCityView(currentCity);

					}
				}
			}
				
		}
else if (buildType.equals("ArcheryRange")|| buildType.equals("Barracks")||buildType.equals("Stable"))	
			{
			if(currentCity.getMilitaryBuildings().size()==0)
					{
						game.getPlayer().build(buildType, currentCity.getName());
						this.drawCityView(currentCity);
					}
			else {
				for (MilitaryBuilding mb: currentCity.getMilitaryBuildings()) {
					if ( mb instanceof ArcheryRange && buildType.equals("ArcheryRange")) {
						JOptionPane.showMessageDialog(mainView,"You Can't Build Again");
					}
					else if ( mb instanceof  Barracks && buildType.equals("Barracks")) {
						JOptionPane.showMessageDialog(mainView,"You Can't Build Again");
					}
					else if ( mb instanceof  Stable && buildType.equals("Stable")) {
						JOptionPane.showMessageDialog(mainView,"You Can't Build Again");
					}
					else
					{
					  int size=currentCity.getMilitaryBuildings().size();
					  game.getPlayer().build(buildType, currentCity.getName());
					  this.drawCityView(currentCity);

					}
				}
			}
				
			}
		    
	       }
			catch(NotEnoughGoldException ne)
			{
				JOptionPane.showMessageDialog(mainView,"You're poor");
			}
		}
		
		if( ((JButton)(e.getSource())).getName().equals("ecoButton")) {
			
			cityView.getBuildingDetails().setText(buildingDetails);
			
			Object[] options = {"Upgrade"};
			int n = JOptionPane.showOptionDialog(mainView,"Please Choose an Action",null, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);


			EconomicBuilding eco = currentCity.getEconomicalBuildings().get(economicButtons.indexOf(e.getSource()));
			try {
				if(n==0)
				{
					JOptionPane.showMessageDialog(mainView,"Your building will be upgraded");
					game.getPlayer().upgradeBuilding(eco);			
				}
				 
			}
			catch(NotEnoughGoldException ne)
			{
				JOptionPane.showMessageDialog(mainView,"You're poor");
			}
			catch(MaxLevelException m){
				
				JOptionPane.showMessageDialog(mainView,"Maximum building level is reached");  
			}
			catch(BuildingInCoolDownException bI)
			{
				JOptionPane.showMessageDialog(mainView,"The building is cooling down");  

			}
		
	}
		           
	
			if( ((JButton)(e.getSource())).getName().equals("militaryButton")) {
				cityView.getBuildingDetails().setText(buildingDetails);
				
				Object[] options = {"Upgrade","Recruit"};
				int n = JOptionPane.showOptionDialog(mainView,"Please Choose an Action",null, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);

				
				MilitaryBuilding m = currentCity.getMilitaryBuildings().get(militaryButtons.indexOf(e.getSource()));

				try {
					if(n==0) {
						JOptionPane.showMessageDialog(mainView,"Your building will be Upgraded");
						buildingDetails+="Upgrade Cost: "+m.getUpgradeCost()+"\n";
						JOptionPane.showMessageDialog(mainView,"Your building will be Upgraded");
						game.getPlayer().upgradeBuilding(m);
						this.drawCityView(currentCity);
					}
					else if( n==1){
					   JOptionPane.showMessageDialog(mainView,"Recruiting Units");
					   buildingDetails+="\nRecruitment Cost: "+ m.getRecruitmentCost()+"\n";
					   if(m instanceof ArcheryRange)
					   {
					      game.getPlayer().recruitUnit("archer", currentCity.getName());
					      this.drawCityView(currentCity);
					      unitsDetails="";
					   }
					   
					   else if(m instanceof Barracks)
					   {
					      game.getPlayer().recruitUnit("infantry", currentCity.getName());
					      this.drawCityView(currentCity);
					      unitsDetails="";
					   }
					   else 
					   {
					      game.getPlayer().recruitUnit("cavalry", currentCity.getName());
					      this.drawCityView(currentCity);
					      unitsDetails="";
					   }
			
				}
				}
				catch(MaxLevelException ml)
				{
					JOptionPane.showMessageDialog(mainView,"You Have Reached the Maximum Level");
				}
				catch(NotEnoughGoldException ne)
				{
					JOptionPane.showMessageDialog(mainView,"You're poor");
				}
				catch (MaxRecruitedException r){
					JOptionPane.showMessageDialog(mainView,"Maximum Recruited Units is Reached");  
				}
				catch (BuildingInCoolDownException i ) {
					JOptionPane.showMessageDialog(mainView,"The building is cooling down");
				}
				
			}
			
			
			if(((JButton)(e.getSource())).getName().equals("defendButton"))
			{
//				Army m= currentCity.getDefendingArmy();
//				String s = this.defendingUnitsDetails(m);
//				cityView.getUnitsDetails().setText(s);	
			}
			// City View
			if(((JButton)(e.getSource())).getName().equals("armyButtonC"))
			{
				currentArmy = currentArmies.get(armiesButtonsC.indexOf(e.getSource()));
				String s=this.unitsDetails(currentArmy);
				cityView.getcArmyUnitDetails().setText(s);
			} 
            
		
			
		
		//	if(source.getName().equals("RelocateButton") && selectedArmy!=null && lastSelectedUnit!=null)
//				{
//		       
//					int armyIndex = armiesButtonsW.indexOf(selectedArmy);
//					Army chosenArmy = game.getPlayer().getControlledArmies().get(armyIndex);
//					for(Unit u: chosenArmy.getUnits())
//					{
//						try {
//						
//						if(u instanceof Archer )
//						{
//							chosenArmy.relocateUnit(u);
//						}
//						else if(u instanceof Infantry ) 
//						{
//							chosenArmy.relocateUnit(u);
//						}
//						else if (u instanceof Cavalry ) 
//						{
//							chosenArmy.relocateUnit(u);
//						}
//						}
//						catch(MaxCapacityException m)
//						{
//							JOptionPane.showMessageDialog(mainView, "Army has reached the max capacity");
//						}
//					}
//					System.out.println("test1");
//					selectedArmy = null;
//				}
		
}
		
	
	public static void main(String[] args) throws IOException {
		new Controller();
	}
}
