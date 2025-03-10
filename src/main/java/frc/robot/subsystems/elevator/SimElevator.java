package frc.robot.subsystems.elevator;

public class SimElevator implements ElevatorBase {

  public SimElevator() {}

  @Override
  public void runElevator(double value) {
    System.out.println("running elevator");
  }

  @Override
  public void stopElevator() {}

  @Override
  public double getElevatorEncoderValues() {
    return 0.0;
  }
}
