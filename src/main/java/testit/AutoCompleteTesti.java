package testit;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoCompleteTesti extends Application {
    static String[] tekstit = new String[] {
            "tekstiä",
            "jotain muuta",
            "kokeiluja",
            "aaaaaaaaaa",
            "nämä ovat vaihtoehtoja"
    };

    static AutoCompleteTextField<String> autoTF = new AutoCompleteTextField<>(null);

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane(autoTF);

        autoTF.getEntries().addAll(List.of(tekstit));

        /*
        TextFieldistä pitää painaa hiirellä vielä kerran, sitten voi ylös- ja alas-nuolilla
        valita halutun vaihtoehdon ehdotuksista kun kirjoittaa.
         */

        Scene scene = new Scene(pane, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/**
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.<p>
 *
 * If the entered text matches a part of any of the supplied entries these are
 * going to be displayed in a popup. Further the matching part of the entry is
 * going to be displayed in a special style, defined by
 * {@link #textOccurenceStyle textOccurenceStyle}. The maximum number of
 * displayed entries in the popup is defined by
 * {@link #maxEntries maxEntries}.<br>
 * By default the pattern matching is not case-sensitive. This behaviour is
 * defined by the {@link #caseSensitive caseSensitive}
 * .<p>
 *
 * The AutoCompleteTextField also has a List of
 * {@link #filteredEntries filteredEntries} that is equal to the search results
 * if search results are not empty, or {@link #filteredEntries filteredEntries}
 * is equal to {@link #entries entries} otherwise. If
 * {@link #popupHidden popupHidden} is set to true no popup is going to be
 * shown. This list can be used to bind all entries to another node (a ListView
 * for example) in the following way:
 * <pre>
 * <code>
 * AutoCompleteTextField auto = new AutoCompleteTextField(entries);
 * auto.setPopupHidden(true);
 * SimpleListProperty filteredEntries = new SimpleListProperty(auto.getFilteredEntries());
 * listView.itemsProperty().bind(filteredEntries);
 * </code>
 * </pre>
 *
 * @author Caleb Brinkman
 * @author Fabian Ochmann
 * @param <S>
 */
// TODO enteriä pitää painaa kahdesti joskus
class AutoCompleteTextField<S> extends TextField
{

    private final ObjectProperty<S> lastSelectedItem = new SimpleObjectProperty<>();

    /**
     * The existing autocomplete entries.
     */
    private final SortedSet<S> entries;

    /**
     * The set of filtered entries:<br>
     * Equal to the search results if search results are not empty, equal to
     * {@link #entries entries} otherwise.
     */
    private ObservableList<S> filteredEntries
            = FXCollections.observableArrayList();

    /**
     * The popup used to select an entry.
     */
    private ContextMenu entriesPopup;

    /**
     * Indicates whether the search is case sensitive or not. <br>
     * Default: false
     */
    private boolean caseSensitive = false;

    /**
     * Indicates whether the Popup should be hidden or displayed. Use this if
     * you want to filter an existing list/set (for example values of a
     * {@link javafx.scene.control.ListView ListView}). Do this by binding
     * {@link #getFilteredEntries() getFilteredEntries()} to the list/set.
     */
    private boolean popupHidden = false;

    /**
     * The CSS style that should be applied on the parts in the popup that match
     * the entered text. <br>
     * Default: "-fx-font-weight: bold; -fx-fill: red;"
     * <p>
     * Note: This style is going to be applied on an
     * {@link javafx.scene.text.Text Text} instance. See the <i>JavaFX CSS
     * Reference Guide</i> for available CSS Propeties.
     */
    private String textOccurenceStyle = "-fx-font-weight: bold; "
            + "-fx-fill: red;";

    /**
     * The maximum Number of entries displayed in the popup.<br>
     * Default: 10
     */
    private int maxEntries = 10;

    /**
     * Construct a new AutoCompleteTextField.
     *
     * @param entrySet
     */
    public AutoCompleteTextField(SortedSet<S> entrySet)
    {
        super();
        this.entries = (entrySet == null ? new TreeSet<>() : entrySet);
        this.filteredEntries.addAll(entries);

        entriesPopup = new ContextMenu();

        textProperty().addListener((ObservableValue<? extends String> observableValue, String s, String s2) ->
        {

            if (getText() == null || getText().length() == 0)
            {
                filteredEntries.clear();
                filteredEntries.addAll(entries);
                entriesPopup.hide();
            } else
            {
                LinkedList<S> searchResult = new LinkedList<>();
                //Check if the entered Text is part of some entry
                String text1 = getText();
                Pattern pattern;
                if (isCaseSensitive())
                {
                    pattern = Pattern.compile(".*" + text1 + ".*");
                } else
                {
                    pattern = Pattern.compile(".*" + text1 + ".*", Pattern.CASE_INSENSITIVE);
                }
                for (S entry : entries)
                {
                    Matcher matcher = pattern.matcher(entry.toString());
                    if (matcher.matches())
                    {
                        searchResult.add(entry);
                    }
                }
                if (!entries.isEmpty())
                {
                    filteredEntries.clear();
                    filteredEntries.addAll(searchResult);
                    //Only show popup if not in filter mode
                    if (!isPopupHidden())
                    {
                        populatePopup(searchResult, text1);
                        if (!entriesPopup.isShowing())
                        {
                            entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    }
                } else
                {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) ->
        {
            entriesPopup.hide();
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public SortedSet<S> getEntries()
    {
        return entries;
    }

    /**
     * Populate the entry set with the given search results. Display is limited
     * to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<S> searchResult, String text)
    {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int count = Math.min(searchResult.size(), getMaxEntries());
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i).toString();
            final S itemObject = searchResult.get(i);
            int occurence;

            if (isCaseSensitive())
            {
                occurence = result.indexOf(text);
            } else
            {
                occurence = result.toLowerCase().indexOf(text.toLowerCase());
            }
            if (occurence < 0)
            {
                continue;
            }
            //Part before occurence (might be empty)
            Text pre = new Text(result.substring(0, occurence));
            //Part of (first) occurence
            Text in = new Text(result.substring(occurence, occurence + text.length()));
            in.setStyle(getTextOccurenceStyle());
            //Part after occurence
            Text post = new Text(result.substring(occurence + text.length(), result.length()));

            TextFlow entryFlow = new TextFlow(pre, in, post);

            CustomMenuItem item = new CustomMenuItem(entryFlow, true);
            item.setOnAction((ActionEvent actionEvent) ->
            {
                lastSelectedItem.set(itemObject);
                // Added lines by Orofil
                this.setText(lastSelectedItem.get().toString()); // Autofills the selection
                this.positionCaret(this.getText().length()); // Sets the caret to the end
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }

    public S getLastSelectedObject()
    {
        return lastSelectedItem.get();
    }

    public ContextMenu getEntryMenu()
    {
        return entriesPopup;
    }

    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    public String getTextOccurenceStyle()
    {
        return textOccurenceStyle;
    }

    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    public void setTextOccurenceStyle(String textOccurenceStyle)
    {
        this.textOccurenceStyle = textOccurenceStyle;
    }

    public boolean isPopupHidden()
    {
        return popupHidden;
    }

    public void setPopupHidden(boolean popupHidden)
    {
        this.popupHidden = popupHidden;
    }

    public ObservableList<S> getFilteredEntries()
    {
        return filteredEntries;
    }

    public int getMaxEntries()
    {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries)
    {
        this.maxEntries = maxEntries;
    }

}