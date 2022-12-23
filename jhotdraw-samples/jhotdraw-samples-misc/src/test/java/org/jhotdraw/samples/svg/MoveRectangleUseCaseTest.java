package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.SPI.Rectangle;
import org.jhotdraw.samples.factory.AbstractFigureFactory;
import org.jhotdraw.samples.factory.RectangleFactory;
import org.junit.Test;

public class MoveRectangleUseCaseTest extends AbstractMoveFigureUseCase<Rectangle> {

    @Test
    public void moveRectangleTest() {
        super.moveFigure(RectangleFactory.getInstance().create(AbstractFigureFactory.Type.SVG));
    }
}
