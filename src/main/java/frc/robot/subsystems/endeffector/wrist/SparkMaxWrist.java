// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endeffector.wrist;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class SparkMaxWrist implements WristBase {
  
  private SparkMax WristMotor;
  private SparkMaxConfig WristMotorConfig;
  
  /** Creates a new SparkMaxWrist. */
  public SparkMaxWrist() {
    WristMotor = new SparkMax(WristConstants.WristMotorID, MotorType.kBrushless);

    WristMotorConfig = new SparkMaxConfig();
    WristMotorConfig.idleMode(IdleMode.kBrake);

    WristMotor.configure(WristMotorConfig, 
    ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  }

  @Override
  public void rotateWrist(double value) {
    WristMotor.set(value);
  }

  @Override
  public void stopWrist() {
    WristMotor.set(0);
  }

  @Override
  public double getWristAngle() {
    return 0.0;
  }
}
