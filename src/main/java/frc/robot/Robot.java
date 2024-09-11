// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private static final String CANBUS_NAME = "rio";
  private final TalonFX rightFrontDriving = new TalonFX(1,CANBUS_NAME);
  private final TalonFX leftFrontDriving = new TalonFX(2,CANBUS_NAME);
  private final TalonFX rightBackDriving = new TalonFX(3,CANBUS_NAME);
  private final TalonFX leftBackDriving = new TalonFX(4,CANBUS_NAME);

  final DutyCycleOut leftOut = new DutyCycleOut(0.0);
  final DutyCycleOut rightOut = new DutyCycleOut(0.0);
  private final XboxController driverController = new XboxController(0);

  private int printCount = 0;
  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
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
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    if (++printCount >= 10) {
      printCount = 0;
      System.out.println("Left out: " + leftFrontDriving.get());
      System.out.println("Right out: " + rightBackDriving.get());
      System.out.println("Left Pos: " + leftFrontDriving.getPosition());
      System.out.println("Right Pos: " + rightFrontDriving.getPosition());
    }

    double leftWheelsControlArcade = driverController.getLeftY();
    double rightWheelsControlArcade = driverController.getRightX();

    leftOut.Output = leftWheelsControlArcade + rightWheelsControlArcade ;
    rightOut.Output = leftWheelsControlArcade - rightWheelsControlArcade;

    rightFrontDriving.setControl(rightOut);
    leftFrontDriving.setControl(leftOut);

    // Smart Dashboard is a Dashboard that displays values you want in real time
    SmartDashboard.putNumber("Right Wheel Speed", rightWheelsControlArcade);
    SmartDashboard.putNumber("Left Wheel Speed", leftWheelsControlArcade);
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
