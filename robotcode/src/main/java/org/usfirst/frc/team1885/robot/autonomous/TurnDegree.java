package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import edu.wpi.first.wpilibj.DriverStation;

public class TurnDegree extends AutonomousCommand {
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private static final double MAX_ERROR = 0.1;
	private static final double PROPORTION = 0.006;
	
	private double degrees;
	private double targetYaw;
	private double error;
	
	public TurnDegree(DriveTrain drivetrain, NavX navx, double degrees)
	{
		this.drivetrain = drivetrain;
		this.navx = navx;
		this.degrees = degrees;
	}
	
	public void init()
	{
		navx.zeroYaw();
		this.targetYaw = (navx.getYaw() + degrees) % 180;
		this.error = (navx.getYaw() - targetYaw) % 180;
		DriverStation.reportError(String.format("Starting TurnDegree. \n Initial Yaw: %f \n Current Yaw: %f \n Target Yaw: %f \n Error: %f", navx.getInitialYaw(), navx.getYaw(), targetYaw, error), false);
	}
	
	public boolean update()
	{
		if(isWithinError(navx.getYaw(), targetYaw, MAX_ERROR)) return true;
		DriverStation.reportError(String.format("Updating TurnDegree. \n Current Yaw: %f \n Target Yaw: %f \n Error: %f", navx.getYaw(), targetYaw, error), false);
		
		double leftPower, rightPower;
		error = ( navx.getYaw() - targetYaw) % 180;
		
		leftPower = (PROPORTION * error);
		rightPower = -(PROPORTION * error);
		
		drivetrain.setMotors(leftPower, rightPower);
		DriverStation.reportError(String.format("Left: %f Right %f", leftPower, rightPower), false );
		
		return false;
	}
	
	private boolean isWithinError(double value, double targetValue, double error)
	{
		if( value < (targetValue + error) && value > (targetValue - error) ) return true;
		DriverStation.reportError(String.format("Value: %f Target: %f Allowable Error: %f", value, targetValue, error), false);
		return false;
	}
	
}
