package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ThenFigure {
    @ExpectedScenarioState
    private DrawingEditor editor;

    private List<Figure> figureList;

    public ThenFigure figureHasMoved() {
        figureList = editor.getActiveView().getDrawing().getFiguresFrontToBack();
        assert figureList.size() > 0;

        figureList.forEach((Figure figure) -> {
            Rectangle2D.Double bounds = figure.getBounds();
            assertBounds(bounds);
        });
        return this;
    }

    private void assertBounds(Rectangle2D.Double bounds) {
        assertTrue(bounds.x != 0 && bounds.y != 0);
    }
}