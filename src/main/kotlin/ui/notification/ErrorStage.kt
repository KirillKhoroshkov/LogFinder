package ui.notification

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage

class ErrorStage(stageTitle: String, message: String) : Stage() {

    init {
        title = stageTitle
        val text = Text(message)
        val okButton = Button("Ok")
        okButton.onAction = EventHandler {
            this.close()
        }
        val borderPane = BorderPane()
        borderPane.top = text
        borderPane.bottom = okButton
        BorderPane.setAlignment(text, Pos.CENTER)
        BorderPane.setAlignment(okButton, Pos.BOTTOM_CENTER)
        BorderPane.setMargin(text, Insets(30.0))
        BorderPane.setMargin(okButton, Insets(25.0))
        initModality(Modality.APPLICATION_MODAL)
        val newScene = Scene(borderPane, 300.0, 150.0)
        this.scene = newScene
    }
}