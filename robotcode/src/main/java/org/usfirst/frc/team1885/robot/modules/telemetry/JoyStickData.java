package org.usfirst.frc.team1885.robot.modules.telemetry;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1885.robot.modules.Module;

import edu.wpi.first.wpilibj.Joystick;

public class JoyStickData implements Module{

	private Joystick joystick;
	private int numButtons;
	private int numAxis;
	private List<Boolean> buttons = new ArrayList<Boolean>();
	private List<Double> axis = new ArrayList<Double>();
	
	public JoyStickData(){
		
		numButtons = joystick.getButtonCount();
		numAxis = joystick.getAxisCount();
	}

	public void initialize() {
		joystick = new Joystick(0);
		buttons.set(0, false);
		for (int i = 1; i <= numButtons; i++)
		{
			buttons.set(i, false);
		}
		for (int i = 0; i < numAxis; i++)
		{
			axis.set(i, 0.0);
		}
	}
	
	public void update() {
		for (int i = 1; i <= numButtons; i++)
		{
			buttons.set(i, joystick.getRawButton(i));
		}
		for (int i = 0; i < numAxis; i++)
		{
			axis.set(i, joystick.getRawAxis(i));
		}
	}
	public boolean getButtons(int slot) {
		return buttons.get(slot);
	}
	public double getAxis(int slot) {
		return axis.get(slot);
	}
}
