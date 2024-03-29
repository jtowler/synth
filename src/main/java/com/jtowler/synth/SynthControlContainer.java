package com.jtowler.synth;

import javax.swing.*;
import java.awt.*;

public class SynthControlContainer extends JPanel {
    private Point mouseClickLocation;
    protected boolean on;
    private SynthesizerRemastered synth;

    public SynthControlContainer(SynthesizerRemastered synth) {
        this.synth = synth;
    }

    public boolean isOn() {
        return on;
    }

    public Point getMouseClickLocation() {
        return mouseClickLocation;
    }

    public void setMouseClickLocation(Point mouseClickLocation) {
        this.mouseClickLocation = mouseClickLocation;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public Component add(Component component) {
        component.addKeyListener(synth.getKeyAdapter());
        return super.add(component);
    }

    @Override
    public Component add(Component component, int index) {
        component.addKeyListener(synth.getKeyAdapter());
        return super.add(component, index);
    }

    @Override
    public Component add(String name, Component component) {
        component.addKeyListener(synth.getKeyAdapter());
        return super.add(name, component);
    }

    @Override
    public void add(Component component, Object constraint) {
        component.addKeyListener(synth.getKeyAdapter());
        super.add(component, constraint);
    }

    @Override
    public void add(Component component, Object constraint, int index) {
        component.addKeyListener(synth.getKeyAdapter());
        super.add(component, constraint, index);
    }
}
