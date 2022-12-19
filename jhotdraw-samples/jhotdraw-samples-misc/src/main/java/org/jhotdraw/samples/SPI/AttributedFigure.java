package org.jhotdraw.samples.SPI;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.figure.Figure;

import java.util.Map;

public interface AttributedFigure extends Figure {
    void setAttributes(Map<AttributeKey<?>, Object> map);
}
