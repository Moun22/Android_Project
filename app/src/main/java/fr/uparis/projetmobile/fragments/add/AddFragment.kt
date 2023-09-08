package fr.uparis.projetmobile.fragments.add

import android.R.attr
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.uparis.projetmobile.R
import fr.uparis.projetmobile.model.Plante
import fr.uparis.projetmobile.viewModel.PlanteViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Date
import java.time.LocalDate
import android.R.attr.data
import androidx.core.net.toUri
import java.io.File


class AddFragment : Fragment() {
private lateinit var  mPlanteViewModel: PlanteViewModel
    var localUri : Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val callback= object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }

        }
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

        }


        requireActivity().onBackPressedDispatcher.addCallback(callback)
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add, container, false)
        mPlanteViewModel= ViewModelProvider(this).get(PlanteViewModel::class.java)
        view.add_btn.setOnClickListener {  insertDatatoDataBase() }

        view.choixImage.setOnClickListener {  getContent.launch("image/*")}
        return  view
    }

    private fun insertDatatoDataBase() {
        val nomC= addNomC_et.text.toString()
        val nomL= addNomL_et.text.toString()

        val fr1= frequenc1.text.toString()
        val fr2= frequence2.text.toString()
        val frequence1= if(TextUtils.isEmpty(fr1) ) 1 else fr1.toInt()
        val frequence2= if(TextUtils.isEmpty(fr2) ) 1 else fr2.toInt()
        if (checkPlante(nomC,nomL)){
            val date= LocalDate.now().plusDays((frequence1/frequence2).toLong())

            val plante= Plante(0,nomC,nomL,frequence1,frequence2,Date.valueOf(date.toString()),localUri.toString())
            mPlanteViewModel.ajoutPlante(plante)
            Toast.makeText(requireContext(), "Plante ajoutée", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else {
            Toast.makeText(requireContext(), "Veuillez indiquer un nom commun ou latin", Toast.LENGTH_SHORT).show()
        }

    }
    private  fun checkPlante(nomC:String,nomL:String):Boolean{
         return (!TextUtils.isEmpty(nomC) || !TextUtils.isEmpty(nomL) )

    }


}


