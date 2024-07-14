package com.example.summerweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.summerweather.R
import com.example.summerweather.logic.model.Place
import com.example.summerweather.ui.weather.WeatherActivity

/*
RecyclerView适配器的标准写法
 */
class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){
        inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val placeName: TextView = view.findViewById(R.id.placeName)
            val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        //使得能从城市搜索界面跳转到天气界面
        val holder = ViewHolder(view)
        /*
        给place_item.xml的最外层布局注册了一个点击事件监听器，
        然后在点击事件中获取当前点击项的经纬度坐标和地区名称，并传入到intent
        调用Fragment的startActivity()方法启动WeatherActivity
         */
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply{
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            //当点击了任何子项布局时，在跳转到WeatherActivity之前，先调用PlaceViewModel的savePlace()方法来存储选中的城市。
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size
}