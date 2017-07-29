package org.usfirst.frc.team1885.robot.modules.driverControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.autonomous.Command;
import org.usfirst.frc.team1885.robot.autonomous.DriveStraight;
import org.usfirst.frc.team1885.robot.autonomous.Nudge;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.Joystick;

public abstract class DriverControl implements Module {
	
	public static final double JOYSTICK_DEADZONE = 0.05;
	public static final double TRIGGER_DEADZONE = 0.5;
	public static final double WARP_DEGREES_PER_SECOND = 5;
	public static final int GAMEPAD_LEFT_X = 0;
	public static final int GAMEPAD_LEFT_Y = 1;
	public static final int GAMEPAD_LEFT_TRIGGER = 2;
	public static final int GAMEPAD_RIGHT_TRIGGER = 3;
	public static final int GAMEPAD_RIGHT_X = 4;
	public static final int GAMEPAD_RIGHT_Y = 5;
	
	public static final boolean HIGH_GEAR = true;
	public static final boolean LOW_GEAR = false;
	
	private Map<ControllerType, Joystick> controllerMap;
	private List<Command> runningCommands;
	
	private DriveStraight warpSpeedCommand;
	private Nudge nudgeCommand;
	
	private final DriveTrain driveTrain;
	private final NavX navx;

	private boolean isWarping;
	private boolean isNudging;
	
	public enum ControllerType {
		CONTROLLER(0), CONTROLLER_2(1), TEST_CONTROLLER(2), LEFT_STICK(3), RIGHT_STICK(4);

		final int controllerId;

		ControllerType(int id) {
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain, NavX navx) {
		this.driveTrain = driveTrain;
		this.navx = navx;
		controllerMap = new HashMap<ControllerType, Joystick>();
		runningCommands = new ArrayList<>();
	}

	public void initialize() {
		for (ControllerType type : ControllerType.values()) {
			controllerMap.put(type, new Joystick(type.controllerId));			
		}
	}
	
	public void setSpeeds(double left, double right){
		if(nudgeCommand == null || !runningCommands.contains(nudgeCommand)){
			isNudging = false;
		}
		if(warpSpeedCommand == null || !runningCommands.contains(warpSpeedCommand)){
			isWarping = false;
		}
		
		if(isNudging) return;
		if(isWarping){
			double powerDiff = left - right;
			double degreesPerUpdate = ((1000.0 / Robot.UPDATE_PERIOD) / WARP_DEGREES_PER_SECOND);
			double angleDiff = powerDiff * degreesPerUpdate;
			warpSpeedCommand.adjustBearing(angleDiff);
			return;
		}
		
		if(Math.abs(left - right) < JOYSTICK_DEADZONE ){
			left = right = (left + right) / 2;
		}
		if(Math.abs(left) < JOYSTICK_DEADZONE){
			left = 0;
		}
		if(Math.abs(right) < JOYSTICK_DEADZONE){
			right = 0;
		}
		driveTrain.setPower(left, right);
	}
	
	public abstract void updateDriveTrain();
	
	public void update(){
		updateCommands();
		updateDriveTrain();
	}
	
	public void setShift(boolean shifted){
		driveTrain.setShift(shifted);
	}
	
	public Joystick getController(ControllerType type){
		return controllerMap.get(type);
	}
		
	protected void initiateWarpSpeed(double direction){
		warpSpeedCommand = new DriveStraight(driveTrain, navx, direction);
		warpSpeedCommand.init();
		setShift(true);
		injectCommand(warpSpeedCommand);
		isWarping = true;
	}
	
	protected void disableWarpSpeed(){
		removeCommand(warpSpeedCommand);
		setShift(false);
		warpSpeedCommand = null;
		isWarping = false;
	}
	
	protected void nudge(double direction){
		nudgeCommand = new Nudge(driveTrain, direction);
		injectCommand(nudgeCommand);
		isNudging = true;
	}
	
	public boolean isWarpSpeed(){
		return warpSpeedCommand != null;
	}

	protected void injectCommand(Command command){
		runningCommands.add(command);
	}
	
	private void updateCommands(){
		List<Command> commandsToRemove = new ArrayList<>();
		for(Command command : runningCommands){
			boolean status = command.update();
			if(status){
				commandsToRemove.add(command);
			}
		}
		for(Command command : commandsToRemove){
			runningCommands.remove(command);
		}
	}
	
	protected void removeCommand(Command command){
		runningCommands.remove(command);
	}
	
	public boolean isNudging(){
		return isNudging;
	}
	
	public boolean isWarping(){
		return isWarping;
	}
}
