package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SolenoidBase;

public class GearManipulator implements Module{

	private static final int FLY_CAN_ID = 7;
	
	private Map<PistonType, SolenoidBase> pistonMap;
	
	private boolean isOpen;
	private boolean isTilted;
	private boolean isKicked;
	private boolean isUp;
	private boolean isDropping;
	private double intakePower;
	
	private CANTalon intakeWheels;
	
	private enum PistonType {
		DOOR_DOWN(0, false), DOOR_TIPPER(1, false), KICKER(2, false), INTAKE(3, true), DROPPER(5, false);
		
		public final int port;
		public final boolean isDouble;
		
		PistonType(int port, boolean isDouble){
			this.port = port;
			this.isDouble = isDouble;
		}
	}
	
	public GearManipulator(){
		pistonMap = new HashMap<>();
	}	

	@Override
	public void initialize() {
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
		intakeWheels = new CANTalon(FLY_CAN_ID);
		intakeWheels.setControlMode(CANTalon.TalonControlMode.PercentVbus.value);
	}
	
	public void setOpen(boolean open){
		this.isOpen = open;
	}
	
	public void setTilted(boolean tilted){
		this.isTilted = tilted;
	}
	
	public void setKick(boolean kicked){
		if(!isOpen && kicked){
			kicked = false;
		}
		isKicked = kicked;
	}
	
	public void setRaised(boolean raised){
		if(!raised && (isOpen || isTilted || isDropping)){
			raised = true;
		}
		if(raised && (isOpen || isTilted)){
			raised = false;
		}
		isUp = raised;
	}
	
	public void setDropping(boolean drop){
		if(drop && (!isUp || !isTilted)){
			drop = false;
		}
		isDropping = drop;
	}
	
	public void setIntakeSpeed(double power){
		this.intakePower = power;
	}

	@Override
	public void update() {
		setSingleSolenoid(PistonType.DOOR_DOWN, isOpen);
		setSingleSolenoid(PistonType.DOOR_TIPPER, isTilted || isOpen);
		setSingleSolenoid(PistonType.KICKER, isKicked);
		setSingleSolenoid(PistonType.DROPPER, isDropping);
		setDoubleSolenoid(PistonType.INTAKE, isUp?Value.kReverse:Value.kForward);
		intakeWheels.set(intakePower);
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
