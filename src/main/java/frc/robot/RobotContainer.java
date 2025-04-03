// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbConstants;
import frc.robot.subsystems.climb.SimClimb;
import frc.robot.subsystems.climb.SparkMaxClimb;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DriveIO;
import frc.robot.subsystems.drive.DriveIOSim;
import frc.robot.subsystems.drive.DriveIOTalonFX;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIONavX;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorBase;
import frc.robot.subsystems.elevator.SimElevator;
import frc.robot.subsystems.elevator.TalonFXElevator;
import frc.robot.subsystems.endeffector.intake.Intake;
import frc.robot.subsystems.endeffector.intake.IntakeBase;
import frc.robot.subsystems.endeffector.intake.IntakeConstants;
import frc.robot.subsystems.endeffector.intake.SimIntake;
import frc.robot.subsystems.endeffector.intake.SparkFlexIntake;
import frc.robot.subsystems.endeffector.wrist.SparkMaxWrist;
import frc.robot.subsystems.endeffector.wrist.Wrist;
import frc.robot.util.OperatorConsts;
import frc.robot.util.Setpoints;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  private final Climb climb;
  private final Elevator elevator;
  private final Intake intake;
  private final Wrist wrist;

  // Controller
  private final CommandXboxController controller =
      new CommandXboxController(OperatorConsts.PS4DriverPort);

  private final CommandGenericHID buttonBox = new CommandGenericHID(1);

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        drive = new Drive(new DriveIOTalonFX(), new GyroIONavX());
        climb = new Climb(new SparkMaxClimb());
        elevator = new Elevator(new TalonFXElevator());
        intake = new Intake(new SparkFlexIntake());
        wrist = new Wrist(new SparkMaxWrist());
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive = new Drive(new DriveIOSim(), new GyroIO() {});
        climb = new Climb(new SimClimb());
        elevator = new Elevator(new SimElevator());
        intake = new Intake(new SimIntake());
        wrist = new Wrist(new SparkMaxWrist());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive = new Drive(new DriveIO() {}, new GyroIO() {});
        climb = new Climb(new SparkMaxClimb() {});
        elevator = new Elevator(new ElevatorBase() {});
        intake = new Intake(new IntakeBase() {});
        wrist = new Wrist(new SparkMaxWrist() {});
        break;
    }

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Basic Auto Drive
    autoChooser.addOption(
        "Manual Auto Drive", DriveCommands.BasicDrive(drive, 0.5, 0).withTimeout(0.7));

    // Manual Auto (L1 Score)
    autoChooser.addOption(
        "Manual L1 Auto",
        new SequentialCommandGroup(
            DriveCommands.BasicDrive(drive, 0.5, 0).withTimeout(0.7),
            DriveCommands.BasicDrive(drive, 0, 0).withTimeout(0),
            (wrist.posWrist(wrist, 100.0).alongWith(elevator.posElevator(elevator, 1450.0)))
                .withTimeout(1),
            intake.RunIntakeCommand(intake, () -> IntakeConstants.OutTakeRunValue).withTimeout(3),
            DriveCommands.BasicDrive(drive, -0.2, 0).withTimeout(0.5)));
    // Manual Auto (L2 Score?)
    autoChooser.addOption(
        "Manual L2 Auto",
        new SequentialCommandGroup(
            DriveCommands.BasicDrive(drive, 0.5, 0).withTimeout(0.7),
            DriveCommands.BasicDrive(drive, 0, 0).withTimeout(0),
            (wrist.posWrist(wrist, 166.0).alongWith(elevator.posElevator(elevator, 5750.0)))
                .withTimeout(1),
            intake.RunIntakeCommand(intake, () -> IntakeConstants.OutTakeRunValue).withTimeout(3),
            DriveCommands.BasicDrive(drive, -0.2, 0).withTimeout(0.5)));
    // Set up SysId routines
    autoChooser.addOption(
        "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal arcade drive
    drive.setDefaultCommand(
        DriveCommands.arcadeDrive(
            drive, () -> -controller.getLeftY(), () -> -controller.getRightX()));
    // default commands for other subsystems
    // Remembering ternary operators at 11:26 PM
    // Literally almost midnight
    // Please work tmr aaaa

    // Reference the commented print statements found in each
    // command method

    climb.setDefaultCommand(
        climb.manualClimb(
            climb,
            () ->
                controller.y().getAsBoolean()
                    ? ClimbConstants.climbRunValue
                    : controller.b().getAsBoolean() ? -ClimbConstants.climbRunValue : 0));
    elevator.setDefaultCommand(elevator.posElevator(elevator, Setpoints.IDLE.getElevatorPos()));
    intake.setDefaultCommand(
        intake.manualIntake(
            intake,
            () ->
                controller.leftBumper().getAsBoolean()
                    ? IntakeConstants.IntakeRunValue
                    : controller.rightBumper().getAsBoolean()
                        ? -IntakeConstants.OutTakeRunValue
                        : 0));
    wrist.setDefaultCommand(wrist.posWrist(wrist, Setpoints.IDLE.getWristPos()));

    // Careful using these, the wristToPosition and elevatorToPosition may not work
    // controller.a().whileTrue(elevator.posElevator(elevator, -1000.0));

    // L3
    buttonBox
        .button(17)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L3_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L3_REEF.getElevatorPos())));
    controller.pov(270).whileTrue(wrist.posWrist(wrist, 100.0));
    // L2
    buttonBox
        .button(25)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L2_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L2_REEF.getElevatorPos())));

    controller.start().whileTrue(new InstantCommand(() -> elevator.resetElevator()));
    // L4
    buttonBox
        .button(9)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L4_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L4_REEF.getElevatorPos())));
    // for testing sourc
    buttonBox
        .button(7)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.SOURCE.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.SOURCE.getWristPos())));

    // L1
    buttonBox
        .button(32)
        .whileTrue(
            wrist
                .posWrist(wrist, Setpoints.L1_REEF.getWristPos())
                .alongWith(elevator.posElevator(elevator, Setpoints.L1_REEF.getElevatorPos())));
    controller.pov(270).whileTrue(wrist.posWrist(wrist, 100.0));

    // Manual Elevator control
    buttonBox.button(23).whileTrue(elevator.manualElevator(elevator, () -> 0.15));
    buttonBox.button(22).whileTrue(elevator.manualElevator(elevator, () -> -0.15));

    // Manual Wrist control
    buttonBox.button(15).whileTrue(wrist.manualWrist(wrist, () -> 0.15));
    buttonBox.button(14).whileTrue(wrist.manualWrist(wrist, () -> 0.15));

    // Manual Climb control
    buttonBox.button(30).whileTrue(climb.manualClimb(climb, () -> 0.15));

    // buttonBox.button(4).whileTrue(EndEffectorCommands.WristToPosition(wrist, Setpoints.L1_REEF));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}
