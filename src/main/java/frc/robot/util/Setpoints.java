package frc.robot.util;

public enum Setpoints {
  // Discount looged numbers lol
  SOURCE(1000, 1.0),
  L1_REEF(1.0, 1.0),
  L2_REEF(1.0, 1.0),
  L3_REEF(1.0, 1.0),
  L4_REEF(1.0, 1.0);
  private double elevatorPos, wristPos;

  private Setpoints(double elevatorPos, double wristPos) {
    this.elevatorPos = elevatorPos;
    this.wristPos = wristPos;
  }

  public double getElevatorPos() {
    return elevatorPos;
  }

  public double getWristPos() {
    return wristPos;
  }
}
