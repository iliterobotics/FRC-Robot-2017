package org.usfirst.frc.team1885.robot.autonomous;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;

public class DropOffGear extends Command{

	private final GearManipulator gearManipulator;
	private final DriveTrain driveTrain;
	
	private static final double BACKUP_SPEED = 0.5;
	
	private static final int KICK_TIME = 250;
	private static final int START_BACKUP_TIME = 500;
	private static final int STOP_BACKUP_TIME = 1500;
	
	private long initTime;
	
	public DropOffGear(GearManipulator gearManipulator, DriveTrain driveTrain){
		this.gearManipulator = gearManipulator;
		this.driveTrain = driveTrain;
	}
	
	@Override
	public void init() {
		initTime = System.currentTimeMillis();
	}

	@Override
	public boolean update() {
		long timePassed = System.currentTimeMillis() - initTime;
		if(timePassed > STOP_BACKUP_TIME){
			gearManipulator.setLong(false);
			gearManipulator.setShort(false);
			driveTrain.setPower(0, 0);
			return true;
		}
		else if(timePassed > START_BACKUP_TIME){
			gearManipulator.setKick(false);
			driveTrain.setPower(BACKUP_SPEED, BACKUP_SPEED);
		}
		else if(timePassed > KICK_TIME){
			gearManipulator.setKick(true);
		}else{
			gearManipulator.setLong(true);
			gearManipulator.setShort(true);
		}
		return false;
	}
	

}
