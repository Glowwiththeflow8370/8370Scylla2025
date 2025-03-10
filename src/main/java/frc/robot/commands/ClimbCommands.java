package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.climb.Climb;

public class ClimbCommands {
  public ClimbCommands() {}

  // Default Climb command (stops the climb)
  public static Command StopClimb(Climb climb) {
    return Commands.run(
        () -> {
          climb.stopClimb();
        },
        climb);
  }

  // Manual Climb command
  public static Command RunClimb(Climb climb, double value) {
    return Commands.run(
        () -> {
          climb.runClimb();
        },
        climb);
  }

  // Climb to position
  public static Command ClimbToPosition(Climb climb, double position) {
    return Commands.run(
        () -> {
          // It has yet to be implemented : P
        },
        climb);
  }
}
