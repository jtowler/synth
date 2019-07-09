package com.jtowler.synth;

import com.jtowler.synth.utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Random;

import static com.jtowler.synth.SynthesizerRemastered.AudioInfo.SAMPLE_RATE;

public class Oscillator extends SynthControlContainer {

    private final Random random = new Random();
    private static final double FREQUENCY = 440;
    private int wavePos;

    private Waveform waveform = Waveform.Sine;

    public Oscillator(SynthesizerRemastered synth) {
        super(synth);
        JComboBox<Waveform> comboBox = new JComboBox<>(new Waveform[]{Waveform.Sine, Waveform.Square, Waveform.Saw, Waveform.Triangle, Waveform.Noise});
        comboBox.setSelectedItem(Waveform.Sine);
        comboBox.setBounds(10, 10, 75, 75);
        comboBox.addItemListener(l -> {
            if (l.getStateChange() == ItemEvent.SELECTED) {
                waveform = (Waveform) l.getItem();
            }
        });
        add(comboBox);
        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    private enum Waveform {
        Sine, Square, Saw, Triangle, Noise
    }

    public double nextSample() {
        double tDivP = (wavePos++ / (double) SAMPLE_RATE) / (1d / FREQUENCY);
        switch (waveform) {
            case Sine:
                return Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SAMPLE_RATE);
            case Square:
                return Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SAMPLE_RATE));
            case Saw:
                return 2d * (tDivP - Math.floor(0.5 + tDivP));
            case Triangle:
                return 2d * Math.abs(2d * (tDivP - Math.floor(0.5 + tDivP))) - 1;
            case Noise:
                return random.nextDouble();
            default:
                throw new RuntimeException("Oscillator set to unknown waveform.");
        }
    }
}
