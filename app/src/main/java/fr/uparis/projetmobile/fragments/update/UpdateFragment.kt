package fr.uparis.projetmobile.fragments.update

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.sqlite.db.SimpleSQLiteQuery
import fr.uparis.projetmobile.R
import fr.uparis.projetmobile.bd.PlanteDao
import fr.uparis.projetmobile.model.Plante
import fr.uparis.projetmobile.viewModel.PlanteViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.fragment_update.view.choixImage
import java.io.File
import java.sql.Date
import java.time.LocalDate


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mPlanteViewModel:PlanteViewModel
    var localUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callback= object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_update, container, false)
            mPlanteViewModel=ViewModelProvider(this).get(PlanteViewModel::class.java  )
        localUri= Uri.parse(args.currentPlante.photo)
            view.updateNomC_et.setText(args.currentPlante.nomCommun)
            view.updateNomL_et.setText(args.currentPlante.nomLatin)
            view.updatefrequenc1.setText(args.currentPlante.frequance1.toString())
            view.updatefrequence2.setText(args.currentPlante.frequance2.toString())
        view.imageView2.setImageURI(localUri)

        setHasOptionsMenu(true);
        view.update_btn.setOnClickListener {updatePlante()  }
        val getContent = registerForActivityResult(ActivityResultContracts .GetContent())
        { uri: Uri? ->
            if( uri == null ) return@registerForActivityResult
            val inputStream = requireActivity().getContentResolver().openInputStream(uri)

            val fileNamePrefix = "plante";
            val preferences = requireActivity().getSharedPreferences("numImage", Context. MODE_PRIVATE)
            val numImage = preferences.getInt("numImage", 1)
            val fileName = "$fileNamePrefix$numImage"

            val file = File(requireActivity().filesDir, fileName)
            val outputStream = file.outputStream()

            preferences.edit().putInt("numImage",numImage+1).commit()

            inputStream?.copyTo(outputStream)

            localUri = file.toUri()
            outputStream.close()
            inputStream?.close()
            view.imageView2.setImageURI(localUri)

        }
        view.choixImage.setOnClickListener {  getContent.launch("image/*")
        }


        return view
    }
    private  fun updatePlante(){
        val nomC= updateNomC_et.text.toString()
        val nomL= updateNomL_et.text.toString()
        val fr1= updatefrequenc1.text.toString()
        val fr2= updatefrequence2.text.toString()
        val frequence1= if(TextUtils.isEmpty(fr1) ) args.currentPlante.frequance1 else fr1.toInt()
        val frequence2= if(TextUtils.isEmpty(fr2) ) args.currentPlante.frequance2  else fr2.toInt()

        if (checkPlante(nomC,nomL)){
            val date= LocalDate.now().plusDays((frequence1/frequence2).toLong())

            val updateplante= Plante(args.currentPlante.id,nomC,nomL,frequence1,frequence2, Date.valueOf(date.toString()),localUri.toString())
            mPlanteViewModel.updatePlante(updateplante)

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

            Toast.makeText(requireContext(), "Informations de la plante modifiées", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(requireContext(), "Veuillez indiquer un nom commun ou latin", Toast.LENGTH_SHORT).show()

    }
    private  fun checkPlante(nomC:String,nomL:String):Boolean{
        return (!TextUtils.isEmpty(nomC) || !TextUtils.isEmpty(nomL) )

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.delete_menu) {
            deletePlante()
        }
            return super.onOptionsItemSelected(item)

    }

    private fun deletePlante() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Oui"){_, _-> mPlanteViewModel.suprimmerPlante(args.currentPlante)
        Toast.makeText(requireContext(),"Plante supprimée",Toast.LENGTH_SHORT ).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Non"){_,_->}
        builder.setTitle("Supprimmer une plante")
        if(args.currentPlante.nomCommun.isEmpty())
        builder.setMessage("Voulez-vous supprimer la plante  ${args.currentPlante.nomLatin}?")
        else
            builder.setMessage("Voulez-vous supprimer la plante  ${args.currentPlante.nomCommun}?")
    builder.create().show()
    }
}