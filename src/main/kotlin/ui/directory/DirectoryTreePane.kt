package ui.directory

import presenter.Presenter
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.text.Text
import java.nio.file.Path

class DirectoryTreePane(private val presenter: Presenter) : ScrollPane() {
    private val listView = ListView<Path>()

    init {
        content = listView
        isFitToWidth = true
        isFitToHeight = true
    }

    fun showDirectoryContent(directoryPath: Path, contentPaths: Iterable<Path>, desiredSubstring: String) {
        if (contentPaths.any()) {
            for (path in contentPaths.sortedBy { it.toString() }) {
                listView.items.add(path)
            }
            listView.setCellFactory { PathListCell(directoryPath) }
            listView.onKeyPressed = EventHandler<KeyEvent> { keyEvent: KeyEvent ->
                if (keyEvent.code == KeyCode.ENTER) {
                    val path = listView.selectionModel.selectedItem
                    presenter.openFile(path, desiredSubstring)
                }
            }
            listView.onMouseClicked = EventHandler<MouseEvent> { mouseEvent: MouseEvent ->
                if (mouseEvent.button == MouseButton.PRIMARY && mouseEvent.clickCount == 2) {
                    val path = listView.selectionModel.selectedItem
                    presenter.openFile(path, desiredSubstring)
                }
            }
            listView.padding = Insets(3.0, 0.0, 3.0, 0.0)
        } else {
            listView.placeholder = Text("No matches for \"$desiredSubstring\"")
        }
    }

    fun clearDirectoryContent() {
        listView.placeholder = Text()
        listView.items.clear()
    }

    private inner class PathListCell(private val directoryPath: Path) : ListCell<Path?>() {

        override fun updateItem(item: Path?, empty: Boolean) {
            super.updateItem(item, empty)
            text = if (item == null || empty) {
                null
            } else {
                if (directoryPath == item) {
                    item.fileName.toString()
                } else {
                    directoryPath.relativize(item).toString()
                }
            }
        }
    }
}