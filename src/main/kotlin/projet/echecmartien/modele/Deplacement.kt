package projet.echecmartien.modele

import projet.echecmartien.exceptions.DeplacementException


/**
 * cette classe permet de tester les déplacements sur le plateau de jeu
 *
 */

class Deplacement(origine : Coordonnee, destination : Coordonnee) {
    private var origine = origine
    private var destination = destination

    /**
     * dans le constructeur la validité du déplacement dans la grille est testée
     *@throws DeplacementException si le déplacement n'est ni horizontal, ni vertical est ni diagonal
     * les autres cas lèvent une IllegalArgumentException (peut être mis en place avec "require")
     */


    /**
     * getter
     * @return la destination de ce déplacement
     */
    fun getDestination():Coordonnee{
        return destination
    }


    /**
     * getter
     * @return l'origine de ce déplacement
     */
    fun getOrigine():Coordonnee{
       return origine
    }

    /**
     *méthode qui permet de tester si le déplacement est horizontal
     * @return true si le déplacement est horizontal, false sinon
     */
    fun estHorizontal() : Boolean {
        return (origine.getY() == destination.getY())
    }

    /**
     *méthode qui permet de tester si le déplacement est vertical
     * @return true si le déplacement est vertical, false sinon
     */
    fun estVertical(): Boolean {
        return (origine.getX() == destination.getX())
    }

    /**
     * méthode qui permet de tester si le déplacement est diagonal
     * @return true si le déplacement est diagonal, false sinon
     */
    fun estDiagonal():Boolean {
       return estDiagonalPositifXPositifY() || estDiagonalPositifXNegatifY() || estDiagonalNegatifXNegatifY() || estDiagonalNegatifXPositifY()
    }

    /**
     *méthode qui permet de calculer le nombre de case d'un déplacement
     * @return le nombre de case que le pion sera déplacée
     */
    fun longueur(): Int {
        if (estHorizontal()){
            if (estHorizontalPositif()){
                return destination.getX() - origine.getX()
            }else{
                return origine.getX() - destination.getX()
            }
        }else if (estVertical()){
            if (estVerticalPositif()){
                return destination.getY() - origine.getY()
            }else{
                return origine.getY() - destination.getY()
            }
        }else if (estDiagonal()){
            if (origine.getX() > destination.getX()){
                    return origine.getX() - destination.getX()
                }else{
                    return destination.getX() - origine.getX()
                }
            } else{
            throw DeplacementException()
        }
    }


    /**
     * méthode qui permet de déterminer le sens d'un déplacement vertical
     *
     *@return true si le déplacement est positif, false sinon
     */
    fun estVerticalPositif():Boolean{
        return origine.getY() < destination.getY()
    }

    /**
     * méthode qui permet de déterminer le sens d'un déplacement horizontal
     *
     * @return true si le déplacement est positif, false sinon
     */
    fun estHorizontalPositif():Boolean{
        return origine.getX() < destination.getX()
    }

    /**
     * méthode qui permet de déterminer si le sens d'un déplacement diagonal est positif en X et en Y
     *
     * @return true si le déplacement est positif en X et Y, false sinon
     */
    fun estDiagonalPositifXPositifY(): Boolean{
        return origine.getY() < destination.getY() && origine.getX()<destination.getX()
    }
    /**
     * méthode qui permet de déterminer si le sens d'un déplacement diagonal est négatif en X et positif en Y
     *
     * @return true si le déplacement est négatif en X et positif en Y, false sinon
     */
    fun estDiagonalNegatifXPositifY(): Boolean{
        return origine.getY() < destination.getY() && origine.getX()>destination.getX()
    }

    /**
     *
     * méthode qui permet de déterminer si le sens d'un déplacement diagonal est positif en X et négatif en Y
     *
     * @return true si le déplacement est positif en X et négatif en Y, false sinon
     */
    fun estDiagonalPositifXNegatifY(): Boolean{
        return origine.getY() > destination.getY() && origine.getX()<destination.getX()
    }

    /**
     * méthode qui permet de déterminer si le sens d'un déplacement diagonal est négatif en X et négatif en Y
     *
     * @return true si le déplacement est négatif en X et négatif en Y, false sinon
     */
    fun estDiagonalNegatifXNegatifY(): Boolean{
        return origine.getY() > destination.getY() && origine.getX()>destination.getX()
    }

    /**
     * donne le chemin de coordonnées que constitue le déplacement
     * du point de départ vers le point d'arrivée si le déplacement demandé est vertical.
     *
     * @return une liste de coordonnées qui constitue le déplacement du point de départ vers le point d'arrivée
     * si le déplacement est vertical. Le point de départ n'est pas stocké dans la liste.
     * @throws DeplacementException est levée si le déplacement n'est pas vertical
     */
    fun getCheminVertical(): List<Coordonnee> {
        var res = mutableListOf<Coordonnee>()
        if (estVerticalPositif()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX(),origine.getY()+i))
            }
        }else{
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX(),origine.getY()-i))
            }
        }
        return res
    }


    /**
     * donne le chemin de coordonnées que constitue le déplacement
     * du point de départ vers le point d'arrivée si le déplaceme{"origine Y dépasse"}nt demandé est horizontal.
     *
     * @return une liste de coordonnées qui constitue le déplacement du point de départ vers le point d'arrivée.
     * Le point de départ n'est pas stocké dans la liste.
     * si le déplacement est horizontal
     * @throws DeplacementException est levée si le déplacement n'est pas horizontal
     */
    fun getCheminHorizontal(): List<Coordonnee> {
        var res = mutableListOf<Coordonnee>()
        if (estVerticalPositif()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()-i,origine.getY()))
            }
        }else{
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()-i,origine.getY()))
            }
        }
        return res
    }

    /**
     * donne le chemin de coordonnées que constitue le déplacement
     * du point de départ vers le point d'arrivée si le déplacement demandé est diagonal.
     * Le point de départ n'est pas stocké dans la liste.
     *
     * @return une liste de coordonnées qui constitue le déplacement du point de départ vers le point d'arrivée
     * si le déplacement est diagonal
     * @throws DeplacementException est levée si le déplacement n'est pas diagonal
     */
    fun getCheminDiagonal(): List<Coordonnee> {
        var res = mutableListOf<Coordonnee>()
        if (estDiagonalNegatifXNegatifY()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()-i,origine.getY()-i))
            }
        }else if (estDiagonalNegatifXPositifY()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()-i,origine.getY()+i))
            }
        }else if (estDiagonalPositifXNegatifY()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()+i,origine.getY()-i))
            }
        }else if (estDiagonalPositifXPositifY()){
            for (i in 1 until longueur()){
                res.add(Coordonnee(origine.getX()+i,origine.getY()+i))
            }
        }
        return res
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return (other is Deplacement && origine == other.origine && destination == other.destination)
    }




}
