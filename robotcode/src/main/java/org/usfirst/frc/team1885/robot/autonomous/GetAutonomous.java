package org.usfirst.frc.team1885.robot.autonomous;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.coms.ConstantUpdater;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.GearManipulator;
import org.usfirst.frc.team1885.robot.modules.NavX;

public class GetAutonomous extends Command{

	private String position;
	
	@Override
	public void init() {
		
	}

	@Override
	public boolean update() {
		String position = ConstantUpdater.getNetworkTablesString("position");
		System.out.println("OUR CONFIG IS " + position);
		return true;
	}
	
	public List<Command> getAutonomous(DriveTrain driveTrain, GearManipulator gearManipulator, NavX navx){
		List<Command> commands = new ArrayList<>();
		
		if(position == null) position = "center";
		switch(position){
		case "left":
			commands.add(new DriveStraightDistance(driveTrain, navx, 90));
			commands.add(new TurnToDegree(driveTrain, navx, 60, 5));
			commands.add(new DriveStraightVision(driveTrain, navx, 15));
			commands.add(new DropOffGear(gearManipulator, driveTrain));
			commands.add(new TurnToDegree(driveTrain, navx, -10, 20));
			commands.add(new DriveStraightDistance(driveTrain, navx, 48));
			break;
		case "center":
			break;
		case "right":
			commands.add(new DriveStraightDistance(driveTrain, navx, 90));
			commands.add(new TurnToDegree(driveTrain, navx, -60, 5));
			commands.add(new DriveStraightVision(driveTrain, navx, 15));
			commands.add(new DropOffGear(gearManipulator, driveTrain));
			commands.add(new TurnToDegree(driveTrain, navx, 10, 20));
			commands.add(new DriveStraightDistance(driveTrain, navx, 48));
			break;
		default:
			commands.clear();
			break;
		}
		
		return commands;
	}

}
