package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiaoke on 15-7-15.
 */


public class ForecastAdapter extends CursorAdapter {


    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }

    //private Context mContext;

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;


    public ForecastAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);


    }

    private String formatHighLows(Context context, double high, double low){
        boolean isMetric = Utility.isMetric(mContext);
        String hightLowStr = Utility.formatTemperature(context, high,isMetric) + "/" +
                Utility.formatTemperature(context, low, isMetric);
        return hightLowStr;
    }

    private String convertCursorRowToUXFormat(Context context, Cursor cursor){
//        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
//        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
//        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(context,
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP)
        );

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE))+
                '-' + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                '-' + highAndLow;
    }

    @Override
    public int getItemViewType(int position){
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int viewType = getItemViewType(cursor.getPosition());
        int layout_Id = -1;
        if (viewType==VIEW_TYPE_TODAY) {
            layout_Id = R.layout.list_item_forecast_today;

        }else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            layout_Id = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layout_Id, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return  view;



    }
    public void bindView(View view, Context context, Cursor cursor){
//        TextView tv = (TextView) view;
//        tv.setText(convertCursorRowToUXFormat(context, cursor));

        ViewHolder viewHolder = (ViewHolder)view.getTag();

        int weatherConditionId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        int weather_resid=0;
        int viewType = getItemViewType(cursor.getPosition());
        if (viewType == VIEW_TYPE_TODAY)
            weather_resid = Utility.getArtResourceForWeatherCondition(weatherConditionId);
        else if (viewType == VIEW_TYPE_FUTURE_DAY){
            weather_resid = Utility.getIconResourceForWeatherCondition(weatherConditionId);
        }
        viewHolder.iconView.setImageResource(weather_resid);

        //ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        //viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherConditionId));

        boolean isMetric = Utility.isMetric(context);

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        //TextView highView = (TextView)view.findViewById(R.id.list_item_high_textview);
        viewHolder.highTempView.setText(Utility.formatTemperature(context,high,isMetric));

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        //TextView lowView = (TextView)view.findViewById(R.id.list_item_low_textview);
        viewHolder.lowTempView.setText(Utility.formatTemperature(context,low, isMetric));

        String weatherDesc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        //TextView weatherdescView = (TextView)view.findViewById(R.id.list_item_forecast_textview);
        viewHolder.descriptionView.setText(weatherDesc);

        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String datestr = Utility.getFriendlyDayString(context, date);
        //TextView datestrView = (TextView)view.findViewById(R.id.list_item_date_textview);
        viewHolder.dateView.setText(datestr);


    }



}



