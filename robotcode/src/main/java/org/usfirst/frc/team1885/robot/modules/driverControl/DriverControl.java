package org.usfirst.frc.team1885.robot.modules.driverControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.autonomous.Command;
import org.usfirst.frc.team1885.robot.autonomous.DriveStraight;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystick;
import org.usfirst.frc.team1885.robot.common.interfaces.IJoystickFactory;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;

public abstract class DriverControl implements Module {
	
	public static final double DEADZONE = 0.03;
	public static final double WARP_DEGREES_PER_SECOND = 5;
	public static final int GAMEPAD_LEFT_X = 0;
	public static final int GAMEPAD_LEFT_Y = 1;
	public static final int GAMEPAD_LEFT_TRIGGER = 2;
	public static final int GAMEPAD_RIGHT_TRIGGER = 3;
	public static final int GAMEPAD_RIGHT_X = 4;
	public static final int GAMEPAD_RIGHT_Y = 5;
	public static final int CLIMBER_BUTTON = 3;
	
	public static final boolean HIGH_GEAR = true;
	public static final boolean LOW_GEAR = false;
	

	private Map<ControllerType, IJoystick> controllerMap;
	private List<Command> runningCommands;
	
	private DriveStraight warpSpeedCommand;
	
	private final DriveTrain driveTrain;
	private final GearManipulator gearManipulator;
	private final Climber climber;
	private final NavX navx;
	private IJoystickFactory joystickFactory;
	
	private boolean wasClimberPushed;

	public enum ControllerType {
		LEFT_STICK(0), RIGHT_STICK(1), CONTROLLER(2), CONTROLLER_2(3);

		final int controllerId;

		ControllerType(int id) {
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, IJoystickFactory created) {
		this.driveTrain = driveTrain;
		this.joystickFactory = created;
		this.gearManipulator = gearManipulator;
		this.climber = climber;
		this.navx = navx;
		controllerMap = new HashMap<ControllerType, IJoystick>();
		runningCommands = new ArrayList<>();
	}

	public void initialize() {
		for (ControllerType type : ControllerType.values()) {
			controllerMap.put(type, joystickFactory.createJoystick(type.controllerId));			
		}
	}
	
	public void setSpeeds(double left, double right){
		
		if(warpSpeedCommand != null){
			double powerDiff = left - right;
			double degreesPerUpdate = ((1000.0 / Robot.UPDATE_PERIOD) / WARP_DEGREES_PER_SECOND);
			double angleDiff = powerDiff * degreesPerUpdate;
			warpSpeedCommand.adjustBearing(angleDiff);
			return;
		}
		
		if(Math.abs(left - right) < DEADZONE ){
			left = right = (left + right) / 2;
		}
		if(Math.abs(left) < DEADZONE){
			left = 0;
		}
		if(Math.abs(right) < DEADZONE){
			right = 0;
		}
		driveTrain.setPower(left, right);
	}
	
	public void updateManipulator(){
		IJoystick manipulatorController = getController(ControllerType.CONTROLLER_2);
		if(manipulatorController.getRawAxis(1) > 0.5){
			gearManipulator.setIntakeSpeed(-GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		else if(manipulatorController.getRawAxis(1) < -0.5){
			gearManipulator.setIntakeSpeed(GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		
		if( !wasClimberPushed && manipulatorController.getRawButton(CLIMBER_BUTTON)){
			climber.run();
			wasClimberPushed = true;
		}else if(!manipulatorController.getRawButton(CLIMBER_BUTTON)){
			wasClimberPushed = false;
		}
	}
	
	public abstract void updateDriveTrain();
	
	public void update(){
		updateCommands();
		updateDriveTrain();
		updateManipulator();
	}
	
	public void setShift(boolean shifted){
		driveTrain.setShift(shifted);
	}
	
	public void setCasters(boolean casted){
		driveTrain.setCasting(casted);
	}
	
	public IJoystick getController(ControllerType type){
		return controllerMap.get(type);
	}
		
	protected void initiateWarpSpeed(){
		warpSpeedCommand = new DriveStraight(driveTrain, navx);
		warpSpeedCommand.init();
		setShift(true);
		injectCommand(warpSpeedCommand);
	}
	
	protected void disableWarpSpeed(){
		removeCommand(warpSpeedCommand);
		setShift(false);
		warpSpeedCommand = null;
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
			if(!status){
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
}
