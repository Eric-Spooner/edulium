package com.at.ac.tuwien.sepm.ss15.edulium.gui.util;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class PersistentButtonToggleGroup extends ToggleGroup {
    public PersistentButtonToggleGroup() {
        super();

        getToggles().addListener(new ListChangeListener<Toggle>() {
            @Override
            public void onChanged(Change<? extends Toggle> c) {
                while (c.next()) {
                    for (final Toggle addedToggle : c.getAddedSubList()) {
                        ((ToggleButton) addedToggle).addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                            if (addedToggle.equals(getSelectedToggle())) {
                                mouseEvent.consume();
                            }
                        });
                    }
                }
            }
        });
    }
}

