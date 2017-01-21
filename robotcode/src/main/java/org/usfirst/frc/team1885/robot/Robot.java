
package org.usfirst.frc.team1885.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.DriverControl;
import org.usfirst.frc.team1885.robot.modules.Module;

import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	
	public static final long UPDATE_PERIOD = 5;

	private DriveTrain driveTrain;
	private DriverControl driverControl;
	
	private List<Module> runningModules;
	
	public Robot(){
		runningModules = new ArrayList<>();
		
		driveTrain = new DriveTrain();
		driverControl = new DriverControl(driveTrain);
	}

	public void robotInit(){
		
	}
	
	public void autonomous()
	{
		setRunningModules();
		while(isAutonomous()){
			updateModules();
			pause();
		}
	}
	
	public void operatorControl()
	{
		setRunningModules(driverControl, driveTrain);
		while(isOperatorControl()){
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
		try {
			Thread.sleep(UPDATE_PERIOD);
		} catch (InterruptedException e) {
		}
	}
}
