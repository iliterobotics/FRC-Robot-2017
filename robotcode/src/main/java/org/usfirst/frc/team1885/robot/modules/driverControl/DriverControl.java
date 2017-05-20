package org.usfirst.frc.team1885.robot.modules.driverControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.usfirst.frc.team1885.robot.Robot;
import org.usfirst.frc.team1885.robot.autonomous.Command;
import org.usfirst.frc.team1885.robot.autonomous.DriveStraight;
import org.usfirst.frc.team1885.robot.autonomous.Nudge;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.Climber.ClimberState;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.Shooter;

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
	
	public static final int CLIMBER_OPERATOR_BUTTON = 4;
	public static final int CLIMBER_DRIVER_BUTTON = 1;

	public static final int FLAP_DOWN_AXIS = 3;
	public static final int FLAP_TILT_BUTTON = 6;
	public static final int KICK_BUTTON = 2;
	public static final int UP_BUTTON = 5;
	public static final int DOWN_AXIS = 2;
	public static final int DROP_BUTTON = 1;
	public static final int WAIT_BUTTON = 8;
	public static final int LOOK_FOR_SIGNAL_BUTTON = 7;
	
	public static final double DROP_INTAKE_SPEED = 0.5;
	public static final double PASSIVE_INTAKE_SPEED = 0.5;
	
	public static final boolean HIGH_GEAR = true;
	public static final boolean LOW_GEAR = false;
	

	private Map<ControllerType, Joystick> controllerMap;
	private List<Command> runningCommands;
	
	private DriveStraight warpSpeedCommand;
	private Nudge nudgeCommand;
	
	private final DriveTrain driveTrain;
	private final GearManipulator gearManipulator;
	private final Climber climber;
	private final NavX navx;
	private final Shooter shooter;
	
	private boolean wasClimberPushed;
	private boolean wasToggleDrop;
	private boolean isWarping;
	private boolean isNudging;

	private boolean isWait;
	private boolean wasWaitPushed;

	private boolean isLook;
	private boolean wasLookPushed;
	
	public enum ControllerType {
		CONTROLLER(0), CONTROLLER_2(1), TEST_CONTROLLER(2), LEFT_STICK(3), RIGHT_STICK(4);

		final int controllerId;

		ControllerType(int id) {
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain, GearManipulator gearManipulator, Climber climber, NavX navx, Shooter shooter) {
		this.driveTrain = driveTrain;
		this.gearManipulator = gearManipulator;
		this.climber = climber;
		this.navx = navx;
		this.shooter = shooter;
		controllerMap = new HashMap<ControllerType, Joystick>();
		runningCommands = new ArrayList<>();
	}

	public void initialize() {
		for (ControllerType type : ControllerType.values()) {
			controllerMap.put(type, new Joystick(type.controllerId));			
		}
		wasClimberPushed = false;
		wasToggleDrop = false;
		wasWaitPushed = false;
		isWait = false;
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
		driveTrain.setSpeed(left, right);
	}
	
	public void updateManipulator(){
		Joystick driverController = getController(ControllerType.CONTROLLER);
		Joystick manipulatorController = getController(ControllerType.CONTROLLER_2);
		
		updateIntakeWheels(driverController, manipulatorController);
		
		if( !wasClimberPushed && manipulatorController.getRawButton(CLIMBER_OPERATOR_BUTTON)){
			if(climber.getClimberState() != ClimberState.INIT || driverController.getRawButton(CLIMBER_DRIVER_BUTTON)){
				climber.run();
				wasClimberPushed = true;
			}
		}else if(!manipulatorController.getRawButton(CLIMBER_OPERATOR_BUTTON)){
			wasClimberPushed = false;
		}

		if((manipulatorController.getRawAxis(FLAP_DOWN_AXIS) > TRIGGER_DEADZONE) ||
		    manipulatorController.getRawButton(FLAP_TILT_BUTTON)){
			gearManipulator.setLong(true);
			gearManipulator.setDropping(false);
		}else{
			gearManipulator.setLong(false);
		}
		
		if((manipulatorController.getRawAxis(FLAP_DOWN_AXIS) > TRIGGER_DEADZONE) || 
			manipulatorController.getRawButton(DROP_BUTTON)){
			gearManipulator.setShort(true);
		}else{
			gearManipulator.setShort(false);		
		}
		
		if(manipulatorController.getRawButton(DROP_BUTTON)){
			gearManipulator.setDropping(false);
		}
		
		if(manipulatorController.getRawButton(UP_BUTTON)){
			gearManipulator.setLowered(false);
		}else if(manipulatorController.getRawAxis(DOWN_AXIS) > TRIGGER_DEADZONE){
			gearManipulator.setLowered(true);	
		}

		if(manipulatorController.getRawButton(KICK_BUTTON)){
			gearManipulator.setKick(true);
		}else{
			gearManipulator.setKick(false);			
		}
		
		if(!wasToggleDrop && manipulatorController.getRawButton(DROP_BUTTON)){
			gearManipulator.setDropping(!gearManipulator.getDropping());
			wasToggleDrop = true;
		} else if(wasToggleDrop && !manipulatorController.getRawButton(DROP_BUTTON)){
			wasToggleDrop = false;
		}
		
		if(!wasWaitPushed && manipulatorController.getRawButton(WAIT_BUTTON)){
			isWait = !isWait;
			wasWaitPushed = true;
		} else if(wasWaitPushed && !manipulatorController.getRawButton(WAIT_BUTTON)){
			wasWaitPushed = false;
		}
		
		if(!wasLookPushed && manipulatorController.getRawButton(LOOK_FOR_SIGNAL_BUTTON)){
			isLook = !isLook;
			wasLookPushed = true;
		} else if(wasLookPushed && !manipulatorController.getRawButton(LOOK_FOR_SIGNAL_BUTTON)){
			wasLookPushed = false;
		}

	}
	
	private void updateIntakeWheels(Joystick driverController, Joystick manipulatorController) {
		if(manipulatorController.getRawAxis(1) > 0.5){
			gearManipulator.setIntakeSpeed(GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		else if(manipulatorController.getRawAxis(1) < -0.5){
			gearManipulator.setIntakeSpeed(-GearManipulator.DEFAULT_INTAKE_SPEED);
		}
		else if(manipulatorController.getRawButton(DROP_BUTTON)){
			gearManipulator.setIntakeSpeed(DROP_INTAKE_SPEED);			
		} else if(manipulatorController.getRawButton(FLAP_TILT_BUTTON)){
			gearManipulator.setIntakeSpeed(PASSIVE_INTAKE_SPEED);
		}
		else {
			gearManipulator.setIntakeSpeed(0);
		}
	}
	
	public abstract void updateDriveTrain();
	
	public void update(){
		updateCommands();
		updateDriveTrain();
		updateManipulator();
	}
	
	public void setShift(boolean shifted){
		//driveTrain.setShift(shifted);
	}
	
	public Shooter getShooter() {
		return shooter;
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
	
	public boolean isWait(){
		return isWait;
	}
	
	public boolean isLook(){
		return isLook;
	}
}
