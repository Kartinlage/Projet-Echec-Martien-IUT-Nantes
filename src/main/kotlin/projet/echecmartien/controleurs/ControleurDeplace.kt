package projet.echecmartien.controleurs

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.input.MouseEvent
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import projet.echecmartien.exceptions.DeplacementException
import projet.echecmartien.modele.*
import projet.echecmartien.vue.JeuVue
import projet.echecmartien.vue.MainVue
import java.util.*
import kotlin.random.Random

class ControleurDeplace(private val vue: JeuVue, modele : Jeu) : EventHandler<MouseEvent>{
    val jeu = modele



    override fun handle(event: MouseEvent) {
        var row = GridPane.getRowIndex(event.source as Node)
        var column = GridPane.getColumnIndex(event.source as Node)
        jeu.setCoordDestinationDeplacement(Coordonnee(column,row))
        var originCords = jeu.getCoordOrigineDeplacement()!!
        var originRow = originCords.getY()
        var originColumn = originCords.getX()
        var type = jeu.plateau.getCases()[originColumn][originRow].getPion()!!.getScore()
        var typePris = 0
        if (!jeu.plateau.getCases()[column][row].estLibre()){
            typePris = jeu.plateau.getCases()[column][row].getPion()!!.getScore()
            jeu.getJoueurCourant()!!.pionCapture.add(jeu.plateau.getCases()[column][row].getPion()!!)
        }
        jeu.deplacer(originColumn,originRow,column,row)
        vue.setAsNull(vue.grille.children[originColumn*(vue.grille.rowCount)+originRow] as Circle,jeu)
        if (type == 1){
            vue.setAsPetitPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
        }else if (type == 2){
            vue.setAsMoyenPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
        }else if (type == 3){
            vue.setAsGrandPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
        }


        // Compteur des points
        if (!jeu.plateau.getCases()[column][row].estLibre() && jeu.plateau.getCases()[column][row].getJoueur() != jeu.getJoueurCourant()){
            if (jeu.getJoueurCourant()!!.nom == vue.joueur1.text){
                vue.point1.text = "${jeu.getJoueurCourant()!!.calculerScore()} Points"
                if (typePris == 1){
                    vue.nbPetit2.text = (vue.nbPetit2.text.toInt() + 1).toString()
                }
                if (typePris == 2){
                    vue.nbMoyen2.text = (vue.nbMoyen2.text.toInt() + 1).toString()
                }
                if (typePris == 3){
                    vue.nbGrand2.text = (vue.nbGrand2.text.toInt() + 1).toString()
                }
            }
            if (jeu.getJoueurCourant()!!.nom == vue.joueur2.text){
                vue.point2.text = "${jeu.getJoueurCourant()!!.calculerScore()} Points"
                if (typePris == 1 ){
                    vue.nbPetit.text = (vue.nbPetit.text.toInt() + 1).toString()
                }
                if (typePris == 2 ){
                    vue.nbMoyen.text = (vue.nbMoyen.text.toInt() + 1).toString()
                }
                if (typePris == 3 ){
                    vue.nbGrand.text = (vue.nbGrand.text.toInt() + 1).toString()
                }
            }
        }
        jeu.changeJoueurCourant()

        vue.update(jeu)

        vue.nbTour += 1
        vue.compteTour.text = "Tour ${vue.nbTour}"
        vue.tourSansPrises.text = "Tours sans prises : ${jeu.getNombreCoupsSansPrise()}"

        //playerturn
        if (Joueur(vue.joueur1.text) == jeu.getJoueurCourant()){
            vue.joueur2.style = "-fx-font-weight : bold; -fx-text-fill : red;"
            vue.joueur2.style = ""
        }else{
            vue.joueur2.style = "-fx-font-weight : bold; -fx-text-fill : red;"
            vue.joueur1.style = ""
        }

        if (jeu.arretPartie()){
            if (jeu.joueurVainqueur()!!.nom == jeu.getJoueurCourant()!!.nom){
                jeu.changeJoueurCourant()
            }
            val dialog = Alert(Alert.AlertType.INFORMATION)
            dialog.title="Fin de partie"
            dialog.headerText="La partie est terminée"
            dialog.contentText = "Le gagnant est ${jeu.joueurVainqueur()!!.nom} avec ${jeu.joueurVainqueur()!!.calculerScore()} Point(s) \n " + "Le joueur ${jeu.getJoueurCourant()!!.nom} à perdu, il avait ${jeu.getJoueurCourant()!!.calculerScore()} Point(s) "
            dialog.showAndWait()
        }




        //contrôle IA

        if (vue.IActive == true){
            var listeDeplace = mutableListOf<Coordonnee>()
            var listeDeplaceCos = mutableListOf<Coordonnee>()
            var listePrendre = mutableListOf<Coordonnee>()
            var listePrendreCos = mutableListOf<Coordonnee>()
            for (row in 4 until 8){
                for (column in 0 until 4){
                    for (i in 0 until 8){
                        for(j in 0 until 4){
                            if (vue.grille.children[i*(vue.grille.columnCount)+j] is Circle && jeu.plateau.getCases()[column][row].getPion() != null){
                                try {
                                    jeu.plateau.getCases()[column][row].getPion()!!.getDeplacement(Deplacement(Coordonnee(column,row),Coordonnee(j,i)))
                                    if(!jeu.deplacementPossible(column,row,j,i,jeu.plateau.getCases()[column][row].getJoueur())){
                                        throw DeplacementException()
                                    }
                                    vue.fixeListenerCase((vue.grille.children[j*(vue.grille.rowCount)+i] as Circle),ControleurDeplace(vue,jeu))
                                    if (jeu.plateau.getCases()[j][i].getPion() == null){
                                        listeDeplace.add(Coordonnee(j,i))
                                        listeDeplaceCos.add(Coordonnee(column,row))
                                    }else{
                                        listePrendre.add(Coordonnee(j,i))
                                        listePrendreCos.add(Coordonnee(column,row))
                                    }
                                }catch (e : DeplacementException){
                                }
                            }

                        }
                    }
                }
            }

            if (listePrendre.size != 0){
                triPrendre(listePrendre,listePrendreCos)
            }else{
                triDeplace(listeDeplace,listeDeplaceCos)
                var rand = Random.nextInt(0,listeDeplace.size)
                jeu.setCoordDestinationDeplacement(Coordonnee(listeDeplace[rand].getX(),listeDeplace[rand].getY()))
                jeu.setCoordOrigineDeplacement(listeDeplaceCos[rand])
            }

            originCords = jeu.getCoordOrigineDeplacement()!!
            originColumn = originCords.getX()
            originRow = originCords.getY()
            var destiCords = jeu.getCoordDestinationDeplacement()!!
            column = destiCords.getX()
            row = destiCords.getY()

            type = jeu.plateau.getCases()[originColumn][originRow].getPion()!!.getScore()
            typePris = 0
            if (!jeu.plateau.getCases()[column][row].estLibre()){
                typePris = jeu.plateau.getCases()[column][row].getPion()!!.getScore()
                jeu.getJoueurCourant()!!.pionCapture.add(jeu.plateau.getCases()[column][row].getPion()!!)
            }

            //fun contrôle IA

            jeu.deplacer(originColumn,originRow,column,row)

            vue.setAsNull(vue.grille.children[originColumn*(vue.grille.rowCount)+originRow] as Circle,jeu)
            if (type == 1){
                vue.setAsPetitPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
            }else if (type == 2){
                vue.setAsMoyenPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
            }else if (type == 3){
                vue.setAsGrandPion(vue.grille.children[column*(vue.grille.rowCount)+row] as Circle,jeu)
            }


            // Compteur des points
            if (!jeu.plateau.getCases()[column][row].estLibre() && jeu.plateau.getCases()[column][row].getJoueur() != jeu.getJoueurCourant()){
                if (jeu.getJoueurCourant()!!.nom == vue.joueur1.text){
                    vue.point1.text = "${jeu.getJoueurCourant()!!.calculerScore()} Points"
                    if (typePris == 1){
                        vue.nbPetit2.text = (vue.nbPetit2.text.toInt() + 1).toString()

                    }
                    if (typePris == 2){
                        vue.nbMoyen2.text = (vue.nbMoyen2.text.toInt() + 1).toString()
                    }
                    if (typePris == 3){
                        vue.nbGrand2.text = (vue.nbGrand2.text.toInt() + 1).toString()
                    }
                }
                if (jeu.getJoueurCourant()!!.nom == vue.joueur2.text){
                    vue.point2.text = "${jeu.getJoueurCourant()!!.calculerScore()} Points"
                    if (typePris == 1 ){
                        vue.nbPetit.text = (vue.nbPetit.text.toInt() + 1).toString()
                    }
                    if (typePris == 2 ){
                        vue.nbMoyen.text = (vue.nbMoyen.text.toInt() + 1).toString()
                    }
                    if (typePris == 3 ){
                        vue.nbGrand.text = (vue.nbGrand.text.toInt() + 1).toString()
                    }
                }
            }
            jeu.changeJoueurCourant()

            vue.update(jeu)

            vue.nbTour += 1
            vue.compteTour.text = "Tour ${vue.nbTour}"
            vue.tourSansPrises.text = "Tours sans prises : ${jeu.getNombreCoupsSansPrise()}"

            //playerturn
            if (Joueur(vue.joueur1.text) == jeu.getJoueurCourant()){
                vue.joueur1.style = "-fx-font-weight : bold;"
                vue.joueur2.style = ""
            }else{
                vue.joueur2.style = "-fx-font-weight : bold;"
                vue.joueur1.style = ""
            }

            if (jeu.arretPartie()){
                if (jeu.joueurVainqueur()!!.nom == jeu.getJoueurCourant()!!.nom){
                    jeu.changeJoueurCourant()
                }
                val dialog = Alert(Alert.AlertType.INFORMATION)
                dialog.title="Fin de partie"
                dialog.headerText="La partie est terminée"
                dialog.contentText = "Le gagnant est ${jeu.joueurVainqueur()!!.nom} avec ${jeu.joueurVainqueur()!!.calculerScore()} Point(s) \n " + "Le joueur ${jeu.getJoueurCourant()!!.nom} à perdu, il avait ${jeu.getJoueurCourant()!!.calculerScore()} Point(s) "
                dialog.showAndWait()
            }
        }


    }

