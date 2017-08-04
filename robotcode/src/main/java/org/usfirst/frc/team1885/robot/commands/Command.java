package org.usfirst.frc.team1885.robot.commands;

public abstract class Command {
	/**
	 * Method calls needed to initialize the command go here.
	 */
	public abstract void init();
	/**
	 * 
	 * @return True to terminate execution and move on to the next command, or false to keep running the current command.
	 */
	public abstract boolean update();
}
