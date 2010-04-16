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

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.interfaces.ViewController;

public class Throttle implements ViewController, ModelListener {
  andRoc      m_andRoc    = null;
  
  public Throttle(andRoc androc) {
    m_andRoc = androc;
    m_andRoc.getSystem().m_Model.addListener(this);
  }
  
  @Override
  public void initView() {
    
    m_andRoc.setContentView(R.layout.throttle);
    Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
    s.setPrompt(new String("Loco"));

    ArrayAdapter m_adapterForSpinner = new ArrayAdapter(m_andRoc,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);
    m_adapterForSpinner.add("NS 2418");
    m_adapterForSpinner.add("E19");
    m_adapterForSpinner.add("V160");
  }

  @Override
  public void modelListLoaded(int MODELLIST) {
    if( MODELLIST == ModelListener.MODELLIST_LC ) {
      Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
      if( s != null ) {
        ArrayAdapter m_adapterForSpinner = new ArrayAdapter(m_andRoc, android.R.layout.simple_spinner_item);
        m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(m_adapterForSpinner);

        // TODO: get the loco ids from the model
        m_adapterForSpinner.add("NS 2418");
      }
    }
  }

}
