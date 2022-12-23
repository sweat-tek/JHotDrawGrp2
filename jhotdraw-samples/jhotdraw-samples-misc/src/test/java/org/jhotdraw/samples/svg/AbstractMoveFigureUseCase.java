package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.behaviourtest.GivenFigure;
import org.jhotdraw.samples.svg.behaviourtest.ThenFigure;
import org.jhotdraw.samples.svg.behaviourtest.WhenMovingFigure;

public abstract class AbstractMoveFigureUseCase<T extends Figure> extends ScenarioTest<GivenFigure, WhenMovingFigure, ThenFigure> {
    public void moveFigure(T t) {
        given().provideFigure(t);
        when().movingFigure();
        then().figureHasMoved();
    }
}
