package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;

import java.util.Set;
import java.util.stream.Collectors;

public class WhenChangingText {
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

    public WhenChangingText changingTextTo(String textValue) {
        assert editor.getActiveView().getSelectionCount() > 0;
        Set<Figure> figs = editor.getActiveView()
                .getSelectedFigures().stream()
                .filter(x -> x instanceof TextHolderFigure)
                .collect(Collectors.toSet());
        assert !figs.isEmpty();

        figs.forEach((Figure figure) -> ((TextHolderFigure) figure).setText(textValue));

        return this;
    }
}
