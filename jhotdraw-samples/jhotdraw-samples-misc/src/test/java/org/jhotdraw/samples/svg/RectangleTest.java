package org.jhotdraw.samples.svg;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class RectangleTest extends AssertJSwingJUnitTestCase {
    @Override
    protected void onSetUp() throws Exception {
        application(Main.class).start();
    }

    @Test
    public void UsecaseTest() {
        /*
        FrameFixture frame = findFrame(new GenericTypeMatcher<Frame>(Frame.class) {
            protected boolean isMatching(Frame frame) {
                return "Your application title".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
         */
    }
}
