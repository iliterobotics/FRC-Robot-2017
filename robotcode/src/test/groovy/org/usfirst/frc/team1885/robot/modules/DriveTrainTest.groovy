package org.usfirst.frc.team1885.robot.modules

import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalon
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory
import org.usfirst.frc.team1885.robot.common.interfaces.IDriverStation
import org.usfirst.frc.team1885.robot.modules.DriveTrain.MotorType

import spock.lang.Specification

class DriveTrainTest extends Specification {
	
	def "Test initiaize to ensure motor map is fully intialized without null"() {
		setup:
			ICanTalonFactory canTalonFactory = Mock(ICanTalonFactory.class);
			canTalonFactory.getCanTalon(_) >> Mock(ICanTalon.class);
			IDriverStation driverStation = Mock(IDriverStation.class);
			DriveTrain aTrain = new DriveTrain(canTalonFactory, driverStation);
		when:
			aTrain.initialize();
			
		then: 
			aTrain.motorMap.isEmpty() == false;
			for(MotorType aType : MotorType.values()) {
				aTrain.motorMap.containsKey(aType);
			}
	}

}
