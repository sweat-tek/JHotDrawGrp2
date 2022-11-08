package org.jhotdraw.draw.text;

import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;

public class UndoableTextEdit extends AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;
    private final TextHolderFigure editedFigure;
    private final String oldText;
    private final String newText;

    public UndoableTextEdit(TextHolderFigure editedFigure, String oldText, String newText) {
        this.editedFigure = editedFigure;
        this.oldText = oldText;
        this.newText = newText;
    }


    @Override
    public String getPresentationName() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels.getString("attribute.text.text");
    }

    @Override
    public void undo() {
        super.undo();
        editedFigure.willChange();
        editedFigure.setText(oldText);
        editedFigure.changed();
    }

    @Override
    public void redo() {
        super.redo();
        editedFigure.willChange();
        editedFigure.setText(newText);
        editedFigure.changed();
    }
}