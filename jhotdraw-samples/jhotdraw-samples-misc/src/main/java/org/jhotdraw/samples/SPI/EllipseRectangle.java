package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.AttributeKey;

public interface Shape {

    <T> T get(AttributeKey<T> key);

    <T> void set(AttributeKey<T> key, T newValue);

    java.awt.Shape getTransformedShape();

}
