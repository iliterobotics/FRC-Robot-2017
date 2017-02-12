package org.usfirst.frc.team1885.robot.modules;

import java.util.Map;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class GearManipulator implements Module{

	private Map<PistonType, Solenoid> pistonMap;
	
	private boolean isOpen;
	private boolean isTilted;
	private boolean isKeked;
	
	private enum PistonType {
		DOOR_DOWN(0), DOOR_TIPPER(1), KICKER(2);
		
		public final int port;
		
		PistonType(int port){
			this.port = port;
		}
	}
	
	public GearManipulator(){
	}	

	@Override
	public void initialize() {
		for(PistonType type : PistonType.values()) {
			Solenoid solenoid = new Solenoid(type.port);
			pistonMap.put(type, solenoid);
		}
	}
	
	public void setOpen(boolean open){
		this.isOpen = open;
	}
	
	public void setTilted(boolean tilted){
		this.isTilted = tilted;
	}
	
	public void setKick(boolean kicked){
		if(!isOpen && kicked){
			kicked = false;
		}
		isKeked = kicked;
	}

	@Override
	public void update() {
		pistonMap.get(PistonType.DOOR_DOWN).set(isOpen);
		pistonMap.get(PistonType.DOOR_TIPPER).set(isTilted);
		pistonMap.get(PistonType.KICKER).set(isKeked);			
	}
	
}
