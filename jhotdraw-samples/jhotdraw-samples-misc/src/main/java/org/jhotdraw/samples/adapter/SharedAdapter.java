package org.jhotdraw.samples.adapter;

import org.jhotdraw.draw.AttributeKey;

import java.awt.*;

public interface SharedAdapter {

    <T> T get(AttributeKey<T> key);

    Shape getTransformedShape();

}
