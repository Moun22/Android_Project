package fr.uparis.projetmobile.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import fr.uparis.projetmobile.bd.PlanteBD
import fr.uparis.projetmobile.bd.PlanteDao
import fr.uparis.projetmobile.repository.PlanteRepisitory
import fr.uparis.projetmobile.model.Plante
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanteViewModel(application: Application): AndroidViewModel(application) {
     val lireDonnes: LiveData<List<Plante>>
    private val repisitory: PlanteRepisitory
    init {
        val planteDao= PlanteBD.getBD(application).plantedao()
        repisitory = PlanteRepisitory(planteDao)
        lireDonnes = repisitory.lireLesPlantes
    }

    fun ajoutPlante(plante: Plante){
        viewModelScope.launch(Dispatchers.IO) { repisitory.ajoutPlante(plante) }
    }
    fun updatePlante(plante: Plante) {
        viewModelScope.launch(Dispatchers.IO) {
            repisitory.updatePlante(plante)
        }
    }

    fun suprimmerPlante(plante: Plante){
        viewModelScope.launch(Dispatchers.IO) {repisitory.deletePlante(plante)
             }
    }

    fun chercherPlante(searchQuery: String): LiveData<List<Plante>>
    {
        return  repisitory.chercherPlante(searchQuery)
    }

}