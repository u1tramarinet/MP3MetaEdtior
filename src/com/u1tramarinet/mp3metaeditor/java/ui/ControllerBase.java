package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.util.UiThreadExecutor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


abstract class ControllerBase implements Initializable {
    private final UiThreadExecutor executor = new UiThreadExecutor();
    protected void runOnUiThread(Runnable r) {
        executor.execute(r);
    }

    protected void bindDisableProperty(ObservableValue<Boolean> value, Node... nodes) {
        for (Node node : nodes) {
            node.disableProperty().bind(value);
        }
    }

    protected ObservableValue<Boolean> isDisableOrNotSelected(CheckBox checkBox) {
        return checkBox.disableProperty()
                .or(checkBox.selectedProperty().not());
    }

    protected boolean isNullOrEmpty(String input) {
        return (null == input) || input.equals("");
    }

    protected void bindTextPropertyBidirectional(ObjectProperty<String> property, TextField textField) {
        textField.textProperty().bindBidirectional(property);
    }
}
