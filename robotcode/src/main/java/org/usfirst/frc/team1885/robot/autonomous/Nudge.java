package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;

public class Nudge extends Command{

	private static final double NUDGE_POWER = 0.5;
	private static final int NUDGE_TIME = 30;	
	
	private final double direction;
	
	private DriveTrain driveTrain;

	private int time;
	
	public Nudge(DriveTrain driveTrain, double direction){
		this.driveTrain = driveTrain;
		this.direction = direction;
	}
	
	@Override
	public void init() {
		time = 0;
	}

	@Override
	public boolean update() {
		time += Robot.UPDATE_PERIOD;
		driveTrain.setPower(-(NUDGE_POWER * direction), (NUDGE_POWER * direction));
		if(time >= NUDGE_TIME){
			driveTrain.setPower(0, 0);
			return true;
		}
		return false;
	}

}
