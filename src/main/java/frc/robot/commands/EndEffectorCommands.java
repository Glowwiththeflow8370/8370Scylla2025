package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.endeffector.intake.Intake;
import frc.robot.subsystems.endeffector.wrist.Wrist;
import frc.robot.util.Setpoints;

public class EndEffectorCommands {
  public EndEffectorCommands() {}

  // Wrist commands

  public static Command StopWrist(Wrist wrist) {
    return Commands.run(
        () -> {
          wrist.StopWrist();
        },
        wrist);
  }

  // Manual control (Wrist)
  public static Command RotateWrist(Wrist wrist, double value) {
    return Commands.run(
        () -> {
          wrist.RotateWrist(value);
        },
        wrist);
  }

  // Rotate To position (Wrist)
  public static Command WristToPosition(Wrist wrist, Setpoints setpoint) {
    return Commands.run(
        () -> {
          if (wrist.GetAngle() < setpoint.getWristPos()) {
            wrist.RotateWrist(0.1);
          }
          if (wrist.GetAngle() > setpoint.getWristPos()) {
            wrist.RotateWrist(-0.1);
          } else {
            wrist.RotateWrist(0);
          }
        },
        wrist);
  }

  // Intake commands (should be configured to run
  // both in auto and teleop)
  public static Command RunIntake(Intake intake, double value) {
    return Commands.run(
        () -> {
          intake.RunIntake(value);
        },
        intake);
  }

  public static Command StopIntake(Intake intake) {
    return Commands.run(
        () -> {
          intake.StopIntake();
        },
        intake);
  }
}
