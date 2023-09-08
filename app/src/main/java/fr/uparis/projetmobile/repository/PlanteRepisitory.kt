package fr.uparis.projetmobile.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import fr.uparis.projetmobile.bd.PlanteDao
import fr.uparis.projetmobile.model.Plante


class PlanteRepisitory (private val planteDao: PlanteDao) {
    val lireLesPlantes: LiveData<List<Plante>> = planteDao.LireLesPlantes()
       fun ajoutPlante(plante: Plante){
        planteDao.ajoutPlante(plante)
    }
    fun updatePlante(plante: Plante){
        planteDao.updatePlante(plante)
    }
    fun deletePlante(plante: Plante){
        planteDao.supprimerPlante(plante)

    }
    fun chercherPlante(searchQuery: String) : LiveData<List<Plante>>{

        return planteDao.ChercherPlante(searchQuery)
    }
}