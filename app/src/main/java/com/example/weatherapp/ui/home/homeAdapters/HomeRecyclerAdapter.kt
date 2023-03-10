package com.example.weatherapp.ui.home.homeAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.model.Daily


class HomeRecyclerAdapter(
    private var thisDay: Current,
    private var hour: List<Current>,
    private var day: List<Daily>)
    : RecyclerView.Adapter<ViewHolder>() {

    lateinit var currentDay: Current
    lateinit var hourList: List<Current>
    lateinit var dayList: List<Daily>


    init {
        this.thisDay = currentDay
        this.hour = hourList
        this.day=dayList
    }

    inner class ViewHolder(var v:View) {
        var recyclerView: View=v.findViewById(R.id.recycler_adapted_in_home)

    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            R.layout.today_forcast
        }else if(position == 1){
            R.layout.recycler_hour_item
        }else{
            R.layout.recycler_daily_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var veiw:View= inflater.inflate(viewType,parent,false)
        val viewHolder:RecyclerView.ViewHolder =ViewHolder(veiw) as RecyclerView.ViewHolder
        return viewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (position == 0) {
//
//            val viewPager2 = holder.recyclerView as ViewPager2
//            viewPager2.adapter = SliderAdapter(categoryWithMeals.getMeals(), addToFavourite)
//            holder.viewPagerSetUp(viewPager2)
//            holder.switchViewPagerItem(viewPager2, categoryWithMeals.getMeals().size())
//        } else if(position==1){
//            (holder.recyclerView as RecyclerView).adapter =
//                MealAdapter(categoryWithMeals.getMeals(), addToFavourite)
//            holder.viewAll.setOnClickListener(View.OnClickListener { v ->
//                findNavController(v).navigate(
//                    com.soha.foodplanner.ui.home.HomeFragmentDirections
//                        .actionHomeFragmentToVeiwAllCatFragment(categoryWithMeals.getName())
//                )
//            })
//        }else if(position==2){
//
//        }else{
//
//        }
    }

    override fun getItemCount(): Int {
        return 3
   }

}