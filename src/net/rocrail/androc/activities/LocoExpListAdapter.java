package net.rocrail.androc.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.activities.LocoAdapter.ViewHolder;
import net.rocrail.androc.objects.Loco;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocoExpListAdapter extends BaseExpandableListAdapter {
  Context m_Context = null;
  List<Loco> m_LocoList     = null;
  List<Loco> m_SteamList    = new ArrayList<Loco>();
  List<Loco> m_DieselList   = new ArrayList<Loco>();
  List<Loco> m_ElectricList = new ArrayList<Loco>();
  List<Loco> m_TrainsetList = new ArrayList<Loco>();
  List<Loco> m_SpecialList  = new ArrayList<Loco>();
  List<Loco>[] m_Lists      = new ArrayList[5];
  boolean sortbyaddr = false;

  public LocoExpListAdapter(Context context, List<Loco> locoList) {
    m_Context = context;
    m_LocoList = locoList;
    m_Lists[0] = m_SteamList;
    m_Lists[1] = m_DieselList;
    m_Lists[2] = m_ElectricList;
    m_Lists[3] = m_TrainsetList;
    m_Lists[4] = m_SpecialList;
    Iterator<Loco> it = m_LocoList.iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      if( loco.Cargo.equals("commuter"))
        m_TrainsetList.add(loco);
      else if( loco.Cargo.equals("post") || loco.Cargo.equals("cleaning") )
        m_SpecialList.add(loco);
      else if( loco.Engine.equals("steam"))
        m_SteamList.add(loco);
      else if( loco.Engine.equals("diesel"))
        m_DieselList.add(loco);
      else if( loco.Engine.equals("electric"))
        m_ElectricList.add(loco);
      
    }

  }
  
  @Override
  public Object getChild(int group, int child) {
    List<Loco> list = m_Lists[group];
    if( list.size() >= child )
      return list.get(child).ID;
    return null;
  }

  @Override
  public long getChildId(int group, int child) {
    return group * 1000 + child;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    return getCustomChildView(groupPosition, childPosition, convertView, parent);
  }

  @Override
  public int getChildrenCount(int group) {
    switch(group) {
    case 0: return m_SteamList.size();
    case 1: return m_DieselList.size();
    case 2: return m_ElectricList.size();
    case 3: return m_TrainsetList.size();
    case 4: return m_SpecialList.size();
    }
    return 0;
  }

  @Override
  public Object getGroup(int group) {
    switch(group) {
    case 0: return m_Context.getText(R.string.Steam).toString();
    case 1: return m_Context.getText(R.string.Diesel).toString();
    case 2: return m_Context.getText(R.string.Electric).toString();
    case 3: return m_Context.getText(R.string.Trainset).toString();
    case 4: return m_Context.getText(R.string.Special).toString();
    }
    return null;
  }

  @Override
  public int getGroupCount() {
    return 5;
  }

  @Override
  public long getGroupId(int group) {
    return group;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    return getCustomGroupView(groupPosition, convertView, parent);
  }

  @Override
  public boolean hasStableIds() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isChildSelectable(int arg0, int arg1) {
    return true;
  }
  
  
  public static class ViewHolder {
    public TextView text;
    public TextView text2;
    public TextView addr;
    public ImageView icon;
}

  public View getCustomGroupView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    ViewHolder holder;

    if (row == null) {
      LayoutInflater inflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.lococatitem, parent, false);

      holder = new ViewHolder();

      holder.text = (TextView) row.findViewById(R.id.locoCatText);
      holder.icon = (ImageView) row.findViewById(R.id.folderImage);
      holder.icon.setFocusable(false);
      row.setTag(holder);
    } 
    else {
      holder = (ViewHolder) row.getTag();
    }

    
    switch(position) {
    case 0: holder.text.setText("Steam"); break;
    case 1: holder.text.setText("Diesel"); break;
    case 2: holder.text.setText("Electric"); break;
    case 3: holder.text.setText("Trainset"); break;
    case 4: holder.text.setText("Special"); break;
    }
  
    return row;
  }
  
  
  public int getRealPosition(int group, int position) {
    
    List<Loco> list = m_Lists[group];
    Loco loco = list.get(position);
    
    for( int i = 0; i < m_LocoList.size(); i++ ) {
      if( loco == m_LocoList.get(i) ) {
        return i;
      }
    }
    return 0;
  }
  

  public View getCustomChildView(int groupposition, int position, View convertView, ViewGroup parent) {
    View row = convertView;
    ViewHolder holder;

    if (row == null) {
      LayoutInflater inflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.locorow, parent, false);

      holder = new ViewHolder();

      holder.text = (TextView) row.findViewById(R.id.locoRowText);
      holder.addr = (TextView) row.findViewById(R.id.locoRowAddr);
      holder.icon = (ImageView) row.findViewById(R.id.locoRowImage);
      //holder.icon.setClickable(true);
      holder.icon.setFocusable(false);
      row.setTag(holder);
    } 
    else {
      holder = (ViewHolder) row.getTag();
    }

    List<Loco> list = m_Lists[groupposition];
    
    if( list != null && position < list.size() ) {
      Loco loco = list.get(position);
      if( sortbyaddr ) {
        holder.text.setText(""+loco.Addr);
        holder.addr.setText(loco.ID);
      }
      else {
        holder.text.setText(loco.ID);
        holder.addr.setText(""+loco.Addr);
      }
  
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
