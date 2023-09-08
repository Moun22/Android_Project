package fr.uparis.projetmobile.bd

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import fr.uparis.projetmobile.model.Plante
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun ajoutPlante( plante: Plante)

     @Update
     fun updatePlante(plante: Plante)

     @Delete
     fun supprimerPlante(plante: Plante)

    @Query("SELECT * FROM info_plante ORDER BY id ASC")
    fun LireLesPlantes(): LiveData<List<Plante>>

    @Query("SELECT * FROM info_plante Where nomCommun LIKE :searchQuery OR nomLatin LIKE :searchQuery")
    fun ChercherPlante(searchQuery: String): LiveData<List<Plante>>

}