package fr.uparis.projetmobile.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.projetmobile.R
import fr.uparis.projetmobile.viewModel.PlanteViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import android.view.MenuInflater


class ListFragmentToday : Fragment() {
    private lateinit var mPlanteViewModel: PlanteViewModel
    lateinit var adapter :ListeTodayAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val thiscontext=container?.context
        adapter=ListeTodayAdapter(thiscontext)
        val view = inflater.inflate(R.layout.fragment_list_today, container, false)
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mPlanteViewModel = ViewModelProvider(this).get(PlanteViewModel::class.java)
        if(adapter==null)

        mPlanteViewModel.lireDonnes.observe(
            viewLifecycleOwner,
            Observer { plante -> adapter?.setData(plante) })

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
                adapter?.setData(it)
            }  })
    }

}