package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.utils.States;
import frc.utils.Util;
import frc.utils.States.ShooterState;

public class Shooter extends SubsystemBase {
    private static final CANSparkMax motorTop = Util.createSparkMAX(1, MotorType.kBrushless);
    private static final CANSparkMax motorBottom = Util.createSparkMAX(2, MotorType.kBrushless);
    private RelativeEncoder relShooterEncoder = motorTop.getEncoder();
    private SparkPIDController m_pidController;
    private static Shooter instance;
    
    public static Shooter getInstance() {
        if(instance == null) instance = new Shooter();
        return instance;
    }
    public Shooter(){
        resetEncoders();
        m_pidController = motorTop.getPIDController();  //Sets the control of the right motor to speed that the PID controller commanded
        motorTop.setSmartCurrentLimit(25);
        motorBottom.setSmartCurrentLimit(25);
        motorBottom.follow(motorTop, true); // Slave motors to leading motor to sync them together
        motorTop.setIdleMode(IdleMode.kBrake);
        motorBottom.setIdleMode(IdleMode.kBrake);
        m_pidController.setP(ShooterConstants.kP); //Proportional Gain 
        m_pidController.setI(ShooterConstants.kI);// Intergral Gain
        m_pidController.setD(ShooterConstants.kD); // Derivative Gain
        m_pidController.setIZone(0); // DW will be 0 for most of the time
        m_pidController.setOutputRange(ShooterConstants.pidOutputLow, ShooterConstants.pidOutputHigh); // Max and Min output so the robot does rip itself apart
        register();
    }

    public void setOpenLoop(double value) {
        SmartDashboard.putNumber("Flywheel Percent Speed:", value);
        motorTop.set(value);
        motorBottom.set(value);
    }

    public void PauseShooter() {
        setOpenLoop(0);
    }

    public void getVelocity(){
        double currentVelocity = relShooterEncoder.getVelocity();
        SmartDashboard.putNumber("Current NEO RPM:", currentVelocity);
    }

    public void resetEncoders() {
        relShooterEncoder.setPosition(0.0);
    }

    public void setShooterRPM(double GoalRPM) {
        // double output = (ShooterConstants.DesiredRPM - getVelocity())*kP + Target*kFF = 
        m_pidController.setReference(GoalRPM, ControlType.kVelocity);
    }

    public void setShooterRPM( States.ShooterState state) {
        SmartDashboard.putNumber("Position", state.val);
        switch (state) {
            case ZERO:
                m_pidController.setP(ShooterConstants.kP);
                setShooterRPM(0);
                break;
            case HALF:
                m_pidController.setP(ShooterConstants.kP);
                setShooterRPM(0.5);
                break;
            case FULL:
                m_pidController.setP(ShooterConstants.kP);
                setShooterRPM(1.0);
                break;
            case GOAL:
                m_pidController.setP(ShooterConstants.kP);
                setShooterRPM(ShooterConstants.DesiredRPM);
                break;
        }
    }
    //(Target-Current)*kP + Target*kFF = output
    
}