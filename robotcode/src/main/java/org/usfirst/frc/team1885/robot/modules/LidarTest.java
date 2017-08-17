package org.usfirst.frc.team1885.robot.modules;
import java.util.TimerTask;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Timer;

public class LidarTest {
	
		private I2C i2c;
		private byte[] distance;

		private final int LIDAR_ADDR = 0x62;
		private final int LIDAR_CONFIG_REGISTER = 0x00;
		private final int LIDAR_DISTANCE_REGISTER = 0x8f;
		private java.util.Timer updater;

		private long lastUpdateTime;

		public LidarTest(Port port)
		{
			i2c = new I2C(port, LIDAR_ADDR);

			distance = new byte[2];

			lastUpdateTime = System.currentTimeMillis();
			updater = new java.util.Timer();
		}

		public void start()
		{
			updater.scheduleAtFixedRate(new LIDARUpdater(), 0, 100);
		}

		public void start(int period)
		{
			updater.scheduleAtFixedRate(new LIDARUpdater(), 0, period);
		}

		public void stop()
		{
			updater.cancel();
			updater = new java.util.Timer();
		}

		public long updateTime()
		{
			return(System.currentTimeMillis() - lastUpdateTime);
		}

		public int getDistance()
		{
			return (int) Integer.toUnsignedLong(distance[0] << 8) + Byte.toUnsignedInt(distance[1]);
		}

		public boolean updateDistance()
		{
			if(System.currentTimeMillis() - lastUpdateTime >= 100)
			{
				i2c.write(LIDAR_CONFIG_REGISTER, 0x04);
				Timer.delay(0.04);
				i2c.read(LIDAR_DISTANCE_REGISTER, 2, distance);
				Timer.delay(0.005);
				lastUpdateTime = System.currentTimeMillis();
				return true;
			}
			return false;
		}

		private class LIDARUpdater extends TimerTask
		{
			@Override
			public void run()
			{
				while(true)
				{
					updateDistance();
					try
					{
						Thread.sleep(10);
					} catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

	
}
