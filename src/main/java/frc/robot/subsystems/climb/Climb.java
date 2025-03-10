// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {

  private ClimbBase climb;

  /** Creates a new Climb. */
  public Climb(ClimbBase climb) {
    this.climb = climb;
  }

  public void runClimb() {
    climb.runClimb(0.15);
  }

  public void stopClimb() {
    climb.stopClimb();
  }

  public double getAngle() {
    return climb.getClimbAngle();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
