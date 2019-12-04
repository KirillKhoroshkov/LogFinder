
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import presenter.Presenter
import ui.MainPane

class Main : Application() {
    private val presenter = Presenter()
    private val mainPane = MainPane(presenter)
    private val scene = Scene(mainPane, 1000.0, 600.0)

    init {
        presenter.view = mainPane
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = "LogFinder"
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}