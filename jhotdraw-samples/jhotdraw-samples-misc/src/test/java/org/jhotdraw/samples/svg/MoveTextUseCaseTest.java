package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.svg.figures.SVGTextFigure;
import org.junit.Test;

public class MoveTextUseCaseTest extends AbstractMoveFigureUseCase<SVGTextFigure> {

    @Test
    public void moveTextTest(){
        super.moveFigure(new SVGTextFigure());
    }

}
