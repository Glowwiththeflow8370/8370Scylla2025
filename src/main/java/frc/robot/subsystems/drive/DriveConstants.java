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

package frc.robot.subsystems.drive;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class DriveConstants {
  public static final double maxSpeedMetersPerSec = 6;
  public static final double trackWidth = Units.inchesToMeters(24.3125);

  // Device CAN IDs
  //   public static final int pigeonCanId = 1;
  public static final int leftLeaderCanId = 4;
  public static final int leftFollowerCanId = 2;
  public static final int rightLeaderCanId = 3;
  public static final int rightFollowerCanId = 1;

  // Motor configuration
  public static final int currentLimit = 80;
  public static final double wheelRadiusMeters = Units.inchesToMeters(3.0);
  public static final double motorReduction = 0.1018;
  public static final boolean leftInverted = false;
  public static final boolean rightInverted = true;
  public static final DCMotor gearbox = DCMotor.getKrakenX60(2);

  // Velocity PID configuration
  public static final double realKp = 0.0;
  public static final double realKd = 0.0;
  public static final double realKs = 0.0;
  public static final double realKv = 0.1;

  public static final double simKp = 0.05;
  public static final double simKd = 0.0;
  public static final double simKs = 0.00889;
  public static final double simKv = 0.22651;

  // PathPlanner configuration
  public static final double robotMassKg = 74.088;
  public static final double robotMOI = 6.883;
  public static final double wheelCOF = 1.2;
  public static final RobotConfig ppConfig =
      new RobotConfig(
          robotMassKg,
          robotMOI,
          new ModuleConfig(
              wheelRadiusMeters,
              maxSpeedMetersPerSec,
              wheelCOF,
              gearbox.withReduction(motorReduction),
              currentLimit,
              2),
          trackWidth);
}
