package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.awt.geom.AffineTransform;
import java.util.Set;

public class WhenMovingFigure {
    @ProvidedScenarioState
    @ExpectedScenarioState
    private DrawingEditor editor;

    @BeforeStage
    public void before() {
        editor.getActiveView().getDrawing().getFiguresFrontToBack().iterator().forEachRemaining(
                fig -> {
                    editor.getActiveView().addToSelection(fig);
                }
        );
    }

    public WhenMovingFigure movingFigure() {
        assert editor.getActiveView().getSelectionCount() > 0;
        Set<Figure> figs = editor.getActiveView().getSelectedFigures();
        assert figs != null;
        figs.forEach((Figure figure) -> {
            AffineTransform transform = new AffineTransform();
            transform.translate(10, 10);
            figure.transform(transform);
        });

        return this;
    }
}
