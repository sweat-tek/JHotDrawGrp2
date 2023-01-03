package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.svg.figures.SVGTextFigure;
import org.junit.Test;

public class ChangeTextUseCaseTest extends AbstractChangeTextUseCase<SVGTextFigure> {

    @Test
    public void moveTextTest(){
        super.moveFigure(new SVGTextFigure());
    }

}
