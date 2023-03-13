// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LedConstants;
import frc.robot.subsystems.LedSubsystem;

public class LedChase extends CommandBase {
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    private Integer startR;
    private Integer startG;
    private Integer startB;
    private int headR;
    private int headG;
    private int headB;
    private int baseR;
    private int baseG;
    private int baseB;
    private int trailR;
    private int trailG;
    private int trailB;
    private int trailLength;
    private int trailDecayDeltaR;
    private int trailDecayDeltaG;
    private int trailDecayDeltaB;
    private double previousTime;
    private double waitTime;
    private boolean randomFillStart = false;
    private AddressableLEDBuffer buffer = new AddressableLEDBuffer(LedConstants.LED_LENGTH);
    private LedSubsystem ledSubsystem;
    private int headPosition = 0;
    private double decayRate;

    public LedChase(
            LedSubsystem ledSubsystem,
            double frequency,
            boolean randomFillStart,
            Integer startR, Integer startG, Integer startB,
            int baseR, int baseG, int baseB,
            int headR, int headG, int headB,
            int trailLength,
            int trailR, int trailG, int trailB,
            int trailDecayDeltaR, int trailDecayDeltaG, int trailDecayDeltaB) {
        this.ledSubsystem = ledSubsystem;
        this.waitTime = 1 / frequency;
        this.startR = startR;
        this.startG = startG;
        this.startB = startB;
        this.headR = headR;
        this.headG = headG;
        this.headB = headB;
        this.baseR = baseR;
        this.baseG = baseG;
        this.baseB = baseB;
        this.trailR = trailR;
        this.trailG = trailG;
        this.trailB = trailB;
        this.trailDecayDeltaR = trailDecayDeltaR;
        this.trailDecayDeltaG = trailDecayDeltaG;
        this.trailDecayDeltaB = trailDecayDeltaB;
        this.trailLength = trailLength;
        this.decayRate = (trailLength / (double)buffer.getLength());
        this.randomFillStart = randomFillStart;

        if (startR == null || startG == null || startB == null) {
            this.randomFillStart = true;
        }
        addRequirements(ledSubsystem);
    }

    @Override
    public void initialize() {
        if (randomFillStart) {
            scramble();
        } else {
            for (int i = 0; i < buffer.getLength(); i++) {
                buffer.setRGB(i, startR, startG, startB);
            }
        }

        ledSubsystem.setLeds(buffer);
        buffer.setRGB(headPosition, headR, headG, headB);
        ledSubsystem.setLeds(buffer);
        previousTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {

            if (Timer.getFPGATimestamp() >= previousTime + waitTime) {
                previousTime = Timer.getFPGATimestamp();
                headPosition++;
                if (headPosition - trailLength > buffer.getLength()) {
                    headPosition = 0;
                    scramble();
                }
                for (int i = 0; i <= trailLength + 1; i++) {
                    if (i == trailLength + 1) {
                        if(headPosition - i >= 0 && headPosition - i < buffer.getLength()) {
                            buffer.setRGB(headPosition - i, baseR, baseG, baseB);
                        }
                    } else
                        if(headPosition - i >= 0 && headPosition - i < buffer.getLength()) {
                            buffer.setRGB(headPosition - i, calcRTrail(i), calcGTrail(i), calcBTrail(i));
                        }
                }
                ledSubsystem.setLeds(buffer);
            }

    }

    @Override
    public void end(boolean interrupted) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(i, LedConstants.TIMBERWOLF_R, LedConstants.TIMBERWOLF_G, LedConstants.TIMBERWOLF_B);
        }
        ledSubsystem.setLeds(buffer);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private int calcRTrail(int i) {
        return (int) Math.round(
                Math.max(
                        Math.min(
                                (trailR + trailDecayDeltaR * i) - (decayRate * i),
                                255),
                        0));
    }

    private int calcGTrail(int i) {
        return (int) Math.max(
                Math.min(
                        (trailG + trailDecayDeltaG * i) - (decayRate * i),
                        255),
                0);
    }

    private int calcBTrail(int i) {
        return (int) Math.max(
                Math.min(
                        (trailB + trailDecayDeltaB * i) - (decayRate * i),
                        255),
                0);
    }

    private void scramble() {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(i, (int) Math.floor(Math.random() * (255 - 25 + 1) + 25),
                    (int) Math.floor(Math.random() * (255 - 25 + 1) + 25),
                    (int) Math.floor(Math.random() * (255 - 25 + 1) + 25));
        }
    }
}
