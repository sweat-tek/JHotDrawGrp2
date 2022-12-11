package org.jhotdraw.samples.svg.behaviourtest;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;

public class GivenFigure extends Stage<GivenFigure> {
    @ProvidedScenarioState
    private DrawingEditor editor;

    @BeforeStage
    private void before() {
        editor = new DefaultDrawingEditor();
        DrawingView view = new DefaultDrawingView();
        view.setDrawing(new QuadTreeDrawing());
        editor.setActiveView(view);
    }

    public GivenFigure provideFigure(TestFigureCommand testFigureCommand) {
        Figure figure = testFigureCommand.execute();
        editor.getActiveView().getDrawing().add(figure);

        return this;
    }
}
