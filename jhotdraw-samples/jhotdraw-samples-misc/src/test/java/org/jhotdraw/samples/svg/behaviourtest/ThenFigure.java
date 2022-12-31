package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertTrue;

public class ThenFigure {
    @ExpectedScenarioState
    private DrawingEditor editor;

    private Figure figure;

    public ThenFigure figureHasMoved() {
        figure = editor.getActiveView().getDrawing().getFiguresFrontToBack().get(0);
        assert figure != null;

        Rectangle2D.Double bounds = figure.getBounds();
        assertBounds(bounds);

        return this;
    }

    private void assertBounds(Rectangle2D.Double bounds) {
        assertTrue(bounds.x != 0 && bounds.y != 0);
    }
}