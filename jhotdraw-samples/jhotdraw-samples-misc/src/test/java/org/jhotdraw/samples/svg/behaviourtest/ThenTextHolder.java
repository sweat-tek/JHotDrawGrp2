package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;


import static org.junit.Assert.assertEquals;

public class ThenTextHolder {
    @ExpectedScenarioState
    private DrawingEditor editor;

    public ThenTextHolder textHolderTextHasChangedTo(String textValue) {
        Figure figure = editor.getActiveView().getDrawing().getFiguresFrontToBack().get(0);
        assert figure != null;
        assert figure instanceof TextHolderFigure;
        TextHolderFigure textHolder = (TextHolderFigure) figure;

        assertEquals(textHolder.getText(), textValue);

        return this;
    }
}