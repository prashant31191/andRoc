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

import android.app.Activity;

public class Model {
  andRoc  m_andRoc  = null;
  List m_Listeners = new ArrayList();
  HashMap   m_LocoMap = new HashMap();
  
  public Model(andRoc androc) {
    m_andRoc = androc;
  }
 
  public Loco getLoco(String ID) {
    return (Loco)m_LocoMap.get(ID);
  }
  
  public void addLoco(Loco loco) {
    m_LocoMap.put(loco.ID, loco);
  }
  
  public void addListener( ModelListener listener ) {
    m_Listeners.add(listener);
  }
  
  public void lclistLoaded() {
    Iterator it = m_Listeners.iterator();
    while( it.hasNext() ) {
      ModelListener listener = (ModelListener)it.next();
      listener.modelListLoaded(ModelListener.MODELLIST_LC);
    }
  }
  
}
