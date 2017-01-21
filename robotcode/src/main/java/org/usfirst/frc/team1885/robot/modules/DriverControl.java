package org.usfirst.frc.team1885.robot.modules;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class DriverControl implements Module{

	private Map<ControllerType, Joystick> joyStickMap;
	
	private final DriveTrain driveTrain;
	
	private enum ControllerType{
		LEFT_STICK(0), RIGHT_STICK(1), CONTROLLER(2);

		final int controllerId;
		ControllerType(int id){
			controllerId = id;
		}
	}

	public DriverControl(DriveTrain driveTrain){
		this.driveTrain = driveTrain;
		joyStickMap = new HashMap<ControllerType, Joystick>();
	}
	
	public void initialize() {
		for(ControllerType type : ControllerType.values()){
			joyStickMap.put(type, new Joystick(type.controllerId));
		}
		driveTrain.setMode(DriveTrain.DriveMode.DRIVER_CONTROL_LOW);
	}

	public void update() {
		driveTrain.setSpeeds(-(float)joyStickMap.get(ControllerType.LEFT_STICK).getAxis(AxisType.kY), (float)joyStickMap.get(ControllerType.RIGHT_STICK).getAxis(AxisType.kY));
	}

}
