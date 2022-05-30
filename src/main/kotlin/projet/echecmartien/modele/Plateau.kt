package projet.echecmartien.modele

import projet.echecmartien.librairie.GeneralData


class Plateau {

    private var cases : Array<Array<Case>>
    private val tailleHorizontale : Int
    private val tailleVerticale : Int

    init {
        this.tailleVerticale = 8
        this.tailleHorizontale = 4
        var caseFant : MutableList<Array<Case>> = mutableListOf()
        var fantoche : MutableList<Case>
        for (i in 0 until tailleHorizontale){
            fantoche = mutableListOf()
            for (j in 0 until tailleVerticale){
                fantoche.add(Case())
            }
            caseFant.add(fantoche.toTypedArray())
        }
        cases = caseFant.toTypedArray()
    }

    /**
     * initialise le plateau de jeu avec les pions
     */

    fun initialiser() {
        var tab = GeneralData().tableau
        for(i in 0 until tailleHorizontale){
            for ( j in 0 until tailleVerticale){
                if (tab[i][j].valeur == "M"){
                    cases[i][j].setPion(MoyenPion())
                }
                if (tab[i][j].valeur == "G"){
                    cases[i][j].setPion(GrandPion())
                }
                if (tab[i][j].valeur == "P"){
                    cases[i][j].setPion(PetitPion())
                }
            }
        }
    }



    /**
     * donne la taille horizontale du plateau
     * @return la taille horizontale du plateau
     */
    fun getTailleHorizontale(): Int {
        return this.tailleHorizontale
    }


    /**
     * donne la taille verticale du plateau
     * @return la taille verticale du plateau
     */
    fun getTailleVerticale(): Int {
        return this.tailleVerticale
    }


    /**
     * donne le tableau des cases du plateau
     * @return les cases du plateau
     */
    fun getCases(): Array<Array<Case>> {
        return cases

    }


}