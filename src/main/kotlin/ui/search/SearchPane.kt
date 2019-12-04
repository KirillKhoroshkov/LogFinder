package ui.search

import presenter.Presenter
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

class SearchPane(private val presenter: Presenter) : VBox() {
    private val searchTextField = TextField()
    private val directoryTextField = TextField()
    private val extensionsTextField = TextField()
    private val searchButton = Button("", ImageView(Image("search.png")))

    init {
        searchButton.onAction = EventHandler { presenter.findDesiredFiles() }
        searchTextField.maxHeight = Double.MAX_VALUE
        searchTextField.promptText = "Enter the text you want to search"
        HBox.setHgrow(searchTextField, Priority.ALWAYS)
        directoryTextField.promptText = "Where to look"
        extensionsTextField.promptText = ".log"
        val searchHBox = HBox(searchTextField, searchButton)
        val directorySplitPane = SplitPane(directoryTextField, extensionsTextField)
        directorySplitPane.dividers[0].position = 0.7
        children.addAll(searchHBox, directorySplitPane)
    }

    fun getSearchQuery(): String {
        return searchTextField.text
    }

    fun getDirectoryPathQuery(): String {
        return directoryTextField.text
    }

    fun getExtensionsQuery(): String {
        return extensionsTextField.text
    }
}