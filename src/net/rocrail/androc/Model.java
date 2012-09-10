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

import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.objects.Block;
import net.rocrail.androc.objects.FiddleYard;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.objects.Output;
import net.rocrail.androc.objects.Route;
import net.rocrail.androc.objects.Sensor;
import net.rocrail.androc.objects.Signal;
import net.rocrail.androc.objects.StageBlock;
import net.rocrail.androc.objects.Switch;
import net.rocrail.androc.objects.Text;
import net.rocrail.androc.objects.Track;
import net.rocrail.androc.objects.Turntable;
import net.rocrail.androc.objects.ZLevel;

import org.xml.sax.Attributes;



public class Model {
  RocrailService  rocrailService  = null;
  private List<ModelListener>  m_Listeners = new ArrayList<ModelListener>();
  public List<Loco>  m_LocoList = new ArrayList<Loco>();
  public HashMap<String,Loco> m_LocoMap = new HashMap<String,Loco>();
  public List<ZLevel> m_ZLevelList = new ArrayList<ZLevel>();
  public HashMap<String,Switch> m_SwitchMap = new HashMap<String,Switch>();
  public HashMap<String,Output> m_OutputMap = new HashMap<String,Output>();
  public HashMap<String,Text> m_TextMap = new HashMap<String,Text>();
  public HashMap<String,Signal> m_SignalMap = new HashMap<String,Signal>();
  public HashMap<String,Sensor> m_SensorMap = new HashMap<String,Sensor>();
  public HashMap<String,Block> m_BlockMap = new HashMap<String,Block>();
  public HashMap<String,StageBlock> m_StageBlockMap = new HashMap<String,StageBlock>();
  public HashMap<String,FiddleYard> m_FiddleYardMap = new HashMap<String,FiddleYard>();
  public HashMap<String,Turntable> m_TurntableMap = new HashMap<String,Turntable>();
  public HashMap<String,Route> m_RouteMap = new HashMap<String,Route>();
  public HashMap<String,Track> m_TrackMap = new HashMap<String,Track>();
  public List<Item>   m_ItemList = new ArrayList<Item>();
  public List<String> m_ScheduleList = new ArrayList<String>();
  public List<String> m_RouteList = new ArrayList<String>();
  public List<String> m_ActionList = new ArrayList<String>();
  public List<String> m_SwitchList = new ArrayList<String>();
  public List<String> m_OutputList = new ArrayList<String>();
  public List<String> m_TextList = new ArrayList<String>();
  public String m_Title = "";  
  public String m_Name = "";  
  public String m_RocrailVersion = "";  
  public boolean ModPlan = false;
  public boolean m_bDonKey = false;
  
  Turntable m_CurrentTT = null;
  StageBlock m_CurrentSB = null;
  Loco m_CurrentLC = null;

  
  public Model(RocrailService rocrailService) {
    this.rocrailService = rocrailService;
  }
  
  public void setup(Attributes atts) {
    m_LocoList.clear();
    m_LocoMap.clear();
    m_ZLevelList.clear();
    m_SwitchMap.clear();
    m_OutputMap.clear();
    m_TextMap.clear();
    m_SignalMap.clear();
    m_SensorMap.clear();
    m_BlockMap.clear();
    m_StageBlockMap.clear();
    m_FiddleYardMap.clear();
    m_TurntableMap.clear();
    m_RouteMap.clear();
    m_TrackMap.clear();
    m_ItemList.clear();
    m_ScheduleList.clear();
    m_RouteList.clear();
    m_ActionList.clear();
    m_SwitchList.clear();
    m_OutputList.clear();
    m_TextList.clear();
    
    m_Title = Item.getAttrValue(atts, "title", "New");  
    m_Name = Item.getAttrValue(atts, "name", "plan.xml");  
    m_RocrailVersion = atts.getValue("rocrailversion"); 
    ModPlan = Item.getAttrValue(atts, "modplan", false);
    m_bDonKey = Item.getAttrValue(atts, "donkey", false);
    
    informListeners(ModelListener.MODELLIST_PLAN_START);
  }  

