package frc.robot.subsystems.endeffector.wrist;

public class SimWrist implements WristBase {
  public SimWrist() {}

  @Override
  public void rotateWrist(double value) {
    System.out.println("rotating wrist");
  }

  @Override
  public void stopWrist() {
    
  }

  @Override
  public double getWristAngle() {
    return 0.0;
  }
}
