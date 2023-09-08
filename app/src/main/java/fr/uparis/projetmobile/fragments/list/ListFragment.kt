package fr.uparis.projetmobile.fragments.list

import android.os.Bundle
import android.view.*
import android.view.View.inflate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.projetmobile.R
import fr.uparis.projetmobile.viewModel.PlanteViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import android.view.MenuInflater
import fr.uparis.projetmobile.MainActivity
import kotlinx.android.synthetic.main.fragment_add.view.*
import android.app.SearchManager
import android.content.Context
import android.view.View.VIEW_LOG_TAG


class ListFragment : Fragment() {
    private lateinit var mPlanteViewModel: PlanteViewModel
    var adapter = ListeAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mPlanteViewModel = ViewModelProvider(this).get(PlanteViewModel::class.java)
        mPlanteViewModel.lireDonnes.observe(
            viewLifecycleOwner,
            Observer { plante -> adapter.setData(plante) })
        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        view.today.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_listFragmentToday)
        }
        return view
    }

    override  fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true


            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if(p0!=null)
                        searchDatabase(p0)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if(p0!=null)
                        searchDatabase(p0)
                    return true
                }

            })




    }

    private fun searchDatabase(query:String){

        val searchQuery ="%$query%"
        mPlanteViewModel.chercherPlante(searchQuery).observe(
            this,
            Observer { list -> list.let{
                adapter.setData(it)
            }  })
    }

}