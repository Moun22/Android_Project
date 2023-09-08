package fr.uparis.projetmobile.bd

import android.content.Context
import androidx.room.*
import fr.uparis.projetmobile.model.Plante

@Database(entities = [Plante::class], version =1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class PlanteBD: RoomDatabase (){
    abstract fun plantedao(): PlanteDao
    companion object{
        private var INSTANCE: PlanteBD?=null

        fun getBD(context: Context): PlanteBD{
            val tempISTANCE = INSTANCE
            if(tempISTANCE != null){
                return tempISTANCE
            }
            synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,
                PlanteBD::class.java , "plante_bd ").build()
                INSTANCE= instance
                return  instance
            }
        }
    }
}