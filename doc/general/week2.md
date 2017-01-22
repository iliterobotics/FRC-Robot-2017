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
