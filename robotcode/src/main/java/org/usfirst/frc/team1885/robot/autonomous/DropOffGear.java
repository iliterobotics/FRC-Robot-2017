package org.usfirst.frc.team1885.robot.autonomous;
import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;

public class DropOffGear extends Command{

	private final GearManipulator gearManipulator;

	private double currentTime;
	
	public DropOffGear(GearManipulator gearManipulator){
		this.gearManipulator = gearManipulator;
	}
	
	@Override
	public void init() {
		currentTime = 0;
	}

	@Override
	public boolean update() {
		currentTime += Robot.UPDATE_PERIOD;
		if(currentTime > 750){
			gearManipulator.setLong(false);
			gearManipulator.setShort(false);
			return true;
		}
		else if(currentTime > 500){
			gearManipulator.setKick(false);
		}
		else if(currentTime > 250){
			gearManipulator.setKick(true);
		}else{
			gearManipulator.setLong(true);
			gearManipulator.setShort(true);
		}
		return false;
	}
	

}
