package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.Util;

public class Shooter extends SubsystemBase {
    private static final CANSparkMax motorTop = Util.createSparkMAX(1, MotorType.kBrushless);
    private static final CANSparkMax motorBottom = Util.createSparkMAX(2, MotorType.kBrushless);
    private RelativeEncoder relShooterEncoder = motorTop.getEncoder();

    
}
