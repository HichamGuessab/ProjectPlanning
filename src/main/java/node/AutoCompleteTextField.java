package node;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AutoCompleteTextField extends TextField {
    private final SortedSet<String> suggestions;
    private final ContextMenu entriesPopup;
    public AutoCompleteTextField() {
        super();
        suggestions = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(getText().length() == 0) {
                    entriesPopup.hide();
                } else {
                    LinkedList<String> searchResult = new LinkedList<>(suggestions.subSet(getText(), getText() + Character.MAX_VALUE));
                    if(suggestions.size() > 0) {
                        populatePopup(searchResult);
                        if(!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoCompleteTextField.this, AutoCompleteTextField.this.localToScreen(0, AutoCompleteTextField.this.getHeight()).getX(), AutoCompleteTextField.this.localToScreen(0, AutoCompleteTextField.this.getHeight()).getY());
                        }
                    } else {
                        entriesPopup.hide();
                    }
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                entriesPopup.hide();
            }
        });
    }

    public SortedSet<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions.clear();
        this.suggestions.addAll(suggestions);
    }

    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);

        for(int i=0; i<count; i++) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent -> {
                setText(result);
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}
