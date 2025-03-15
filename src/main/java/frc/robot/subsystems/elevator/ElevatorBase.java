package frc.robot.subsystems.elevator;

public interface ElevatorBase {

  public class ElevatorBaseInputs {}

  // This is making me go insane, however
  // the reason for all of these interfaces
  // and multiple classes are for two reasons
  // - Cleaness of code
  // - Enableing of robot simulation
  // and maybe even robot replay (but that is
  // above my paygrade atm)

  // No defaults as to force each class that implements
  // the methods to have a way to use them
  // * could change in the future
  public default void runElevator(double value) {}

  public default void stopElevator() {}

  public default void setElevatorPosition(double targetPosition) {}

  public default void setVoltage(double Volts) {}

  public default double getElevatorEncoderValues() {
    return 0.0;
  }
  // public StatusSignal<Angle> getElevatorEncoderValues2() {
  //   return 0.0
  // }

  public default void resetEncoder() {}
}
