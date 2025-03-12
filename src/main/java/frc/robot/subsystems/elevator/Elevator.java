// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {

  ElevatorBase elevator;

  SysIdRoutine sysId;

  LoggedTunableNumber ElevatorAngle;

  /** Creates a new Elevator. */
  public Elevator(ElevatorBase elevator) {
    this.elevator = elevator;

    // Configure SysId (not yet used)
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Elevator/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> runOpenLoop(voltage.in(Volts)), null, this));
  }

  public void runOpenLoop(double Volts) {
    elevator.setVoltage(Volts);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("elevator angle", elevator.getElevatorEncoderValues());
  }

  public void RunElevator(double value) {
    elevator.runElevator(value);
  }

  public double getEncoderValues() {
    return elevator.getElevatorEncoderValues();
  }

  public void StopElevator() {
    elevator.stopElevator();
  }
}
