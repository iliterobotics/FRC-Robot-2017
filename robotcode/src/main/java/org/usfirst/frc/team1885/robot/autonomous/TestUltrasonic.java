package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.UltraSonic;

public class TestUltrasonic extends AutonomousCommand{

	private final UltraSonic ultraSonic;
	
	public TestUltrasonic(UltraSonic us){
		ultraSonic = us;
	}
	
	public void init() {
	}

	@Override
	public boolean update() {
		System.out.println(ultraSonic.getMM() + "");
		return false;
	}

}
