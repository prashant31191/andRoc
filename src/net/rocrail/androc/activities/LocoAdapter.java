/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2011 - Rob Versluis <r.j.versluis@rocrail.net>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package net.rocrail.androc.activities;

import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Loco;
import android.app.Activity;
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
  boolean sortbyaddr = false;
  
  public LocoAdapter(Activity activity, int textViewResourceId, List<Loco> locoList, boolean sortbyaddr) {
    super(activity, textViewResourceId);
    m_Activity = activity;
    m_LocoList = locoList;
    this.sortbyaddr = sortbyaddr; 
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
    public TextView addr;
    public ImageView icon;
    public ImageView dir;
}

  
  public View getCustomView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    ViewHolder holder;

    if (row == null) {
      LayoutInflater inflater = m_Activity.getWindow().getLayoutInflater();
      row = inflater.inflate(R.layout.locorow, parent, false);

      holder = new ViewHolder();

      holder.text = (TextView) row.findViewById(R.id.locoRowText);
      holder.addr = (TextView) row.findViewById(R.id.locoRowAddr);
      holder.icon = (ImageView) row.findViewById(R.id.locoRowImage);
      holder.dir  = (ImageView) row.findViewById(R.id.locoRowDir);
      //holder.icon.setClickable(true);
      holder.icon.setFocusable(false);
      row.setTag(holder);
    } 
    else {
      holder = (ViewHolder) row.getTag();
    }

    
    if( m_LocoList != null && position < m_LocoList.size() ) {
      Loco loco = m_LocoList.get(position);
      if( sortbyaddr ) {
        holder.text.setText(""+loco.Addr);
        holder.addr.setText(loco.ID);
      }
      else {
        holder.text.setText(loco.ID);
        holder.addr.setText(""+loco.Addr);
      }
      
      holder.dir.setImageResource(loco.Placing ? R.drawable.fwd:R.drawable.rev);
  
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
