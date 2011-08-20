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
  
  public View getCustomView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = m_Activity.getWindow().getLayoutInflater();
    View row = inflater.inflate(R.layout.locorow, parent, false);
    TextView label = (TextView) row.findViewById(R.id.locoRowText);
    ImageView icon = (ImageView) row.findViewById(R.id.locoRowImage);

    if( m_LocoList != null && position < m_LocoList.size() ) {
      Loco loco = m_LocoList.get(position);
      label.setText(loco.ID);
  
      Bitmap img = loco.getLocoBmp(loco.imageView);
      if( img != null )
        icon.setImageBitmap(img);
      else
        icon.setImageResource(R.drawable.noimg);
    }
    else {
      label.setText("?");
      icon.setImageResource(R.drawable.noimg);
    }
  
    return row;
  }
}
