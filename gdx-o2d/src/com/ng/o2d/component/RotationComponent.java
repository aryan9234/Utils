package com.ng.o2d.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * (c) 2016 Abhishek Aryan
 *
 * @author Abhishek Aryan
 * @since 1/30/2016.
 */
public class RotationComponent implements Component {

    public boolean clockwise;
    public float speed=1;

    public RotationComponent(float speed){
        clockwise= MathUtils.randomBoolean();
        this.speed=speed;
    }
}
