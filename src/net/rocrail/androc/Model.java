/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2010 - Rob Versluis <r.j.versluis@rocrail.net>

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
package net.rocrail.androc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;

import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.objects.Block;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.objects.Sensor;
import net.rocrail.androc.objects.Signal;
import net.rocrail.androc.objects.Switch;
import net.rocrail.androc.objects.Track;
import net.rocrail.androc.objects.ZLevel;

public class Model {
  RocrailService  m_andRoc  = null;
  private List<ModelListener>  m_Listeners = new ArrayList<ModelListener>();
  public List<Loco>  m_LocoList = new ArrayList<Loco>();
  public HashMap<String,Loco> m_LocoMap = new HashMap<String,Loco>();
  public List<ZLevel> m_ZLevelList = new ArrayList<ZLevel>();
  public List<Switch> m_SwitchList = new ArrayList<Switch>();
  public List<Item>   m_ItemList = new ArrayList<Item>();
  public String m_Title = "";  
  public String m_Name = "";  
  public String m_RocrailVersion = "";  

  
  public Model(RocrailService rocrailService) {
    m_andRoc = rocrailService;
  }
  
  public void setup(Attributes atts) {
    m_LocoList.clear();
    m_LocoMap.clear();
    m_ZLevelList.clear();
    
    m_Title = atts.getValue("title");  
    m_Name = atts.getValue("name");  
    m_RocrailVersion = atts.getValue("rocrailversion");  
  }  

 
  public Loco getLoco(String ID) {
    /* Rob:
     * A new instance of the Loco class is returned if the hasmap is used to look up the ID...
     * Is this a hasmap bug or do I overlook something here?
     * 
     * Original code:
     * return m_LocoMap(ID);
     */
    Iterator<Loco> it = m_LocoList.iterator();
    while( it.hasNext()) {
      Loco loco = it.next();
      if( ID.equals(loco.ID ))
        return loco;
    }
    return null;
  }
  
  public void addLoco(Loco loco, Attributes atts) {
    m_LocoList.add(loco);
    m_LocoMap.put(loco.ID, loco);
  }
  
  public void addSwitch(Switch sw) {
    m_SwitchList.add(sw);
  }
  
  public void addItem(String itemtype, Attributes atts) {
    if( itemtype.equals("tk") ) {
      Track track = new Track(atts);
      m_ItemList.add(track);
      return;
    }
    
    if( itemtype.equals("fb") ) {
      Sensor sensor = new Sensor(m_andRoc, atts);
      m_ItemList.add(sensor);
      return;
    }
    
    if( itemtype.equals("sg") ) {
      Signal signal = new Signal(m_andRoc, atts);
      m_ItemList.add(signal);
      return;
    }

    if( itemtype.equals("bk") ) {
      Block block = new Block(atts);
      m_ItemList.add(block);
      return;
    }
  }
  
  public void addLevel(String level, Attributes atts) {
    String sZ = atts.getValue("z");
    int z = 0;
    if( sZ != null && sZ.length() > 0 )
      z = Integer.parseInt(sZ);
    m_ZLevelList.add(new ZLevel(level, z));
  }
  
  public void addListener( ModelListener listener ) {
    m_Listeners.add(listener);
  }
  
  public void lclistLoaded() {
    Iterator<ModelListener> it = m_Listeners.iterator();
    while( it.hasNext() ) {
      ModelListener listener = it.next();
      listener.modelListLoaded(ModelListener.MODELLIST_LC);
    }
  }
  
  public void planLoaded() {
    Iterator<ModelListener> it = m_Listeners.iterator();
    while( it.hasNext() ) {
      ModelListener listener = it.next();
      listener.modelListLoaded(ModelListener.MODELLIST_PLAN);
    }
  }
  
}
