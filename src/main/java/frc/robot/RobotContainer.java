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
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.climb.Climb;
import frc.robot.subsystems.climb.ClimbBase;
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
import frc.robot.subsystems.elevator.ElevatorConstants;
import frc.robot.subsystems.elevator.SimElevator;
import frc.robot.subsystems.elevator.TalonFXElevator;
import frc.robot.subsystems.endeffector.intake.Intake;
import frc.robot.subsystems.endeffector.intake.IntakeBase;
import frc.robot.subsystems.endeffector.intake.IntakeConstants;
import frc.robot.subsystems.endeffector.intake.SimIntake;
import frc.robot.subsystems.endeffector.intake.SparkFlexIntake;
import frc.robot.subsystems.endeffector.wrist.SimWrist;
import frc.robot.subsystems.endeffector.wrist.SparkMaxWrist;
import frc.robot.subsystems.endeffector.wrist.Wrist;
import frc.robot.subsystems.endeffector.wrist.WristBase;
import frc.robot.subsystems.endeffector.wrist.WristConstants;
import frc.robot.util.OperatorConsts;
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
  private final CommandPS4Controller controller =
      new CommandPS4Controller(OperatorConsts.PS4DriverPort);

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
        wrist = new Wrist(new SimWrist());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive = new Drive(new DriveIO() {}, new GyroIO() {});
        climb = new Climb(new ClimbBase() {});
        elevator = new Elevator(new ElevatorBase() {});
        intake = new Intake(new IntakeBase() {});
        wrist = new Wrist(new WristBase() {});
        break;
    }

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

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
                controller.triangle().getAsBoolean()
                    ? ClimbConstants.climbRunValue
                    : controller.cross().getAsBoolean() ? -ClimbConstants.climbRunValue : 0));
    elevator.setDefaultCommand(
        elevator.manualElevator(
            elevator,
            () ->
                controller.pov(0).getAsBoolean()
                    ? ElevatorConstants.elevatorRunValue
                    : controller.pov(180).getAsBoolean()
                        ? -ElevatorConstants.elevatorRunValue
                        : 0));
    intake.setDefaultCommand(
        intake.manualIntake(
            intake,
            () ->
                controller.L2().getAsBoolean()
                    ? IntakeConstants.IntakeRunValue
                    : controller.R2().getAsBoolean() ? -IntakeConstants.IntakeRunValue : 0));
    wrist.setDefaultCommand(
        wrist.manualWrist(
            wrist,
            () ->
                controller.L1().getAsBoolean()
                    ? WristConstants.WristRunValue
                    : controller.L2().getAsBoolean() ? -WristConstants.WristRunValue : 0));

    // Careful using these, the wristToPosition and elevatorToPosition may not work
    // buttonBox.button(3).whileTrue(EndEffectorCommands.WristToPosition(wrist, Setpoints.SOURCE));
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
