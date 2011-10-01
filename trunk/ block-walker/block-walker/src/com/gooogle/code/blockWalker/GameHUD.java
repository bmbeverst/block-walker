package com.gooogle.code.blockWalker;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.util.HorizontalAlign;

import android.widget.Toast;

public class GameHUD extends HUD{
	private int energy = 100;
	private final ChangeableText energyText;
	private int life = 3 ;
	private Sprite life1;
	private Sprite life2;
	private Sprite life3;
	private ChangeableText levelText;
	static private int halfStepIncrease;
	static private int halfStepDecrease;
 
	//called by blockwalker class
	public GameHUD(){
		life1 = new Sprite(10, 10, 32, 32, Resources.loadTexture("heart.png", 32, 32));
		life2 = new Sprite(30, 10, 32, 32, Resources.loadTexture("heart.png", 32, 32));
		life3 = new Sprite(50, 10, 32, 32, Resources.loadTexture("heart.png", 32, 32));

		energyText = new ChangeableText(150 , 10, Resources.loadFont("Zrnic.ttf"), "Energy: ", "Energy: XXXXXX".length());
		levelText = new ChangeableText(400 , 10, Resources.loadFont("Zrnic.ttf"), "Level ", "Level XXXX".length());
		levelText.setText("Level 0");
		levelText.setColor(0, 0, 0, 0.55f);
		energyText.setText("Energy: " + "100");
		energyText.setColor(0, 0, 0, 0.55f);
 		this.attachChild(life1); 
		this.attachChild(life2);
		this.attachChild(life3);
 		this.attachChild(energyText);
 		this.attachChild(levelText);
	}
	
	
	public int getLifeCount(){
		return life;
	}
	
	public void setLifeCount(int pLife){
		switch(pLife){
			case 1:
				life1.setVisible(true);
				life2.setVisible(false);
				life3.setVisible(false);
				break;
			case 2:
				life1.setVisible(true);
				life2.setVisible(true);
				life3.setVisible(false);
				break;
			case 3: 
				life1.setVisible(true);
				life2.setVisible(true);
				life3.setVisible(true);
				break;
				
		}				
			life = pLife;
 	}
	
	public int getEnergyCount(){
		return energy;
	}
	
	public void setEnergyCount(int pEnergy){
		energy = pEnergy;
		energyText.setText("Energy: " + pEnergy);	
	}
	
	public void increaseEnergyCount(){
		//energy cap is at 200.
		if ( energy<200){
 		energy ++;
		}
		energyText.setText("Energy: " + energy);
	}
	

	public void increaseHalfEnergy() { 
		halfStepIncrease ++  ; 
		if ( halfStepIncrease == 10){
			this.increaseEnergyCount();
			halfStepIncrease = 0; // reset
		}
	}
	
	public boolean hasEnergy(){
		return energy > 0;
	}

	public void decreaesEnergyCount(){
		if(this.hasEnergy()){ 
				energy--;			
			energyText.setText("Energy: " + energy); 
		}
	}	
	

	public void decreaesHalfEnergyCount() {
		// for every x hit, decrease one energy. try to make it slower. 
		halfStepDecrease ++ ; 
		if (halfStepDecrease == 40 )
		{
			this.decreaesEnergyCount();
			halfStepDecrease = 0; //reset 			
		}
		
	}
	
	//if return false, game is over ! do something.
	public boolean decreaseLife(){
		if (life==3){
			life--;
			life3.setVisible(false);
 			return true;
		}
		
		if (life==2)
		{
			life--;
			life2.setVisible(false);
 			return true;
			
		}
		
		if (life==1)
		{
			life--;
			life1.setVisible(false);
 			return false;
 			//game over
			
		}
		 
		return false;
	
	}


	public void setLevelText(String pstring) {
		levelText.setText(pstring);
	}




	 
}
