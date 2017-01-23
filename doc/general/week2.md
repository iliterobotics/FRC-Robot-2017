# Week 2 General Notes

## Driving straight autonomously

### With Encoders

Our initial approach to driving autonomous was to use the encoder velocities combined with periodic power adjustment to ensure that both encoders were going the same speed. This proved to be an inefficient way to drive straight because the encoders being attached to a complex physical system introduces many opportunities for error. Our code was able to create equal velocities in both encoders, but this produced a noticeable veer to the right. We concluded that using encoders in this way was not worthwhile moving forward.

### With the NavX breakout board

The *NavX* board allows us to use gyroscopic measurements to determine our alignment more accurately and disregarding any physical variance between both sides of the drive train. We use a configured multiplier on the variance in degrees to adjust the power output on either side of the drive train.

```
degreeVariance = intialDegreeMeasurement - currentDegreeMeasurement

leftOutput = baseOutput + (degreeVariance * PROPORTION)
rightOutput = baseOutput - (degreeVariance * PROPORTION)
```

## Gradle structure change (multi-project)

The structure of our main project was altered this week from a single Gradle project to a *multi-project* setup. This means that we can have one git repository for all of our java projects and simple construct multiple hierarchical Gradle projects within the **superproject** so that each project can depend on and implement one another. The major hurdle of this change was configuring *GradleRIO* correctly in this multi-project setup, but thankfully the makers of *GradleRIO* have an example setup (here)[https://github.com/Open-RIO/GradleRIO/tree/master/examples/multi_project] which we based our own off of.

> #### Note
>
> The most important thing to keep in mind is that the **superproject** must have *GradleRIO* as a plugin but not actually apply it. This is done with the following syntax.
>
> ```
> plugins {
>   ...
>   id "jaci.openrio.gradle.GradleRIO" version "2017.1.0" apply false
>   ...
> }
> ```
>

## Driver station design

Due to issues with our team's bandwidth we took on the job of designing the driver station which will hold the driver station laptop and controllers during the match. The assembly represents a sheet of metal serving as the base and a sheet of poly-carbonate for the upper layer. The purpose of this is to ensure that the driver station remains bottom-heavy so that it can handle being rocked around during matches. The design includes a gap cavity for securing the driver station laptop, space to velcro on the *flight-sticks* and a c-shaped bracket for holding the *game-pad*. While this assignment is not normally taken by the programming team, we had a member who was trained in **Autodesk** and was able to complete the task without infringing on the progression of development. Additionally, because in the *pit* at competition code tests and configurations are done on the driver-station laptop, usually done by one of the lead programmers or a lead programmer in training, it is important that the design of the driver station allows for easy access to the laptop and its peripherals at all times, and a design developed by the programmers themselves ensures this is the case.
