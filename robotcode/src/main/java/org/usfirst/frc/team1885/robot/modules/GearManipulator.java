package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1885.coms.ConstantUpdater;

import com.ctre.MotorControl.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SolenoidBase;

public class GearManipulator implements Module{

	private static final int INTAKE_CAN_ID = 9;
	private static final int SECONDAY_CAN_ID = 10;
	public static final double DEFAULT_INTAKE_SPEED = 1.0;
	
	public static final double MAX_INTAKE_AMPERAGE = 60;
	
	private Map<PistonType, SolenoidBase> pistonMap;
	
	private static final double BAR_WAIT = 100;
	
	private boolean isLong;
	private boolean isShort;
	private boolean isKicked;
	private boolean isDown;
	private boolean isDropping;
	private double intakePower;
	
	private CANTalon intakeWheels;
	private CANTalon intakeBar;
	
	private long initBarOpen;
	
	private boolean hasIntakeHitLimit;
	private boolean barOpening;
	private boolean goingDown;
	
	private enum PistonType {
		INTAKE(0, true), LONG_DOOR(5, false), SHORT_DOOR(4, false), KICKER(6, false), DROPPER(3, false);
		
		public final int port;
		public final boolean isDouble;
		
		PistonType(int port, boolean isDouble){
			this.port = port;
			this.isDouble = isDouble;
		}
	}
	
	public GearManipulator(){
		pistonMap = new HashMap<>();
		for(PistonType type : PistonType.values()) {
			SolenoidBase solenoid;
			if(type.isDouble){
				solenoid = new DoubleSolenoid(type.port, type.port + 1);
			}
			else{
				solenoid = new Solenoid(type.port);
			}
			pistonMap.put(type, solenoid);
		}
		intakeWheels = new CANTalon(INTAKE_CAN_ID);
		intakeBar = new CANTalon(SECONDAY_CAN_ID);
		intakeWheels.setControlMode(CANTalon.TalonControlMode.PercentVbus.value);
		intakeBar.setControlMode(CANTalon.TalonControlMode.PercentVbus.value);
	}	

	@Override
	public void initialize() {
		isDropping = 
		isLong = 
		isShort =
		isKicked = false;
		isDown = false;
		ConstantUpdater.putNumber("intake_current", 0);
	}
	

	public void setLong(boolean longdown){
		isLong = longdown;
	}

	public void setShort(boolean shortdown){
		isShort = shortdown;
	}
	
	public void setKick(boolean kicked){
		if(!(isLong && isShort) && kicked){
			kicked = false;
		}
		isKicked = kicked;
	}
	
	public void setLowered(boolean lowered){
		if(!lowered && (isShort || isLong || isDropping)){
			lowered = false;
		}
		if(lowered && (isShort || isLong)){
			lowered = true;
		}
		
		if(lowered){
			goingDown = true;
		}else{
			isDown = false;
		}
	}
	
	public void setDropping(boolean drop){
		if(!drop){
			isDropping = false;
		} else{
		}
	}
	
	public void setIntakeSpeed(double power){
		this.intakePower = power;
	}
	
	public boolean getDropping(){
		return isDropping;
	}
	
	public boolean isShort(){
		return isShort;
	}
	
	public boolean isLong(){
		return isLong;
	}

	public boolean isDown(){
		return isDown;
	}
	
	public boolean isKicked(){
		return isKicked;
	}
	
	public boolean isStalled(){
		return hasIntakeHitLimit;
	}
	
	public boolean isDropping() {
		return isDropping;
	}
	
	@Override
	public void update() {
		setSingleSolenoid(PistonType.LONG_DOOR, isLong);
		setSingleSolenoid(PistonType.SHORT_DOOR, isShort);
		setSingleSolenoid(PistonType.KICKER, isKicked);
		setSingleSolenoid(PistonType.DROPPER, isDropping);
		
		if(goingDown){
			if(!barOpening){
				isDropping = true;
				barOpening = true;
				initBarOpen = System.currentTimeMillis();
			}
			if(System.currentTimeMillis() - initBarOpen >= BAR_WAIT){
				isDown = true;
				barOpening = false;
				goingDown = false;
			}
		}
		
		setDoubleSolenoid(PistonType.INTAKE, isDown ? Value.kReverse : Value.kForward);
		
		//Check current limit
		if(intakePower == 0) hasIntakeHitLimit = false;
		
		intakeWheels.set(hasIntakeHitLimit?0:intakePower);
		intakeBar.set(hasIntakeHitLimit?0:intakePower);

		if(intakeWheels.getOutputCurrent() >= MAX_INTAKE_AMPERAGE){
			hasIntakeHitLimit = true;
		}
		
		ConstantUpdater.putNumber("intake_current", intakeWheels.getOutputCurrent());
	}
	
	public void setSingleSolenoid(PistonType type, boolean open){
		SolenoidBase base = pistonMap.get(type);
		if(base instanceof Solenoid){
			((Solenoid)base).set(open);
		}else{
			DriverStation.reportError("WRONG SOLENOID TYPE", false);
		}
	}
	
	public void setDoubleSolenoid(PistonType type, DoubleSolenoid.Value value){
		SolenoidBase base = pistonMap.get(type);
		if(base instanceof DoubleSolenoid){
			((DoubleSolenoid)base).set(value);
		}else{
			DriverStation.reportError("WRONG SOLENOID TYPE", false);
		}
	}
	
}
