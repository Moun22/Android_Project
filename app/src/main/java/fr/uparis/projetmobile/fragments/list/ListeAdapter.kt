package fr.uparis.projetmobile.fragments.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.projetmobile.R
import fr.uparis.projetmobile.model.Plante
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.plantes_lignes.view.*
import kotlinx.android.synthetic.main.plantes_lignes.view.NomC_txt
import kotlinx.android.synthetic.main.plantes_lignes.view.NomL_txt
import kotlinx.android.synthetic.main.plantes_lignes.view.date_txt
import kotlinx.android.synthetic.main.plantes_lignes.view.freq_txt
import kotlinx.android.synthetic.main.plantes_lignes.view.id_txt
import kotlinx.android.synthetic.main.plantes_lignes.view.imageView
import kotlinx.android.synthetic.main.plantes_lignes_today.view.*
import java.sql.Date
import java.util.*

class ListeAdapter: RecyclerView.Adapter <ListeAdapter.MyViewHolder>() {
    private var plantListes= mutableListOf<Plante>()

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plantes_lignes,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= plantListes[position]
        holder.itemView.id_txt.text= (position+1).toString()
        if(currentItem.nomCommun.isEmpty()) {
            holder.itemView.NomC_txt.text = currentItem.nomLatin


        }

        else{
            holder.itemView.NomC_txt.text=currentItem.nomCommun
            if (currentItem.nomLatin.isEmpty())
                holder.itemView.NomL_txt.text = ""
        else
                holder.itemView.NomL_txt.text = "("+currentItem.nomLatin+")"}


            holder.itemView.imageView.setImageURI(Uri.parse(currentItem.photo))




        holder.itemView.freq_txt.text=currentItem.frequance1.toString()+" chaque "+currentItem.frequance2.toString()+" jours"
        holder.itemView.date_txt.text=currentItem.dateprochain.toString()


        holder.itemView.ligneslayout.setOnClickListener {
            val action= ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return plantListes.size
    }
    fun setData(plante: List<Plante>){

        this.plantListes= plante as MutableList<Plante>
        notifyDataSetChanged()
    }

}