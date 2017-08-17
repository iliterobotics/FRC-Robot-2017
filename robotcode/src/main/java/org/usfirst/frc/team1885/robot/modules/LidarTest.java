package org.usfirst.frc.team1885.robot.modules;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Timer;

public class LidarTest implements Module {
	
		private I2C i2c;
		private byte[] distance;
		private static final Port PORT_TYPE = Port.kMXP;
		private final int LIDAR_ADDR = 0x62;
		private final int LIDAR_CONFIG_REGISTER = 0x00;
		private final int LIDAR_DISTANCE_REGISTER = 0x8f;
		

		public LidarTest()
		{
			i2c = new I2C(PORT_TYPE, LIDAR_ADDR);

			distance = new byte[2];
		}
		



		public int getDistance()
		{
			int x = (int) Integer.toUnsignedLong(distance[0] << 8) + Byte.toUnsignedInt(distance[1]);
			System.out.println(x);
			return x;
		}

		public void update()
		{
			
				i2c.write(LIDAR_CONFIG_REGISTER, 0x04);
				Timer.delay(0.04);
				i2c.read(LIDAR_DISTANCE_REGISTER, 2, distance);
				Timer.delay(0.005);
				
		}

		
		

		@Override
		public void initialize() {
			
			
		}

	
}
