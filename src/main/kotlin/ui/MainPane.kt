package ui

import exception.IllegalExtensionException
import exception.IllegalSearchQueryException
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import presenter.Presenter
import ui.directory.DirectoryTreePane
import ui.file.FileTabPane
import ui.notification.Notifier
import ui.search.SearchPane
import view.Viewable
import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.util.*

class MainPane(presenter: Presenter) : BorderPane(), Viewable {
    private val searchPane = SearchPane(presenter)
    private val directoryTreePane = DirectoryTreePane(presenter)
    private val fileTabPane = FileTabPane()
    private val notifier = Notifier()

    init {
        val splitPane = SplitPane(directoryTreePane, fileTabPane)
        top = searchPane
        center = splitPane
        splitPane.dividers[0].position = 0.3
    }

    /**
     * @return Strings including the dot
     * @sample: [.sql, .kt, .java]
     * Always lowercase
     */
    override fun getExtensions(): Iterable<String> {
        val extensionsQuery = searchPane.getExtensionsQuery()
        if (extensionsQuery.isEmpty()) {
            return listOf(".log")
        }
        if (extensionsQuery.contains("/")) {
            throw IllegalArgumentException("Extension may not contains '/'")
        }
        val rawExtensions = extensionsQuery
            .split(Regex(" +"))
            .toMutableList()
        if (rawExtensions.isNotEmpty() && rawExtensions[rawExtensions.lastIndex].isEmpty()) {
            rawExtensions.removeAt(rawExtensions.lastIndex)
        }
        if (rawExtensions.isNotEmpty() && rawExtensions[0].isEmpty()) {
            rawExtensions.removeAt(0)
        }
        val extensions = rawExtensions
            .map { if (it.first() == '.') it else ".$it" }
            .map { it.toLowerCase() }
        for (extension in extensions) {
            if (extension.length == 1) { // contains a dot only
                throw IllegalExtensionException("Extension may not be empty")
            }
        }
        return extensions
    }

    override fun getSearchQuery(): String {
        val searchQuery = searchPane.getSearchQuery()
        if (searchQuery.isEmpty()) {
            throw IllegalSearchQueryException("Search query may not be empty")
        } else {
            return searchQuery
        }
    }

    override fun getDirectoryPath(): String {
        return searchPane.getDirectoryPathQuery()
    }

    override fun raiseAccessDeniedErrorMessage() {
        notifier.raiseAccessDeniedErrorMessage()
    }

    override fun raiseBrowsingDirectoryErrorMessage() {
        notifier.raiseBrowsingDirectoryErrorMessage()
    }

    override fun raiseUnacceptableExtensionsErrorMessage() {
        notifier.raiseUnacceptableExtensionsErrorMessage()
    }

    override fun raiseUnacceptableSearchQueryErrorMessage() {
        notifier.raiseUnacceptableSearchQueryErrorMessage()
    }

    override fun clearDirectoryContent() {
        directoryTreePane.clearDirectoryContent()
    }

    override fun showDirectoryContent(directory: Path, content: Iterable<Path>, substring: String) {
        directoryTreePane.showDirectoryContent(directory, content, substring)
    }

    override fun showFile(path: Path, strings: Iterable<String>, indexesOfContainingSubstring: SortedSet<Int>) {
        fileTabPane.showFile(path, strings, indexesOfContainingSubstring)
    }
}