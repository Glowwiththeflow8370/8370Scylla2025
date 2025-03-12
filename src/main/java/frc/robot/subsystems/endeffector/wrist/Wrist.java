// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.wrist;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {

  WristBase wrist;

  /** Creates a new Wrist. */
  public Wrist(WristBase wrist) {
    this.wrist = wrist;
  }

  public void RotateWrist(double value) {
    wrist.rotateWrist(value);
  }

  public void StopWrist() {
    wrist.stopWrist();
  }

  public double GetAngle() {
    return 0.0;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Wrist angle", wrist.getWristAngle());
  }
}
