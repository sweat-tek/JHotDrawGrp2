package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.samples.svg.behaviourtest.*;

public abstract class AbstractChangeTextUseCase<T extends TextHolderFigure> extends ScenarioTest<GivenFigure, WhenChangingText, ThenTextHolder> {
    public void moveFigure(T t) {
        String testString = "Random test string!";
        given().provideFigure(t);
        when().changingTextTo(testString);
        then().textHolderTextHasChangedTo(testString);
    }
}
