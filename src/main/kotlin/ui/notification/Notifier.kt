package ui.notification

class Notifier {

    fun raiseBrowsingDirectoryErrorMessage() {
        ErrorStage("Browsing directory error", "Can not browse directory").show()
    }

    fun raiseAccessDeniedErrorMessage() {
        ErrorStage("Access denied error", "Can not read files in this directory").show()
    }

    fun raiseUnacceptableExtensionsErrorMessage() {
        ErrorStage("Unacceptable extensions error", "Some extensions are not allowed").show()
    }

    fun raiseUnacceptableSearchQueryErrorMessage() {
        ErrorStage("Unacceptable search query error", "This search query is unacceptable").show()
    }
}