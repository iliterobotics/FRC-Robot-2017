package org.usfirst.frc.team1885.robot.common.impl;

import org.usfirst.frc.team1885.robot.common.impl.converters.FeedbackConverter;
import org.usfirst.frc.team1885.robot.common.impl.converters.TalonControlModeConverter;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalon;
import org.usfirst.frc.team1885.robot.common.interfaces.ICanTalonFactory;

import com.ctre.MotorControl.CANTalon;
import com.ctre.MotorControl.SmartMotorController.FeedbackDevice;
import com.ctre.MotorControl.SmartMotorController.TalonControlMode;

public class DefaultCanTalonFactory implements ICanTalonFactory {

	@Override
	public ICanTalon getCanTalon(int id) {
		return new CanTalonImpl(new CANTalon(id));
	}
	
	private class CanTalonImpl implements ICanTalon { 
		private  final CANTalon canTalon;

		public CanTalonImpl(CANTalon canTalon) {
			this.canTalon = canTalon;
		}

		@Override
		public void setEncPosition(int i) {
			this.canTalon.setEncPosition(i);
			
		}

		@Override
		public void setP(double d) {
			this.canTalon.setP(d);
			
		}
		@Override
		public double getD() {
			return this.canTalon.getD();
		}
		@Override
		public double getI() {
			return this.canTalon.getI();
		}
		
		@Override
		public double getP() {
			return this.canTalon.getP();
		}

		@Override
		public void setControlMode(ETalonControlMode follower) {
			TalonControlMode modeToSet = TalonControlModeConverter.getConverter().convert(follower);
			
			if(modeToSet != null) { 
				this.canTalon.setControlMode(modeToSet.value);
			}
		}

		@Override
		public void set(int talonId) {
			this.canTalon.set(talonId);
			
		}

		@Override
		public int getEncVelocity() {
			return this.canTalon.getEncVelocity();
		}

		@Override
		public void set(double d) {
			this.canTalon.set(d);
			
		}

		@Override
		public ETalonControlMode getControlMode() {
			
			TalonControlMode controlMode = this.canTalon.getControlMode();
			ETalonControlMode convert = TalonControlModeConverter.getConverter().reverse().convert(controlMode);
			
			return convert;
		}
		
		@Override
		public void setFeedbackDevice(EFeedbackDevice analogencoder) {
			FeedbackDevice convert = FeedbackConverter.getConverter().reverse().convert(analogencoder);
			this.canTalon.setFeedbackDevice(convert);
			
		}

		@Override
		public void setVoltageRampRate(double rate) {
			this.canTalon.setVoltageRampRate(rate);
		}

		@Override
		public int getEncPosition() {
			return this.canTalon.getEncPosition();
		}
	}
	
	

}
