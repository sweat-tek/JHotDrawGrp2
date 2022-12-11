package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.behaviourtest.GivenFigure;
import org.jhotdraw.samples.svg.behaviourtest.TestFigureCommand;
import org.jhotdraw.samples.svg.behaviourtest.ThenFigure;
import org.jhotdraw.samples.svg.behaviourtest.WhenMovingFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;
import org.junit.Test;

public class RectangleUseCaseTest extends ScenarioTest<GivenFigure, WhenMovingFigure, ThenFigure> {
    @Test
    public void movingRectangleFigure() {
        TestFigureCommand testFigureCommand = () -> SVGRectFigure.newDefaultRectangle();
        given().provideFigure(testFigureCommand);
        when().movingFigure();
        then().figureHasMoved();
    }
}
