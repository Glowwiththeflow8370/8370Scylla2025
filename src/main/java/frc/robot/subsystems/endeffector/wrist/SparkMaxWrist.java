// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.wrist;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class SparkMaxWrist implements WristBase {

  private SparkMax WristMotor;
  private SparkMaxConfig WristMotorConfig;
  AbsoluteEncoder WristEncoder;

  /** Creates a new SparkMaxWrist. */
  public SparkMaxWrist() {
    WristMotor = new SparkMax(WristConstants.WristMotorID, MotorType.kBrushless);

    WristEncoder = WristMotor.getAbsoluteEncoder();

    WristMotorConfig = new SparkMaxConfig();
    WristMotorConfig.idleMode(IdleMode.kBrake);

    WristMotor.configure(
        WristMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void rotateWrist(double value) {
    WristMotor.set(value);
  }

  @Override
  public void stopWrist() {
    System.out.println("wrist stopping");
    WristMotor.set(0);
  }

  @Override
  public double getWristAngle() {
    return WristEncoder.getPosition() * 360;
  }
}
