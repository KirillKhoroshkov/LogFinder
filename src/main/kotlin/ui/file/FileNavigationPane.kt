package ui.file

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import java.util.*

class FileNavigationPane(indexesOfContainingSubstring: SortedSet<Int>, private val listView: ListView<*>) : HBox() {
    private val previousButton = Button("", ImageView(Image("up.png")))
    private val nextButton = Button("", ImageView(Image("down.png")))
    private val elements = indexesOfContainingSubstring.toList()
    private var currentElement = -1
    private val text = Text()

    init {
        val stackPane = StackPane(text)
        stackPane.maxHeight = Double.MAX_VALUE
        setHgrow(stackPane, Priority.ALWAYS)
        previousButton.onMouseClicked = EventHandler<MouseEvent> {
            selectPreviousElement()
        }
        nextButton.onMouseClicked = EventHandler<MouseEvent> {
            selectNextElement()
        }
        children.addAll(stackPane, previousButton, nextButton)
        selectCurrentElement()
    }

    private fun selectPreviousElement() {
        if (currentElement > 0) {
            currentElement--
        }
        selectCurrentElement()
    }

    private fun selectNextElement() {
        if (currentElement < elements.lastIndex) {
            currentElement++
        }
        selectCurrentElement()
    }

    private fun selectCurrentElement() {
        when {
            elements.isEmpty() -> {
                text.text = "No matches"
            }
            currentElement == -1 -> {
                text.text = "${elements.size} matches"
            }
            else -> {
                text.text = "${currentElement + 1} of ${elements.size} matches"
                listView.selectionModel.select(elements[currentElement])
                listView.scrollTo(elements[currentElement])
            }
        }
    }
}