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

import java.util.Iterator;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SeekBar;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.interfaces.ViewController;

public class Throttle implements ViewController, ModelListener, SeekBar.OnSeekBarChangeListener {
  andRoc      m_andRoc    = null;
  
  public Throttle(andRoc androc) {
    m_andRoc = androc;
    m_andRoc.getSystem().m_Model.addListener(this);
  }

  
  Loco getLoco() {
    Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
    if( s != null ) {
      String id = (String)s.getSelectedItem();
      if( id != null ) {
        Loco loco = m_andRoc.getSystem().m_Model.getLoco(id);
        return loco;
      }
    }
    return null;
  }
  

  @Override
  public void initView() {
    
    m_andRoc.setContentView(R.layout.throttle);
    Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
    s.setPrompt(new String("Select Loco"));

    ArrayAdapter m_adapterForSpinner = new ArrayAdapter(m_andRoc,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    Iterator it = m_andRoc.getSystem().m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = (Loco)it.next();
      m_adapterForSpinner.add(loco.m_ID);
      
    }
    
    SeekBar mSeekBar = (SeekBar)m_andRoc.findViewById(R.id.SeekBarSpeed);
    mSeekBar.setOnSeekBarChangeListener(this);
    
    Button Lights = (Button) m_andRoc.findViewById(R.id.android_buttonf0);
    Lights.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = getLoco();
          if( loco != null )
            loco.lights();
        }
    });

    Button Dir = (Button) m_andRoc.findViewById(R.id.android_buttondir);
    Dir.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = getLoco();
          if( loco != null )
            loco.dir();
        }
    });


    
  }

  
  
  
  @Override
  public void modelListLoaded(int MODELLIST) {
    if (MODELLIST == ModelListener.MODELLIST_LC) {
      Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);

      if (s != null) {
        s.post(new Runnable() {
          public void run() {
            Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
            ArrayAdapter m_adapterForSpinner = (ArrayAdapter) s.getAdapter();
            // get the loco ids from the model
            Iterator it = m_andRoc.getSystem().m_Model.m_LocoMap.values()
                .iterator();
            while (it.hasNext()) {
              Loco loco = (Loco) it.next();
              // TODO: invoke later?
              m_adapterForSpinner.add(loco.m_ID);

            }
          }
        });
      }
    }
  }

  @Override
  public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {
    Loco loco = getLoco();
    if( loco != null )
      loco.speed(progress);
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar arseekbarg0) {
    // TODO Auto-generated method stub

  }

}
