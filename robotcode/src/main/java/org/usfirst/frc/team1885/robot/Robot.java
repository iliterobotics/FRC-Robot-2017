
package org.usfirst.frc.team1885.robot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.usfirst.frc.team1885.coms.ConstantUpdater;
import org.usfirst.frc.team1885.robot.autonomous.Command;
import org.usfirst.frc.team1885.robot.autonomous.GetAutonomous;
import org.usfirst.frc.team1885.robot.modules.ArduinoController;
import org.usfirst.frc.team1885.robot.modules.BeamSensor;
import org.usfirst.frc.team1885.robot.modules.Climber;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.LEDController;
import org.usfirst.frc.team1885.robot.modules.Module;
import org.usfirst.frc.team1885.robot.modules.NavX;
import org.usfirst.frc.team1885.robot.modules.PressureSensor;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControl;
import org.usfirst.frc.team1885.robot.modules.driverControl.DriverControlArcadeControllerTwoStick;

import edu.wpi.first.wpilibj.CameraServer;
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
	private BeamSensor beamSensor;
	private LEDController ledController;
	private ArduinoController arduinoController;
	private Thread constantUpdaterThread;
	
	private Queue<Command> autonomousCommands;
	private List<Module> runningModules;
	
	public Robot(){
	    	    
		constantUpdaterThread = new Thread(ConstantUpdater.getInstance());
		constantUpdaterThread.start();
		
		runningModules = new ArrayList<>();
		autonomousCommands = new LinkedList<>();
		
		navx = new NavX();
		pressureRegulator = new PressureSensor();
		beamSensor = new BeamSensor();
		
		driveTrain = new DriveTrain();
		gearManipulator = new GearManipulator();
		climber = new Climber();
		arduinoController = new ArduinoController();
		driverControl = new DriverControlArcadeControllerTwoStick(driveTrain, gearManipulator, climber, navx);
		ledController = new LEDController(arduinoController, driveTrain, driverControl, pressureRegulator, beamSensor, climber, gearManipulator);

		navx.resetDisplacement();
	}

	public void robotInit(){
		startCameraFeeds();
		while(navx.isCalibrating()){
			System.out.println("CALIBRATING");
			pause();
		}
		navx.setInitialAngle(navx.getAngle());

	}
	
	public void startCameraFeeds(){
		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture(0);
		server.startAutomaticCapture(1);
		server.startAutomaticCapture(2);
	}
	
	public void autonomous()
	{		
		setRunningModules(driveTrain, gearManipulator, pressureRegulator);
		GetAutonomous getAutonomous = new GetAutonomous();
		getAutonomous.update();
		autonomousCommands.clear();
		autonomousCommands.addAll(getAutonomous.getAutonomous(driveTrain, gearManipulator, navx));
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
		setRunningModules(driverControl, gearManipulator, driveTrain, climber, pressureRegulator, beamSensor, arduinoController, ledController);
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
