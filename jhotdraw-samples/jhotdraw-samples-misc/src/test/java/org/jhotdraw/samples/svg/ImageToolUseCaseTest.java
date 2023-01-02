package org.jhotdraw.samples.svg;


import org.jhotdraw.samples.svg.figures.SVGImageFigure;
import org.junit.Test;

public class ImageToolUseCaseTest extends AbstractMoveFigureUseCase<SVGImageFigure>{

    @Test
    public void moveImageToolTest() {
        super.moveFigure(new SVGImageFigure());
    }
}
