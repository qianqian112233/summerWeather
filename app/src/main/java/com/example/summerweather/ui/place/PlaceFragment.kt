package com.example.summerweather.ui.place
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerweather.MainActivity
import com.example.summerweather.R
import com.example.summerweather.databinding.ActivityMainBinding
import com.example.summerweather.databinding.FragmentPlaceBinding
import com.example.summerweather.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter : PlaceAdapter
    private lateinit var binding: FragmentPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //采用了官方推荐的视图绑定方法：
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (activity is MainActivity && viewModel.isPlaceSaved()) {
//            val place = viewModel.getSavedPlace()
//            val intent = Intent(context, WeatherActivity::class.java).apply {
//                putExtra("location_lng", place.location.lng)
//                putExtra("location_lat", place.location.lat)
//                putExtra("place_name", place.name)
//            }
//            startActivity(intent)
//            activity?.finish()
//            return
//        }
        if(viewModel.isPlaceSaved()){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerView.adapter = adapter

        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }

    //这一部分代码逻辑已经调整到onViewCreated中，之后如果出现调用需要格外注意

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val layoutManager = LinearLayoutManager(activity)
//        recyclerView.layoutManager = layoutManager
//        adapter = PlaceAdapter(this, viewModel.placeList)
//        recyclerView.adapter = adapter
//        searchPlaceEdit.addTe
//
//
//
//    }


}