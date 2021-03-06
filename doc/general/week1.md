# Week 1 General Notes
---

## Pre Summary

This week was mainly focused on developing a working system for the season, both technically and amongst our members. Team members were assigned the first wave of tasks and a **working code repository was formed**. Our idea going into this week was to by the end have a running drive train as well as a considerable in our other smaller projects such as **LED control**, **Telemetry**, and **Vision**. Additionally, our school planned to host a pep-rally the following Thursday in which we plan to demonstrate our 2016 robot, *IKNIGHT*. This will give us an opportunity to re-familiarize ourselves with the prior year's code structure as well as possibly test some functions which are not yet possible on our **Test Drive Train**.

## Developments

### FRC-Robot-2017

The git repository [FRC-Robot-2017](https://github.com/iliterobotics/FRC-Robot-2017) is the main repository that we will be using this year for our robot's main codebase. The structure developed in this repository contrasts that of prior years in that instead of using *Singleton Dependency Management*, we use a system of *Dependency Injection*. In our system, dependencies, such as modules, are distributed in the constructors of objects which depend on them. This way all dependencies can be instantiated and distributed in the `Robot.java` class and object must simply be written to require all in-project dependencies rather than instantiating them themselves.

Additionally, the new repository will build, deploy, and manage project dependencies using the **Gradle**. This way, all dependencies and project structures can be centralized in one simple `build.gradle`, minimizing setup on new machines and decreasing the complexity of adding additional repositories to our project (Such as the new `TalonSRX.jar` repository introduced this year)

 - `Module` is the interface that all objects which wish to update periodically (~1 every 5ms) must implement. It has two methods
 	- `boolean update()` which runs the modules updating, including calculating and sending outputs
 	- `void init()` is used to initialize the module upon enabling
 - `AutonomousCommand` is the class which encompasses all functions of the autonomous. It has the same abstract methods as `Module`, and for the same reasons

### Driver Controls

Due to the lack of a clear driving strategy or preferred driving method, we were asked to write several different iterations of driver control commands using different combinations of the *flight sticks* and *gamepad*. All were completed with relative ease and are as follows:

 - `DriverControlTank` is identical to our commonly used control scheme where a left and a right *flight stick* control the left and the right side of the drive train respectively
 - `DriverControlTankController` is also a tank control scheme, but instead of using the *flight sticks*, the left and right joysticks on the *gamepad* are used to control either side of the drive-train
 - `DriverControlArcadeOneStick` Is an arcade control scheme which uses a single *flight stick*. Moving the stick forward and backwards drives the wheels directly forward or backward. Moving the stick to either side causes the robot to turn, and moving the stick to either side while not throttled forwards or backwards causes a **point turn**, a maneuver which is infamously difficult to do precisely in a tank-control scheme.
 - `DriverControlArcadeControllerTwoStick` Is also an arcade control scheme, this time using the left joystick on the *gamepad* to throttle speed, and the right joystick to control turning.

> #### The math for determining the output in an arcade system
> `Throttle` describes the vertical position of the left joystick [-1, 1] and turn represents the lateral position of the right joystick [-1, 1]
> ```
> leftoutput = throttle - turn;
> rightoutput = throttle + turn;
> ```


### LED Control
The LED control code was written prior to the start of the season and was ported over to the main project. The system relies on an **Arduino** to control the direct output to the LED strip. an `I2C` connection between the RoboRIO and said Arduino is used to transmit information to the Arduino as to what command it should execute on the strip. The `I2C` messages are simple strings converted to a series of bytes. For example: in testing the `Red` string transition was used to have the Arduino change the color on the strip to red. Every transmission will end in a `;` byte.

Our electronics team constructed us a small test board for experimenting with the LED strips. Due to the unique power parameters required by the Arduino and the LED strip, custom voltage converters are required and this makes an off-robot test system ideal for early testing.
