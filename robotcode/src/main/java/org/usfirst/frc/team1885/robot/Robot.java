
package org.usfirst.frc.team1885.robot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.usfirst.frc.team1885.robot.autonomous.Command;
import org.usfirst.frc.team1885.robot.autonomous.DriveStraightVision;
import org.usfirst.frc.team1885.robot.autonomous.DropOffGear;
import org.usfirst.frc.team1885.robot.autonomous.TurnToDegree;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.PressureSensor;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlArcadeControllerTwoStick;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot{
	
	
	public static final long UPDATE_PERIOD = 5;

	private DriveTrain driveTrain;
	private DriverControl driverControl;
	private NavX navx;
	private GearManipulator gearManipulator;
	private Climber climber;
	private PressureSensor pressureRegulator;
	
	private Queue<Command> autonomousCommands;
	private List<Module> runningModules;
	
	public Robot(){
		runningModules = new ArrayList<>();
		autonomousCommands = new LinkedList<>();
		
		navx = new NavX();
		pressureRegulator = new PressureSensor();
		driveTrain = new DriveTrain();
		gearManipulator = new GearManipulator();
		climber = new Climber();
		driverControl = new DriverControlArcadeControllerTwoStick(driveTrain, gearManipulator, climber, navx);
	}

	public void robotInit(){
		navx.resetDisplacement();
		while(navx.isCalibrating()){
			pause();
		}
		navx.setInitialAngle(navx.getAngle());
	}
	
	public void autonomous()
	{
		autonomousCommands.clear();
		autonomousCommands.add(new DriveStraightVision(driveTrain, navx, 90));
		autonomousCommands.add(new TurnToDegree(driveTrain, navx, 60, 5));
		autonomousCommands.add(new DriveStraightVision(driveTrain, navx, 15));
		autonomousCommands.add(new DropOffGear(gearManipulator, driveTrain));
		autonomousCommands.add(new TurnToDegree(driveTrain, navx, -10, 20));
		autonomousCommands.add(new DriveStraightVision(driveTrain, navx, 48));
		
		setRunningModules(driveTrain, gearManipulator, pressureRegulator);

		Command currentCommand = autonomousCommands.peek();
		if(currentCommand != null) currentCommand.init();
		while(isAutonomous() && isEnabled()){
				currentCommand = autonomousCommands.peek();
				if(currentCommand != null){
					if(currentCommand.update()){
						autonomousCommands.poll();
						if(autonomousCommands.peek() != null) {
							autonomousCommands.peek().init();
						} else {
							updateModules();
							break;
						}
					}
				}
				updateModules();
				pause();
		}
	}
	
	public void operatorControl()
	{
		setRunningModules(driverControl, gearManipulator, driveTrain, climber, pressureRegulator, pressureRegulator);
		while(isOperatorControl() && isEnabled()){
			updateModules();
			pause();
		}
	}
	
	public void test(){
		setRunningModules(pressureRegulator);
		while(isTest() && isEnabled()){
			updateModules();
			pause();
		}
	}
	
	private void initializeRunningModules(){
		for(Module mod : runningModules){
			mod.initialize();
		}
	}
	
	private void setRunningModules(Module ... modules){
		runningModules.clear();
		for(Module mod : modules){
			runningModules.add(mod);
		}
		initializeRunningModules();
	}
	
	private void updateModules(){
		for(Module mod : runningModules){
			mod.update();
		}
	}
	
	private void pause(){
		Timer.delay(0.001 * UPDATE_PERIOD);
	}
}
