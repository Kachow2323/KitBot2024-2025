// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Shooter;
import frc.utils.States;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public final DriveSubsystem m_robotDrive;
  public final Shooter ShooterInstance;

  public static RobotContainer instance = null;
  public static final XboxController driverController = new XboxController(0);
  public static final XboxController operatorController = new XboxController(0);

  public static final Trigger driver_A = new JoystickButton(driverController, 1),
    driver_B = new JoystickButton(driverController, 2), driver_X = new JoystickButton(driverController, 3),
    driver_Y = new JoystickButton(driverController, 4), driver_LB = new JoystickButton(driverController, 5),
    driver_RB = new JoystickButton(driverController, 6), driver_VIEW = new JoystickButton(driverController, 7),
    driver_MENU = new JoystickButton(driverController, 8);
  private static final Trigger operator_A = new JoystickButton(operatorController, 1),
    operator_B = new JoystickButton(operatorController, 2), operator_X = new JoystickButton(operatorController, 3),
    operator_Y = new JoystickButton(operatorController, 4), operator_LB = new JoystickButton(operatorController, 5),
    operator_RB = new JoystickButton(operatorController, 6), operator_VIEW = new JoystickButton(operatorController, 7),
    operator_MENU = new JoystickButton(operatorController, 8);
  
  private static final POVButton operator_DPAD_UP = new POVButton(operatorController, 0),
    operator_DPAD_RIGHT = new POVButton(operatorController, 90), operator_DPAD_DOWN = new POVButton(operatorController, 180),
    operator_DPAD_LEFT = new POVButton(operatorController, 270);
  private static final POVButton driver_DPAD_UP = new POVButton(driverController, 0),
    driver_DPAD_RIGHT = new POVButton(driverController, 90), driver_DPAD_DOWN = new POVButton(driverController, 180),
    driver_DPAD_LEFT = new POVButton(driverController, 270);

  public static RobotContainer getInstance() {
    if(instance == null){ instance = new RobotContainer();}
    return instance;
}

  // Replace with CommandPS4Controller or CommandJoystick if needed
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    m_robotDrive = new DriveSubsystem();
    ShooterInstance = Shooter.getInstance();

    m_robotDrive.setDefaultCommand(
      new RunCommand(
      () -> m_robotDrive.drive(
        driverController.getLeftY(),
        driverController.getRightX()
      ),
      m_robotDrive));
    // Configure the trigger bindings
    configureBindings();
  }

  private void configureBindings() {
    operator_X
      .whileTrue(
       new RunCommand(() -> 
        { ShooterInstance.setShooterRPM(States.ShooterState.GOAL); 
          }, ShooterInstance)
      );
        operator_Y
      .whileTrue(
       new RunCommand(() -> 
        { ShooterInstance.setShooterRPM(States.ShooterState.FULL); 
          }, ShooterInstance)
      );
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}
