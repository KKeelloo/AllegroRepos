package com.example.allegrorepos

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.allegrorepos.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {

    companion object{
        val mapOfStringResources = mapOf(
            0 to R.string.name,
            1 to R.string.description,
            2 to R.string.created_date,
            3 to R.string.size,
            4 to R.string.stars,
            5 to R.string.language,
            6 to R.string.forks,
            7 to R.string.issues,
            8 to R.string.branch,
            9 to R.string.archived
        )
    }

    private val sharedInfoViewModel: SharedInfoViewModel by activityViewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(detailsViewModel.details.value==null)
            binding.listDetails.adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1)
        else {
            val details = detailsViewModel.details.value!!
            val tmp = Array(mapOfStringResources.size){
                detailsUnPacker(it, details)
            }
            binding.listDetails.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, tmp)
        }

        binding.listDetails.setOnItemClickListener{_,_,position,_->
            when(position){
                4,6,7 -> {
                    sharedInfoViewModel.choseDetail(position, detailsViewModel.details.value?.fetchFromPosition(position)?.toInt() ?: 0)
                    view.findNavController().navigate(R.id.action_detailsFragment_to_chosenDetailFragment)
                }
                else -> {
                    Toast.makeText(view.context, getString(R.string.no_more_detail), Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailsViewModel.statusInfo.observe(viewLifecycleOwner){ value ->
            value.let{
                Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
            }
        }

        detailsViewModel.details.observe(viewLifecycleOwner){ value ->
            value.let { details ->
                val tmp = Array(mapOfStringResources.size){
                    detailsUnPacker(it, details)
                }
                binding.listDetails.adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, tmp)
            }

        }

        detailsViewModel.fetchDetails(sharedInfoViewModel.repo!!, sharedInfoViewModel.username, sharedInfoViewModel.token)
    }


    private fun detailsUnPacker(it: Int, details: RepoDetailsHolder): String{
        return  getText(mapOfStringResources[it]!!).toString() +
                when (it) {
                    9 -> {

                                when {
                                    details.fetchFromPosition(it) == null -> getText(R.string.no_info)
                                    details.fetchFromPosition(it)!!.contentEquals("true") -> getText(R.string.archived_yes)
                                    else -> getText(R.string.archived_no)
                                }
                    }
                    2 ->  {
                        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        val date: Date = format.parse(details.fetchFromPosition(it)!!)!!
                        date
                    }
                    else -> (details.fetchFromPosition(it)?:getText(R.string.no_info))
                }
    }

}