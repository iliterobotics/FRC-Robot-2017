package org.usfirst.frc.team1885.robot.commands;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.coms.ConstantUpdater;
import org.usfirst.frc.team1885.robot.modules.DriveTrain;
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
	
	public List<Command> getAutonomous(DriveTrain driveTrain, NavX navx){
		List<Command> commands = new ArrayList<>();

		update();
		commands.clear();
		System.out.println("POSITION IS " + position);
		if(position == null || position.equals("center")) position = "left";
		switch(position){
		case "left":
			commands.add(new TurnToDegree(driveTrain, navx, 90, 3));
			commands.add(new TurnToDegree(driveTrain, navx, -90, 3));
			commands.add(new TurnToDegree(driveTrain, navx, 180, 3));
			commands.add(new TurnToDegree(driveTrain, navx, -180, 3));
			break;
		case "right":
			commands.add(new DriveStraightDistance(driveTrain, navx, 92));
			commands.add(new TurnToDegree(driveTrain, navx, -60, 3));
			commands.add(new DriveStraightDistance(driveTrain, navx, 15));
			commands.add(new TurnToDegree(driveTrain, navx, 10, 20));
			commands.add(new DriveStraightDistance(driveTrain, navx, 224));
			break;
		default:
			commands.clear();
			break;
		}
		
		return commands;
	}

}
