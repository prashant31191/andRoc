package net.rocrail.androc.activities;

import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Loco;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocoAdapter extends ArrayAdapter<String> {
  List<Loco> m_LocoList = null;
  Activity m_Activity = null;
  
  public LocoAdapter(Activity activity, int textViewResourceId, List<Loco> locoList) {
    super(activity, textViewResourceId);
    m_Activity = activity;
    m_LocoList = locoList;
  }
  
  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }
  
  public void add(Loco loco) {
    super.add(loco.ID);
    m_LocoList.add(loco);
  }
  
  @Override
  public int getPosition (String LocoID) {
    int idx = 0;
    Iterator<Loco> it = m_LocoList.iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      if( LocoID.equals(loco.toString()))
        return idx;
      idx++;
    }
    return 0;
  }
  
  public static class ViewHolder {
    public TextView text;
    public ImageView icon;
}

  
  public View getCustomView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    ViewHolder holder;

    if (row == null) {
      LayoutInflater inflater = m_Activity.getWindow().getLayoutInflater();
      row = inflater.inflate(R.layout.locorow, parent, false);

      holder = new ViewHolder();

      holder.text = (TextView) row.findViewById(R.id.locoRowText);
      holder.icon = (ImageView) row.findViewById(R.id.locoRowImage);
      holder.icon.setClickable(true);

      row.setTag(holder);
    } 
    else {
      holder = (ViewHolder) row.getTag();
    }

    
    if( m_LocoList != null && position < m_LocoList.size() ) {
      Loco loco = m_LocoList.get(position);
      holder.text.setText(loco.ID);
  
      Bitmap img = loco.getLocoBmp(loco.imageView);
      if( img != null )
        holder.icon.setImageBitmap(img);
      else
        holder.icon.setImageResource(R.drawable.noimg);
    }
    else {
      holder.text.setText("?");
      holder.icon.setImageResource(R.drawable.noimg);
    }
  
    return row;
  }
}
