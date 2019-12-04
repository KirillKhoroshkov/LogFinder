package presenter

import exception.*
import javafx.application.Platform.runLater
import view.Viewable
import java.io.*
import java.nio.file.*
import java.nio.file.Files.isRegularFile
import java.util.stream.Collectors
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class Presenter {
    lateinit var view: Viewable
    private val threadManager = ThreadManager()

    fun findDesiredFiles() {
        threadManager.executeNewTaskIfOldDone(Runnable {
            try {
                runLater { view.clearDirectoryContent() }
                val searchQuery = view.getSearchQuery()
                val extensions = view.getExtensions()
                val directoryPathString = view.getDirectoryPath()
                val directoryPath = Paths.get(directoryPathString)
                val contentPaths = getDesiredPaths(directoryPath, extensions, searchQuery)
                runLater { view.showDirectoryContent(directoryPath, contentPaths, searchQuery) }
            } catch (e: UncheckedIOException) {
                if (e.cause is AccessDeniedException) {
                    runLater { view.raiseAccessDeniedErrorMessage() }
                } else {
                    runLater { view.raiseBrowsingDirectoryErrorMessage() }
                }
            } catch (e: IOException) {
                runLater { view.raiseBrowsingDirectoryErrorMessage() }
            } catch (e: IllegalExtensionException) {
                runLater { view.raiseUnacceptableExtensionsErrorMessage() }
            } catch (e: IllegalSearchQueryException) {
                runLater { view.raiseUnacceptableSearchQueryErrorMessage() }
            }
        })
    }

    private fun getDesiredPaths(directory: Path, extensions: Iterable<String>, substring: String): Iterable<Path> {
        return Files.walk(directory).use { walk ->
            walk.filter { isRegularFile(it) }
                .filter { isFileWithDesiredExtension(it, extensions) }
                .filter { isFileWithDesiredSubstring(it, substring) }
                .collect(Collectors.toList())
        }
    }

    private fun isFileWithDesiredExtension(path: Path, extensions: Iterable<String>): Boolean {
        val fileName = path.fileName.toString().toLowerCase()
        for (extension in extensions) {
            if (fileName.length >= extension.length && fileName.takeLast(extension.length) == extension) {
                return true
            }
        }
        return false
    }

    private fun isFileWithDesiredSubstring(path: Path, substring: String): Boolean {
        val file = path.toFile()
        val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file)))
        var result = false
        bufferedReader.use {
            var currentLine: String?
            do {
                currentLine = bufferedReader.readLine()
                if (currentLine != null && currentLine.contains(substring)) {
                    result = true
                    break
                }
            } while (currentLine != null)
        }
        return result
    }

    fun openFile(path: Path, substring: String) {
        threadManager.executeNewTaskIfOldDone(Runnable {
            val strings = mutableListOf<String>()
            val indexesOfContainingSubstring = sortedSetOf<Int>()
            path.toFile().forEachLine {
                strings.add(it)
                if (it.contains(substring)) {
                    indexesOfContainingSubstring.add(strings.lastIndex)
                }
            }
            runLater { view.showFile(path, strings, indexesOfContainingSubstring) }
        })
    }

    private class ThreadManager {
        private var futureTask: FutureTask<Unit>? = null

        @Synchronized
        fun executeNewTaskIfOldDone(runnable: Runnable): Boolean {
            if (futureTask == null || futureTask!!.isDone) {
                val callable = Callable {
                    runnable.run()
                }
                futureTask = FutureTask(callable)
                Thread(futureTask).start()
                return true
            } else {
                return false
            }
        }
    }
}