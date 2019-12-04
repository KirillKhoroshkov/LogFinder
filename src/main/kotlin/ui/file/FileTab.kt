package ui.file

import javafx.event.EventHandler
import javafx.scene.control.ListView
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.input.*
import javafx.scene.input.Clipboard.getSystemClipboard
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import java.util.*


class FileTab(lines: Iterable<String>, indexesOfContainingSubstring: SortedSet<Int>) : Tab() {
    private val listView = ListView<String>()
    private val fileNavigationPane = FileNavigationPane(indexesOfContainingSubstring, listView)

    init {
        val scrollPane = ScrollPane(listView)
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true
        val vBox = VBox(fileNavigationPane, scrollPane)
        scrollPane.maxWidth = Double.MAX_VALUE
        VBox.setVgrow(scrollPane, Priority.ALWAYS)
        fileNavigationPane.maxWidth = Double.MAX_VALUE
        lines.forEach {
            listView.items.add(it)
        }
        listView.onKeyPressed = EventHandler<KeyEvent> { keyEvent: KeyEvent ->
            val copyCombination = KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY)
            if (copyCombination.match(keyEvent)) {
                val line = listView.selectionModel.selectedItem
                val clipboardContent = ClipboardContent()
                clipboardContent.putString(line)
                getSystemClipboard().setContent(clipboardContent)
            }
        }
        content = vBox
    }
}