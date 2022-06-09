package projet.echecmartien.controleurs

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import projet.echecmartien.modele.Jeu
import projet.echecmartien.vue.JeuVue
import projet.echecmartien.vue.MainVue

class ControleurRetour(vue: MainVue, primaryStage: Stage): EventHandler<ActionEvent> {
    val vue : MainVue
    val primaryStage : Stage

    init {
        this.vue = vue
        this.primaryStage = primaryStage
    }

    override fun handle(p0: ActionEvent?) {
        vue.textFieldPseudo2.text = vue.savePseudo2
        vue.textFieldPseudo2.style = "-fx-background-color:white ; -fx-border-color:lightgray"
        vue.textFieldPseudo2.isEditable = true
        vue.botButton.text = "Jouer contre un robot"
        vue.fixeListenerBouton(vue.botButton,ControleurBot(vue,primaryStage))
        vue.fixeListenerBouton(vue.playButton,ControleurPlayButton(vue, primaryStage))
    }
}