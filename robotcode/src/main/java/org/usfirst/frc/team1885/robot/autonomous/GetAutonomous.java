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
		update();
	}

	@Override
	public boolean update() {
		try{
			position = ConstantUpdater.getNetworkTablesString("position");
		} catch(Exception e){
			
		}
		return true;
	}
	
	public List<Command> getAutonomous(DriveTrain driveTrain, GearManipulator gearManipulator, NavX navx){
		List<Command> commands = new ArrayList<>();

		update();
		commands.clear();
		System.out.println("POSITION IS " + position);
		if(position == null || position.equals("center")) position = "left";
		switch(position){
		case "left":
			commands.add(new DriveStraightDistance(driveTrain, navx, 90));
			commands.add(new TurnToDegree(driveTrain, navx, 65, 10));
			commands.add(new DriveStraightDistance(driveTrain, navx, 15));
			commands.add(new DropOffGear(gearManipulator, driveTrain));
			commands.add(new TurnToDegree(driveTrain, navx, -10, 20));
			commands.add(new DriveStraightDistance(driveTrain, navx, 192));
			break;
		case "right":
			commands.add(new DriveStraightDistance(driveTrain, navx, 87));
			commands.add(new TurnToDegree(driveTrain, navx, -65, 10));
			commands.add(new DriveStraightDistance(driveTrain, navx, 15));
			commands.add(new DropOffGear(gearManipulator, driveTrain));
			commands.add(new TurnToDegree(driveTrain, navx, 10, 20));
			commands.add(new DriveStraightDistance(driveTrain, navx, 192));
			break;
		default:
			commands.clear();
			break;
		}
		
		return commands;
	}

}
