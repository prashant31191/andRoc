package net.rocrail.androc.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.objects.Car;
import net.rocrail.androc.objects.Loco;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocoExpListAdapter extends BaseExpandableListAdapter {
  
  private static final int SteamList = 0;
  private static final int DieselList = 1;
  private static final int ElectricList = 2;
  private static final int TrainsetList = 3;
  private static final int SpecialList = 4;
  private static final int CarList = 5;

  
  Context m_Context = null;
  List<Mobile> m_MobileList = null;
  List<Mobile>[] m_Lists    = null;
  boolean sortbyaddr = false;
  int m_Category = 0;
  List<String> m_CatNames = new ArrayList<String>();

  void addCatName(String catname) {
    if( catname != null && catname.length() > 0 ) {
      Iterator<String> it = m_CatNames.iterator();
      while( it.hasNext() ) {
        String l_catname = it.next();
        if( l_catname.equalsIgnoreCase(catname))
          return;
      }
      m_CatNames.add(catname);
    }
  }
  
  int initCategories() {
    if( m_Category == 0 ) {
      m_CatNames.add(m_Context.getText(R.string.Steam).toString());
      m_CatNames.add(m_Context.getText(R.string.Diesel).toString());
      m_CatNames.add(m_Context.getText(R.string.Electric).toString());
      m_CatNames.add(m_Context.getText(R.string.Trainset).toString());
      m_CatNames.add(m_Context.getText(R.string.Special).toString());
      m_CatNames.add(m_Context.getText(R.string.Car).toString());
    }
    else if( m_Category == 1 ) {
      m_CatNames.add("I");
      m_CatNames.add("II");
      m_CatNames.add("III");
      m_CatNames.add("IV");
      m_CatNames.add("V");
      m_CatNames.add("VI");
    }
    else if( m_Category == 2 ) {
      // Roadname
      Iterator<Mobile> it = m_MobileList.iterator();
      while( it.hasNext() ) {
        Mobile mobile = it.next();
        addCatName(mobile.getRoadname());
      }
      
      Collections.sort(m_CatNames, new Comparator<String>() {
        @Override
        public int compare(String s1, String s2)
        {
            return  s1.compareTo(s2);
        }
      });      
      
      m_CatNames.add(m_Context.getText(R.string.none).toString());
    }
    return m_CatNames.size();
  }
  
  void addMobile2Roadname(Mobile mobile) {
    if( mobile.getRoadname() != null && mobile.getRoadname().length() > 0) {
      for( int i = 0; i < m_CatNames.size(); i++ ) {
        String l_catname = m_CatNames.get(i);
        if( l_catname.equalsIgnoreCase(mobile.getRoadname())) {
          m_Lists[i].add(mobile);
          return;
        }
      }
      if( m_CatNames.size() > 0 )
        m_Lists[m_CatNames.size()-1].add(mobile);
    }
    else {
      if( m_CatNames.size() > 0 )
        m_Lists[m_CatNames.size()-1].add(mobile);
    }
  }
  
  public LocoExpListAdapter(Context context, List<Mobile> locoList, int category) {
    m_Context = context;
    m_MobileList = locoList;
    m_Category = category;
    
    int nrcats = initCategories();
    m_Lists = new ArrayList[nrcats];
    for( int i = 0; i < nrcats; i++) {
      m_Lists[i] = new ArrayList<Mobile>();
    }
    
    Iterator<Mobile> it = m_MobileList.iterator();
    while( it.hasNext() ) {
      Mobile mobile = it.next();
      if( mobile instanceof Loco ) {
        Loco loco = (Loco)mobile;
        if( loco.isShow() ) {
          if( m_Category == 1 ) {
            m_Lists[loco.Era].add(loco);
          }
          else if( m_Category == 2 ) {
            addMobile2Roadname(loco);
          }
          else {
            if( loco.Cargo.equals("commuter") || loco.Commuter )
              m_Lists[TrainsetList].add(loco);
            else if( loco.Cargo.equals("post") || loco.Cargo.equals("cleaning") )
              m_Lists[SpecialList].add(loco);
            else if( loco.Engine.equals("steam"))
              m_Lists[SteamList].add(loco);
            else if( loco.Engine.equals("diesel"))
              m_Lists[DieselList].add(loco);
            else if( loco.Engine.equals("electric"))
              m_Lists[ElectricList].add(loco);
            else
              m_Lists[SpecialList].add(loco);
          }
        }
      }
      else if(mobile instanceof Car) {
        Car car = (Car)mobile;
        if( m_Category == 1 ) {
          m_Lists[car.Era].add(car);
        }
        else if( m_Category == 2 ) {
          addMobile2Roadname(car);
        }
        else {
          m_Lists[CarList].add(car);
        }
      }
      
    }

  }
  
  @Override
  public Object getChild(int group, int child) {
    List<Mobile> list = m_Lists[group];
    if( list.size() >= child )
      return list.get(child).getID();
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
    if( group < m_Lists.length )
      return m_Lists[group].size();
    return 0;
  }

  @Override
  public Object getGroup(int group) {
    if( group < m_CatNames.size() )
      return m_CatNames.get(group);
    return null;
  }

  @Override
  public int getGroupCount() {
    return m_Lists.length;
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

    
    if( position < m_CatNames.size() )
      holder.text.setText(m_CatNames.get(position));
    
    return row;
  }
  
  
  public int getRealPosition(int group, int position) {
    
    List<Mobile> list = m_Lists[group];
    Mobile loco = list.get(position);
    
    for( int i = 0; i < m_MobileList.size(); i++ ) {
      if( loco == m_MobileList.get(i) ) {
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

    List<Mobile> list = m_Lists[groupposition];
    
    if( list != null && position < list.size() ) {
      Mobile loco = list.get(position);
      if( sortbyaddr ) {
        holder.text.setText(""+loco.getAddr());
        holder.addr.setText(loco.getID());
      }
      else {
        holder.text.setText(loco.getID());
        holder.addr.setText(""+loco.getAddr());
      }
  
      Bitmap img = loco.getBmp(loco.getImageView());
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
