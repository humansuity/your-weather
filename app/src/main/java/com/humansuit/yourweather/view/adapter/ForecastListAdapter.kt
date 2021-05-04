package com.humansuit.yourweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.yourweather.databinding.ItemWeatherStateBinding
import com.humansuit.yourweather.databinding.ItemWeekdayBinding
import com.humansuit.yourweather.view.data.ForecastSection
import com.humansuit.yourweather.utils.BaseViewHolder
import com.humansuit.yourweather.utils.TYPE_WEATHER_STATE
import com.humansuit.yourweather.utils.TYPE_WEEKDAY

class ForecastListAdapter(private val forecastSectionList: List<ForecastSection>)
    : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_WEEKDAY -> {
                val binding = ItemWeekdayBinding.inflate(layoutInflater, parent, false)
                WeekDayViewHolder(binding)
            }
            TYPE_WEATHER_STATE -> {
                val binding = ItemWeatherStateBinding.inflate(layoutInflater, parent, false)
                WeatherStateViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = forecastSectionList[position]
        when(holder) {
            is WeekDayViewHolder -> holder.bind(element as ForecastSection.WeekDay)
            is WeatherStateViewHolder -> holder.bind(element as ForecastSection.WeatherState)
        }
    }

    override fun getItemCount() = forecastSectionList.size

    override fun getItemViewType(position: Int) = when (forecastSectionList[position]) {
        is ForecastSection.WeekDay -> TYPE_WEEKDAY
        is ForecastSection.WeatherState -> TYPE_WEATHER_STATE
    }


    class WeekDayViewHolder(private val binding: ItemWeekdayBinding) :
        BaseViewHolder<ForecastSection.WeekDay>(binding) {

        override fun bind(item: ForecastSection.WeekDay) {
            binding.weekDayText.text = item.day
        }

    }

    class WeatherStateViewHolder(private val binding: ItemWeatherStateBinding) :
        BaseViewHolder<ForecastSection.WeatherState>(binding) {

        override fun bind(item: ForecastSection.WeatherState) {
            with(binding) {
                weatherStateIcon.setImageResource(item.icon)
                weatherStateText.text = item.state
                timeText.text = item.time
                degreeText.text = item.degree
            }
        }

    }

}


