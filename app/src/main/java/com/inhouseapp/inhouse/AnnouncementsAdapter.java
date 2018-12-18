package com.inhouseapp.inhouse;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AnnouncementsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Announcement> mKisiListesi;

    public AnnouncementsAdapter(Activity activity, List<Announcement> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mKisiListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public Announcement getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mKisiListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.announcementrow, null);
        TextView txtTitle = satirView.findViewById(R.id.txtAnnouncementTitle);
        TextView txtDescr = satirView.findViewById(R.id.txtAnnouncementDesc);
        TextView txtDate = satirView.findViewById(R.id.txtAnnounceDate);

        Announcement kisi = mKisiListesi.get(position);

        txtTitle.setText(kisi.title);
        txtDescr.setText(kisi.description);
        txtDate.setText(kisi.date);

        return satirView;
    }
}