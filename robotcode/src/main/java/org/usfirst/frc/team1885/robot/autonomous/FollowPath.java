package org.usfirst.frc.team1885.robot.autonomous;

import org.usfirst.frc.team1885.robot.modules.DriveTrain;
import org.usfirst.frc.team1885.robot.modules.NavX;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {
	
	public static final FitMethod FIT_METHOD = FitMethod.HERMITE_QUINTIC;
	public static final int SAMPLES = 0;
	public static final double DELTA_TIME = 0;
	public static final double MAX_VEL = 0;
	public static final double MAX_ACC = 0;
	public static final double MAX_JERK = 0;
	
	private DriveTrain drivetrain;
	private NavX navx;
	
	private Config config;
	private TankModifier tankModifier;
	private Trajectory trajectory;
	private EncoderFollower leftFollower, rightFollower;
	
	double actualHeading, desiredHeading, headingError;
	double leftProfileOutput, rightProfileOutput, turn;
	
	public FollowPath(DriveTrain drivetrain, NavX navx) {
		this.drivetrain = drivetrain;
		this.navx = navx;
		
		config = new Config(FIT_METHOD, SAMPLES, DELTA_TIME, MAX_VEL, MAX_ACC, MAX_JERK);
		trajectory = Pathfinder.generate(getWaypoints(), config);
		
		tankModifier = new TankModifier(trajectory);
		tankModifier.modify(DriveTrain.EFFECTIVE_WHEELBASE);
		
		leftFollower = new EncoderFollower(tankModifier.getLeftTrajectory());
		leftFollower.configurePIDVA(DriveTrain.kP_VELOCITY, DriveTrain.kI_VELOCITY, DriveTrain.kD_VELOCITY, DriveTrain.kV, DriveTrain.kA);
		
		rightFollower = new EncoderFollower(tankModifier.getRightTrajectory());
		rightFollower.configurePIDVA(DriveTrain.kP_VELOCITY, DriveTrain.kI_VELOCITY, DriveTrain.kD_VELOCITY, DriveTrain.kV, DriveTrain.kA);
		
	}
	
	public void init() {
		leftFollower.configureEncoder(drivetrain.getLeftPosition(), DriveTrain.ENC_TICKS_PER_TURN, DriveTrain.WHEEL_DIAMETER);
		rightFollower.configureEncoder(drivetrain.getRightPosition(), DriveTrain.ENC_TICKS_PER_TURN, DriveTrain.WHEEL_DIAMETER);
	}
	
	public boolean update() {
		if(leftFollower.isFinished() && rightFollower.isFinished()) return true;
		
		leftProfileOutput = leftFollower.calculate(drivetrain.getLeftPosition());
		rightProfileOutput = rightFollower.calculate(drivetrain.getRightPosition());
		
		actualHeading = navx.getAngle();
		desiredHeading = Pathfinder.r2d(leftFollower.getHeading()); //Only need to use 1 side because both sides are parallel
		headingError = Pathfinder.boundHalfDegrees(desiredHeading - actualHeading);
		turn = DriveTrain.kP_ANGLE_ERROR * headingError;
		
		drivetrain.setSpeed(leftProfileOutput + turn, rightProfileOutput - turn);
		
		return false;
	}
	
	private Waypoint[] getWaypoints() {
		return new Waypoint[] {new Waypoint(0, 0, 0), new Waypoint(0, 10, 0)};
	}
	
}
