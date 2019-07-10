package com.jtowler.synth;

import com.jtowler.synth.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Oscillator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;
    private Wavetable wavetable = Wavetable.Sine;
    private int wavetableStepSize;
    private int wavetableIndex;
    private double keyFrequency;
    private int toneOffset;


    public Oscillator(SynthesizerRemastered synth) {
        super(synth);
        JComboBox<Wavetable> comboBox = new JComboBox<>(Wavetable.values());
        comboBox.setSelectedItem(Wavetable.Sine);
        comboBox.setBounds(10, 10, 75, 75);
        comboBox.addItemListener(l -> {
            if (l.getStateChange() == ItemEvent.SELECTED) {
                wavetable = (Wavetable) l.getItem();
            }
        });
        add(comboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        toneParameter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                        new Point(0, 0), "blank_cursor");
                setCursor(BLANK_CURSOR);
                mouseClickLocation = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        toneParameter.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseClickLocation.y != e.getYOnScreen()) {
                    boolean mouseMovingUp = mouseClickLocation.y - e.getYOnScreen() > 0;
                    if (mouseMovingUp && toneOffset < TONE_OFFSET_LIMIT) {
                        ++toneOffset;
                    } else if (!mouseMovingUp && toneOffset > -TONE_OFFSET_LIMIT) {
                        --toneOffset;
                    }
                    applyToneOffset();
                    toneParameter.setText("x" + getToneOffset());
                }
                Utils.ParameterHandling.PARAMETER_ROBOT.mouseMove(mouseClickLocation.x, mouseClickLocation.y);
            }
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 24);
        add(toneText);
        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    public void setKeyFrequency(double frequency) {
        keyFrequency = frequency;
        applyToneOffset();
    }

    private double getToneOffset() {
        return toneOffset / 1000d;
    }

    public double nextSample() {
        double sample = wavetable.getSamples()[wavetableIndex];
        wavetableIndex = (wavetableIndex + wavetableStepSize) % Wavetable.SIZE;
        return sample;
    }

    private void applyToneOffset(){
        wavetableStepSize = (int)(Wavetable.SIZE * (keyFrequency * Math.pow(2, getToneOffset())) / SynthesizerRemastered.AudioInfo.SAMPLE_RATE);
    }
}
