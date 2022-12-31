package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.SPI.Ellipse;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;
import org.junit.Test;

public class MoveEllipseUseCaseTest extends AbstractMoveFigureUseCase<Ellipse>{


    @Test
    public void moveEllipseTest(){
        super.moveFigure(new SVGEllipseFigure());
    }
}
