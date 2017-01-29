
package org.usfirst.frc.team1885.robot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.usfirst.frc.team1885.robot.autonomous.AutonomousCommand;
import org.usfirst.frc.team1885.robot.autonomous.DriveStraightNavX;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl.ControllerType;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlArcadeControllerTwoStick;
import org.usfirst.frc.team1885.robot.modules.test.TestClamp;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot{
	
	
	public static final long UPDATE_PERIOD = 5;

	private DriveTrain driveTrain;
	private DriverControl driverControl;
	private NavX navx;
	private GearManipulator gearManipulator;
	
	private Queue<AutonomousCommand> autonomousCommands;
	private List<Module> runningModules;
	
	public Robot(){
		runningModules = new ArrayList<>();
		autonomousCommands = new LinkedList<>();
		navx = new NavX();
		driveTrain = new DriveTrain();	
		driverControl = new DriverControlArcadeControllerTwoStick(driveTrain, navx);
		gearManipulator = new GearManipulator(driverControl);
		
	}

	public void robotInit(){
		navx.resetDisplacement();
	}
	
	public void autonomous()
	{
		setRunningModules(driveTrain);
		autonomousCommands.clear();
//		autonomousCommands.add(new DriveStraightNavX(driveTrain, navx));
		autonomousCommands.add(new DriveStraightNavX(driveTrain, navx));
		AutonomousCommand currentCommand = autonomousCommands.peek();
		if(currentCommand != null){
			currentCommand.init();
		}
		while(isAutonomous() && isEnabled()){
			currentCommand = autonomousCommands.peek();
			if(currentCommand != null){
				if(currentCommand.update()){
					autonomousCommands.poll();
					autonomousCommands.peek().init();
				}
			}
			updateModules();
			pause();
		}
	}
	
	public void operatorControl()
	{
		setRunningModules(driverControl, driveTrain);
		while(isOperatorControl() && isEnabled()){
			updateModules();
			pause();
		}
	}
	
	public void test(){
		TestClamp testClamp = new TestClamp();
		setRunningModules(testClamp);
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
		Timer.delay(0.005);
	}
}
