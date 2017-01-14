
package org.usfirst.frc.team1885.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlArcadeControllerTwoStick;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlTank;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlTankController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
	
	public static final long UPDATE_PERIOD = 5;

	private DriveTrain driveTrain;
	private DriverControl driverControl;
	
	private List<Module> runningModules;
	
	public Robot(){
		runningModules = new ArrayList<>();
		
		driveTrain = new DriveTrain();
		driverControl = new DriverControlArcadeControllerTwoStick(driveTrain);
	}

	public void robotInit(){
		DriverStation.reportError("Hello, World!", false);
	}
	
	public void autonomous()
	{
		setRunningModules();
		while(isAutonomous() && isEnabled()){
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
