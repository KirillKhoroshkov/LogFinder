package ui.file

import javafx.collections.ListChangeListener
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import java.nio.file.Path
import java.util.*

class FileTabPane : TabPane() {
    private val openTabs = mutableMapOf<Path, Tab>()

    init {
        tabs.addListener(ListChangeListener<Tab> { change ->
            while (change.next()) {
                change.removed.forEach { removed ->
                    openTabs.filterValues { open ->
                        removed == open
                    }.forEach { (key, _) ->
                        openTabs.remove(key)
                    }
                }
            }
        })
    }

    fun showFile(path: Path, strings: Iterable<String>, indexesOfContainingSubstring: SortedSet<Int>) {
        if (openTabs.containsKey(path)) {
            selectionModel.select(openTabs[path])
        } else {
            val newTab = FileTab(strings, indexesOfContainingSubstring)
            newTab.text = path.fileName.toString()
            tabs.add(newTab)
            openTabs[path] = newTab
        }
    }
}