// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
private static final String CANBUS_NAME = "rio";
  private final TalonFX rightFrontDriving = new TalonFX(1,CANBUS_NAME);
  private final TalonFX leftFrontDriving = new TalonFX(2,CANBUS_NAME);
  private final TalonFX rightBackDriving = new TalonFX(3,CANBUS_NAME);
  private final TalonFX leftBackDriving = new TalonFX(4,CANBUS_NAME);

  final DutyCycleOut leftOut = new DutyCycleOut(0.0);
  final DutyCycleOut rightOut = new DutyCycleOut(0.0);

  private int printCount = 0;


  /** Creates a new ExampleSubsystem. */
  public DriveSubsystem() {
    var leftConfiguration = new TalonFXConfiguration();
    var rightConfiguration = new TalonFXConfiguration();

    leftConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    rightConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;



    /* Set up followers to follow leaders */
    leftBackDriving.setControl(new Follower(leftFrontDriving.getDeviceID(), false));
    rightBackDriving.setControl(new Follower(rightFrontDriving.getDeviceID(), false));
  
    leftFrontDriving.setSafetyEnabled(true);
    rightFrontDriving.setSafetyEnabled(true);

    leftFrontDriving.getConfigurator().apply(leftConfiguration);
    leftBackDriving.getConfigurator().apply(leftConfiguration);
    rightFrontDriving.getConfigurator().apply(rightConfiguration);
    rightBackDriving.getConfigurator().apply(rightConfiguration);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command drive(double LeftWheelsControl, double RightWheelsControl) {
    leftOut.Output = LeftWheelsControl + RightWheelsControl ;
    rightOut.Output = LeftWheelsControl - RightWheelsControl;

    rightFrontDriving.setControl(rightOut);
    leftFrontDriving.setControl(leftOut);

    // Smart Dashboard is a Dashboard that displays values you want in real time
    SmartDashboard.putNumber("Right Wheel Speed", RightWheelsControl);
    SmartDashboard.putNumber("Left Wheel Speed", LeftWheelsControl);
    return null;
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (++printCount >= 10) {
        printCount = 0;
        System.out.println("Left out: " + leftFrontDriving.get());
        System.out.println("Right out: " + rightBackDriving.get());
        System.out.println("Left Pos: " + leftFrontDriving.getPosition());
        System.out.println("Right Pos: " + rightFrontDriving.getPosition());
      }
    
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

public void getInstance() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
}
}
