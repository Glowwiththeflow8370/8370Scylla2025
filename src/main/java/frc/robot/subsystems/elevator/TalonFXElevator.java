package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.measure.Angle;

public class TalonFXElevator implements ElevatorBase {

  TalonFX elevatorMotor, elevatorMotorFollower;
  TalonFXConfiguration elevatorMotorConfigs;

  StatusSignal<Angle> elevatorEncoder;

  PIDController elevatorPID;
  double targetPosition;
  double speedMultiplier; // Adjusts speed when moving up/down

  // PID Constants (Tune these based on your system)
  private static final double kP = .1;
  private static final double kI = 0;
  private static final double kD = 0;

  // Default speed multipliers (Can be tuned)
  private static final double UP_SPEED = 0.1; // Full speed up
  private static final double DOWN_SPEED = 0.02; // Slower speed down (gravity helps)

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

    // Initialize PID Controller
    elevatorPID = new PIDController(kP, kI, kD);
    elevatorPID.setTolerance(100); // Allowable error in encoder ticks

    // Default target position is current position
    targetPosition = getElevatorEncoderValues();
  }

  @Override
  public void runElevator(double value) {
    elevatorMotor.set(value);
  }

  @Override
  public void stopElevator() {
    elevatorMotor.set(0);
  }

  @Override
  public double getElevatorEncoderValues() {
    return elevatorMotor.getPosition().getValueAsDouble() * 360;
  }

  public void setElevatorPosition(double newTargetPosition) {
    targetPosition = newTargetPosition;
    double currentPosition = getElevatorEncoderValues();

    // Adjust speed based on direction
    if (targetPosition > currentPosition) {
      speedMultiplier = UP_SPEED; // Going up
    } else {
      speedMultiplier = DOWN_SPEED; // Going down
    }

    double pidOutput = elevatorPID.calculate(currentPosition, targetPosition);

    // Apply speed control
    elevatorMotor.setVoltage(pidOutput * speedMultiplier);
  }
}
