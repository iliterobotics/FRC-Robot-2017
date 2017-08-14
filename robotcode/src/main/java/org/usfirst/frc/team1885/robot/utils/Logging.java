	package org.usfirst.frc.team1885.robot.utils;

	import java.io.BufferedWriter;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;

	import org.usfirst.frc.team1885.robot.modules.DriveTrain;
	import org.usfirst.frc.team1885.robot.modules.NavX;

	public class Logging
	{

		private static final String FILENAME = "/home/lvuser/katelog.txt";
		private DriveTrain drivetrain;
		private NavX nav;

		public Logging(DriveTrain drivetrain, NavX nav)
		{
			this.drivetrain = drivetrain;
			this.nav = nav;
		}
			
		public void logData()
		{
			BufferedWriter bw = null;
			FileWriter fw = null;
			List<String> valueList = new ArrayList<String>();
			valueList.add("Left Velocity: " + drivetrain.getLeftEncoderVelocity()+" ");
			valueList.add("Right Velocity: " + drivetrain.getRightEncoderVelocity()+" ");
			valueList.add("Yaw: " + nav.getYaw()+" ");
			
			try {
				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				
				for(int x = 0; x < valueList.size(); x++)
				{
				bw.write(valueList.get(x) + "\n");
				}
				System.out.println("Done");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

		}
	}

	

