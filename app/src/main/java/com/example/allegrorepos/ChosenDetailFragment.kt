package com.example.allegrorepos

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.allegrorepos.databinding.FragmentChosenDetailBinding

class ChosenDetailFragment : Fragment() {
    companion object{
        val mapOfInfo = mapOf(
                4 to R.string.info_name,
                6 to R.string.info_name,
                7 to R.string.info_title
        )
    }

    private val sharedInfoViewModel: SharedInfoViewModel by activityViewModels()
    private val chosenDetailsViewModel: ChosenDetailsViewModel by viewModels()
    private lateinit var binding: FragmentChosenDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chosen_detail, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listChosenDetails.adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1)
        chosenDetailsViewModel.repo = sharedInfoViewModel.repo!!

        sharedInfoViewModel.chosenDetail.observe(viewLifecycleOwner){
            binding.txtInfo.text = getText(mapOfInfo[it]!!)
            chosenDetailsViewModel.numOfDetails = sharedInfoViewModel.numberOfDetails
            chosenDetailsViewModel.fetchDetailsOfDetail(it)
        }

        chosenDetailsViewModel.statusInfo.observe(viewLifecycleOwner){ value ->
            value.let{
                Toast.makeText(view.context, getString(it), Toast.LENGTH_SHORT).show()
            }
        }

        chosenDetailsViewModel.itemsToAdd.observe(viewLifecycleOwner){ value ->
            value.let{
                @Suppress("UNCHECKED_CAST")
                (binding.listChosenDetails.adapter as ArrayAdapter<String>).addAll(*it)
            }
        }
    }
}