package com.mirea.kt.ribo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private Context context;
    private ArrayList<ListItemClass> prazdnik;
    private LayoutInflater inflater;
    private List<ListItemClass> listItem = new ArrayList<>();


    public CustomArrayAdapter(@NonNull Context context, int resourse, List<ListItemClass> listItem, LayoutInflater inflater) {
        super(context, resourse, listItem);
        this.inflater = inflater;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        ListItemClass listItemMain = listItem.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_1, null, false);
            viewHolder = new ViewHolder();
            viewHolder.holiday = convertView.findViewById(R.id.tv_holiday);
            viewHolder.share = convertView.findViewById(R.id.shareButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.holiday.setText(listItemMain.getHoliday());

        // Установка обработчика нажатия на кнопку "Поделиться"
        viewHolder.share.setOnClickListener(view -> {
            String holidayInfo = listItemMain.getHoliday(); // Получение информации о празднике
            shareHolidayInfo(getContext(), holidayInfo); // Вызов метода для поделиться информацией о празднике
        });

        return convertView;
    }

    // Метод для поделиться информацией о празднике
    private void shareHolidayInfo(Context context, String holidayInfo) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, holidayInfo);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, holidayInfo);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent telegramIntent = new Intent(Intent.ACTION_SEND);
        telegramIntent.setType("text/plain");
        telegramIntent.putExtra(Intent.EXTRA_TEXT, holidayInfo);
        telegramIntent.setPackage("org.telegram.messenger");
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, holidayInfo);
        whatsappIntent.setPackage("com.whatsapp");
        Intent chooserIntent = Intent.createChooser(sendIntent, "Поделиться:");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{telegramIntent, whatsappIntent});
        context.startActivity(chooserIntent);

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    public class ViewHolder {

        TextView holiday;
        Button share;
    }

}
