package org.usfirst.frc.team1885.robot.autonomous;

import java.util.List;

import org.usfirst.frc.team1885.coms.ConstantUpdater;

public class GetAutonomous extends Command{

	@Override
	public void init() {
		
	}

	@Override
	public boolean update() {
		String position = ConstantUpdater.getNetworkTablesString("");
		return true;
	}
	
	public List<Command> getAutonomous(){
		return null;
	}

}
