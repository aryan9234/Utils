package com.nayragames.vis.component;

import com.artemis.Component;

/**
 * (c) Abhishek Aryan
 *
 * @author Abhishek Aryan
 * @since 12/9/2015.
 *
 */

public class ExpireComponent extends Component {

    public enum ExpireType {
        MAGNET,SHIELD,ANIMATION,PARTICLE,OTHER
    }

    public float delay;
    public ExpireType expireType = ExpireType.OTHER;

    public ExpireComponent(){
        this(1, ExpireType.OTHER);
    }

    public ExpireComponent(float delay,ExpireType type){
        this.delay=delay;
        this.expireType =type;
    }
}
