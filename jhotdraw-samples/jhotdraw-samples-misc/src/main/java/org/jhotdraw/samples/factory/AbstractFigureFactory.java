package org.jhotdraw.samples.factory;

import org.jhotdraw.draw.figure.Figure;

public interface AbstractFigureFactory<T extends Figure>{
    enum Type {
        SVG,
        ODG
    }
    T create(Type type);
}