    fun triPrendre(liste : MutableList<Coordonnee>,listeCos : MutableList<Coordonnee>){
        var max = 1
        var listeMax = mutableListOf<Coordonnee>()
        var listeMaxCos = mutableListOf<Coordonnee>()
        for (i in 0 until liste.size){
            if (jeu.plateau.getCases()[liste[i].getX()][liste[i].getY()].getPion()!!.getScore() == max){
                listeMax.add(liste[i])
                listeMaxCos.add(listeCos[i])
            }else if(jeu.plateau.getCases()[liste[i].getX()][liste[i].getY()].getPion()!!.getScore() > max){
                listeMax = mutableListOf()
                listeMax.add(liste[i])
                listeMaxCos.add(listeCos[i])
                max = jeu.plateau.getCases()[liste[i].getX()][liste[i].getY()].getPion()!!.getScore()
            }
        }
        var rand = Random.nextInt(0,listeMax.size)
        jeu.setCoordDestinationDeplacement(listeMax[rand])
        jeu.setCoordOrigineDeplacement(listeMaxCos[rand])
    }

    fun triDeplace(liste: MutableList<Coordonnee>,listeCos : MutableList<Coordonnee>){
        var listePris = mutableListOf<Coordonnee>()
        var listePrisCos = mutableListOf<Coordonnee>()
        for (i in 0 until liste.size){
            for (j in 0 until 4){
                for (k in 0 until 4){
                    if (jeu.plateau.getCases()[j][k].getPion() != null){
                        try {
                            (jeu.plateau.getCases()[j][k].getPion()!!.getDeplacement(Deplacement(Coordonnee(j,k),liste[i])))
                            if (liste[i] !in listePris){
                                listePris.add(liste[i])
                                listePrisCos.add(listeCos[i])
                            }
                        }catch (_: DeplacementException){
                        }
                    }
                }
            }
        }
        var listeDispo = mutableListOf<Coordonnee>()
        var listeDispoCos = mutableListOf<Coordonnee>()
        for (i in 0 until liste.size){
            if (liste[i] !in listePris){
                listeDispo.add(liste[i])
                listeDispoCos.add(listeCos[i])
            }
        }
        if (listeDispo.size != 0){
            var rand = Random.nextInt(0,listeDispo.size)
            jeu.setCoordDestinationDeplacement(listeDispo[rand])
            jeu.setCoordOrigineDeplacement(listeDispoCos[rand])
        }else{
            var rand = Random.nextInt(0,liste.size)
            jeu.setCoordDestinationDeplacement(liste[rand])
            jeu.setCoordOrigineDeplacement(listeCos[rand])
        }
        println(listeDispo)
        println(listeDispoCos)
    }
}
