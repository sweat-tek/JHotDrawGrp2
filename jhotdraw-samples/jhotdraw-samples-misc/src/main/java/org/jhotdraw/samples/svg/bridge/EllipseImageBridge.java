package org.jhotdraw.samples.svg.bridge;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.ResizeHandleKit;
import org.jhotdraw.draw.handle.TransformHandleKit;
import org.jhotdraw.samples.svg.figures.LinkHandle;

import java.util.Collection;
import java.util.LinkedList;

public class EllipseImageBridge {

    public Collection<Handle> createHandles(int detailLevel, Figure figure){
        LinkedList<Handle> handles = new LinkedList<Handle>();
        switch (detailLevel % 2) {
            case -1: // Mouse hover handles
                handles.add(new BoundsOutlineHandle(figure, false, true));
                break;
            case 0:
                ResizeHandleKit.addResizeHandles(figure, handles);
                handles.add(new LinkHandle(figure));
                break;
            case 1:
                TransformHandleKit.addTransformHandles(figure, handles);
                break;
            default:
                break;
        }
        return handles;
    }

}
