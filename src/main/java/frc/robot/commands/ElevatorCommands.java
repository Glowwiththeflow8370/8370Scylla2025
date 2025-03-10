package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.elevator.Elevator;

public class ElevatorCommands {
  public ElevatorCommands() {}

  // default elevator command
  public static Command StopElevator(Elevator elevator) {
    return Commands.run(
        () -> {
          elevator.StopElevator();
        },
        elevator);
  }

  public static Command RunElevator(Elevator elevator, double speed) {
    return Commands.run(
        () -> {
          elevator.RunElevator(0.15);
        },
        elevator);
  }

  public static Command ElevateToPosition(Elevator elevator) {
    return Commands.runOnce(() -> {}, elevator);
  }
}
