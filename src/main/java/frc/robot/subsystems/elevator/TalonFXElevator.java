// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;

public class TalonFXElevator implements ElevatorBase {

  TalonFX elevatorMotor, elevatorMotorFollower;
  TalonFXConfiguration elevatorMotorConfigs;

  StatusSignal<Angle> elevatorEncoder;
  double elevatorEncoderValue;

  VoltageOut voltageRequest;

  public TalonFXElevator() {
    elevatorMotor = new TalonFX(ElevatorConstants.elevatorMotorID);
    elevatorMotorFollower = new TalonFX(ElevatorConstants.elevatorMotorFolowerID);

    elevatorMotorConfigs = new TalonFXConfiguration();
    elevatorMotorConfigs.CurrentLimits.StatorCurrentLimit = 80;
    elevatorMotorConfigs.CurrentLimits.StatorCurrentLimitEnable = true;

    elevatorMotorFollower.setControl(new Follower(elevatorMotor.getDeviceID(), false));

    elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
    elevatorMotorFollower.setNeutralMode(NeutralModeValue.Brake);

    elevatorEncoder = elevatorMotor.getPosition();
    // Set the starting angle of the elevator
    elevatorEncoderValue = elevatorEncoder.getValueAsDouble();

    voltageRequest = new VoltageOut(0.0);
  }

  // Refer to the ElevatorBase class for more information
  // on the functionality of each method
  @Override
  public void runElevator(double value) {
    elevatorMotor.set(value);
  }

  @Override
  public void stopElevator() {
    elevatorMotor.set(0);
  }

  @Override
  public void setVoltage(double Volts) {
    elevatorMotor.setControl(voltageRequest.withOutput(Volts));
  }

  @Override
  public double getElevatorEncoderValues() {
    return elevatorEncoderValue * 360;
  }
}
