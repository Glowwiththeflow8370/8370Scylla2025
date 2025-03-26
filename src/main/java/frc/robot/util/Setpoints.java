package frc.robot.util;

public enum Setpoints {
  // Discount looged numbers lol
  // Should now be set up for "easier"
  // setpoint setup (probs not needed tho lol)
  IDLE(6000.0, 35.0),
  SOURCE(4350.0, 80.0),
  L1_REEF(1450.0, 100.0),
  L2_REEF(5750.0, 135.0),
  L3_REEF(9000.0, 135.0),
  L4_REEF(15150.0, 140.0);
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