  public Block findBlock4Loco( String ID ) {
    Iterator<Block> it = m_BlockMap.values().iterator();
    while( it.hasNext() ) {
      Block block = it.next();
      if( block.LocoID.equals(ID))
        return block;
    }
    
    return null;
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
      if( ID.equals(loco.ID ) || ID.equals(loco.toString() ) )
        return loco;
    }
    return null;
  }
  
  public void updateItem(String objName, Attributes atts) {
    if( objName.equals("lc") ) {
      Loco lc = m_LocoMap.get(Item.getAttrValue(atts, "id", "?"));
      if( lc != null ) {
        // A consist command could have the same throttle ID as this device...
        if( Item.getAttrValue(atts, "consistcmd", false) || !rocrailService.getDeviceName().equals(Item.getAttrValue(atts, "throttleid", "?"))) {
          lc.updateWithAttributes(atts);
          informListeners(ModelListener.MODELLIST_LC, lc.ID);
        }
      }
      return;
    }
    if( objName.equals("fn") ) {
      Loco lc = m_LocoMap.get(Item.getAttrValue(atts, "id", "?"));
      if( lc != null ) {
        lc.updateFunctions(atts);
        informListeners(ModelListener.MODELLIST_LC, lc.ID);
      }
      return;
    }
    if( objName.equals("sw") ) {
      Switch sw = m_SwitchMap.get(Item.getAttrValue(atts, "id", "?"));
      if( sw != null ) {
        sw.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("co") ) {
      Output co = m_OutputMap.get(Item.getAttrValue(atts, "id", "?"));
      if( co != null ) {
        co.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("sg") ) {
      Signal sg = m_SignalMap.get(Item.getAttrValue(atts, "id", "?"));
      if( sg != null ) {
        sg.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("fb") ) {
      Sensor fb = m_SensorMap.get(Item.getAttrValue(atts, "id", "?"));
      if( fb != null ) {
        fb.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("bk") ) {
      Block bk = m_BlockMap.get(Item.getAttrValue(atts, "id", "?"));
      if( bk != null ) {
        bk.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("sb") ) {
      StageBlock sb = m_StageBlockMap.get(Item.getAttrValue(atts, "id", "?"));
      if( sb != null ) {
        m_CurrentSB = sb;
        sb.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("section") && m_CurrentSB != null ) {
      m_CurrentSB.updateSection(atts);
      return;
    }
    
    if( objName.equals("tx") ) {
      Text tx = m_TextMap.get(Item.getAttrValue(atts, "id", "?"));
      if( tx != null ) {
        tx.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("seltab") ) {
      FiddleYard fy = m_FiddleYardMap.get(Item.getAttrValue(atts, "id", "?"));
      if( fy != null ) {
        fy.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("tt") ) {
      Turntable tt = m_TurntableMap.get(Item.getAttrValue(atts, "id", "?"));
      if( tt != null ) {
        tt.updateWithAttributes(atts);
      }
      return;
    }
    if( objName.equals("st") ) {
      Route st = m_RouteMap.get(Item.getAttrValue(atts, "id", "?"));
      if( st != null ) {
        st.updateWithAttributes(atts);
        boolean locked = Item.getAttrValue(atts, "status", 0) == 1;

        Iterator<Track> itTK = m_TrackMap.values().iterator();
        while( itTK.hasNext() ) {
          Track tk = itTK.next();
          tk.update4Route(st.ID, locked);
        }
        Iterator<Sensor> itFB = m_SensorMap.values().iterator();
        while( itFB.hasNext() ) {
          Sensor fb = itFB.next();
          fb.update4Route(st.ID, locked);
        }

      }
      return;
    }
    if( objName.equals("tk") ) {
      Track tk = m_TrackMap.get(Item.getAttrValue(atts, "id", "?"));
      if( tk != null ) {
        tk.updateWithAttributes(atts);
      }
      return;
    }
  }
  
  
  public void addObject(String objName, Attributes atts) {
    if( objName.equals("zlevel") ) {
      // zlevel handling
      String id = atts.getValue("title");
      if( id != null && id.length() > 0 ) {
        addLevel(id, atts);
      }
      return;
    }

    if( objName.equals("lc") ) {
      Loco loco = new Loco(rocrailService, atts);
      m_CurrentLC = loco;
      m_LocoList.add(loco);
      m_LocoMap.put(loco.ID, loco);
      return;
    }
    if( objName.equals("fundef") && m_CurrentLC != null ) {
      m_CurrentLC.addFunction(atts);
      return;
    }
    
    if( objName.equals("sw") ) {
      Switch sw = new Switch(rocrailService, atts);
      m_SwitchMap.put(sw.ID, sw);
      m_SwitchList.add(sw.ID);
      m_ItemList.add(sw);
      return;
    }
    
    if( objName.equals("co") ) {
      Output co = new Output(rocrailService, atts);
      m_OutputMap.put(co.ID, co);
      m_OutputList.add(co.ID);
      m_ItemList.add(co);
      return;
    }
    
    if( objName.equals("tk") ) {
      Track track = new Track(rocrailService, atts);
      m_TrackMap.put(track.ID, track);
      m_ItemList.add(track);
      return;
    }
    
    if( objName.equals("fb") ) {
      Sensor sensor = new Sensor(rocrailService, atts);
      m_SensorMap.put(sensor.ID, sensor);
      m_ItemList.add(sensor);
      return;
    }
    
    if( objName.equals("sg") ) {
      Signal signal = new Signal(rocrailService, atts);
      m_SignalMap.put(signal.ID, signal);
      m_ItemList.add(signal);
      return;
    }

    if( objName.equals("bk") ) {
      Block block = new Block(rocrailService, atts);
      m_BlockMap.put(block.ID, block);
      m_ItemList.add(block);
      return;
    }

    if( objName.equals("sb") ) {
      StageBlock stageblock = new StageBlock(rocrailService, atts);
      m_CurrentSB = stageblock;
      m_StageBlockMap.put(stageblock.ID, stageblock);
      m_ItemList.add(stageblock);
      return;
    }
    if( objName.equals("section") && m_CurrentSB != null ) {
      m_CurrentSB.addSection(atts);
      return;
    }

    if( objName.equals("tx") ) {
      Text text = new Text(rocrailService, atts);
      m_TextMap.put(text.ID, text);
      m_ItemList.add(text);
      return;
    }

    if( objName.equals("seltab") ) {
      FiddleYard fy = new FiddleYard(rocrailService, atts);
      m_FiddleYardMap.put(fy.ID, fy);
      m_ItemList.add(fy);
      return;
    }

    if( objName.equals("tt") ) {
      Turntable tt = new Turntable(rocrailService, atts);
      m_CurrentTT = tt;
      m_TurntableMap.put(tt.ID, tt);
      m_ItemList.add(tt);
      return;
    }
    if( objName.equals("track") && m_CurrentTT != null ) {
      m_CurrentTT.addTrack(atts);
      return;
    }

    if( objName.equals("sc") ) {
      m_ScheduleList.add(Item.getAttrValue(atts, "id", "?"));
      return;
    }
    if( objName.equals("st") ) {
      Route route = new Route(rocrailService, atts);
      m_RouteMap.put(route.ID, route);
      m_RouteList.add(route.ID);
      m_ItemList.add(route);
      return;
    }
    if( objName.equals("ac") ) {
      m_ActionList.add(Item.getAttrValue(atts, "id", "?"));
      return;
    }
  }
  
  public void addLevel(String level, Attributes atts) {
    String sZ = atts.getValue("z");
    int z = 0;
    if( sZ != null && sZ.length() > 0 )
      z = Integer.parseInt(sZ);
    ZLevel zlevel = new ZLevel(level, z);
    m_ZLevelList.add(zlevel);
    if( ModPlan ) {
      zlevel.X = Item.getAttrValue(atts, "modviewx", 0);
      zlevel.Y = Item.getAttrValue(atts, "modviewy", 0);
      zlevel.cX = Item.getAttrValue(atts, "modviewcx", 0);
      zlevel.cY = Item.getAttrValue(atts, "modviewcy", 0);
      zlevel.ModID = Item.getAttrValue(atts, "modid", "");
    }
  }
  
  public void addListener( ModelListener listener ) {
    m_Listeners.add(listener);
  }
  
  public void listLoaded(String listName ) {
    int listCode = -1;
    if( listName.equals("lclist") )
      listCode = ModelListener.MODELLIST_LC;
    else if( listName.equals("bklist") )
      listCode = ModelListener.MODELLIST_BK;
    else if( listName.equals("swlist") )
      listCode = ModelListener.MODELLIST_SW;
    else if( listName.equals("sglist") )
      listCode = ModelListener.MODELLIST_SG;
    else if( listName.equals("colist") )
      listCode = ModelListener.MODELLIST_CO;
    else if( listName.equals("fblist") )
      listCode = ModelListener.MODELLIST_FB;
    else if( listName.equals("sclist") )
      listCode = ModelListener.MODELLIST_SC;
    else if( listName.equals("stlist") )
      listCode = ModelListener.MODELLIST_ST;
    else if( listName.equals("tklist") )
      listCode = ModelListener.MODELLIST_TK;
    else if( listName.equals("txlist") )
      listCode = ModelListener.MODELLIST_TX;
    else if( listName.equals("plan") )
      listCode = ModelListener.MODELLIST_PLAN;
    
    informListeners(listCode);
  }

  void informListeners( int listCode ) {
    if( listCode != -1 ) {
      Iterator<ModelListener> it = m_Listeners.iterator();
      while( it.hasNext() ) {
        ModelListener listener = it.next();
        listener.modelListLoaded(listCode);
      }
    }

  }
  
  void informListeners( int listCode, String ID ) {
    if( listCode != -1 ) {
      Iterator<ModelListener> it = m_Listeners.iterator();
      while( it.hasNext() ) {
        ModelListener listener = it.next();
        listener.modelUpdate(listCode, ID);
      }
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
