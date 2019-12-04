package view

import java.nio.file.Path
import java.util.*

interface Viewable {

    /**
     * @return Strings including the dot
     * @sample: [.sql, .kt, .java]
     * Always lowercase
     */
    fun getExtensions(): Iterable<String>

    fun getSearchQuery(): String

    fun getDirectoryPath(): String

    fun raiseAccessDeniedErrorMessage()

    fun raiseBrowsingDirectoryErrorMessage()

    fun raiseUnacceptableExtensionsErrorMessage()

    fun raiseUnacceptableSearchQueryErrorMessage()

    fun clearDirectoryContent()

    fun showDirectoryContent(directory: Path, content: Iterable<Path>, substring: String)

    fun showFile(path: Path, strings: Iterable<String>, indexesOfContainingSubstring: SortedSet<Int>)
}