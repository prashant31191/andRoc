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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SeekBar;
import android.widget.AdapterView;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.interfaces.ViewController;

public class Throttle implements ViewController, ModelListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {
  andRoc      m_andRoc         = null;
  int         m_iFunctionGroup = 0;
  int         m_iSelectedLoco  = 0;
  int         m_iLocoCount     = 0;
  
  public Throttle(andRoc androc) {
    m_andRoc = androc;
    m_andRoc.getSystem().m_Model.addListener(this);
  }

  
  Loco findLoco() {
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
  
  void updateFunctions() {
    Button f1 = (Button) m_andRoc.findViewById(R.id.android_buttonf1);
    f1.setText("F"+(1+m_iFunctionGroup*8));
    Button f2 = (Button) m_andRoc.findViewById(R.id.android_buttonf2);
    f2.setText("F"+(2+m_iFunctionGroup*8));
    Button f3 = (Button) m_andRoc.findViewById(R.id.android_buttonf3);
    f3.setText("F"+(3+m_iFunctionGroup*8));
    Button f4 = (Button) m_andRoc.findViewById(R.id.android_buttonf4);
    f4.setText("F"+(4+m_iFunctionGroup*8));
    Button f5 = (Button) m_andRoc.findViewById(R.id.android_buttonf5);
    f5.setText("F"+(5+m_iFunctionGroup*8));
    Button f6 = (Button) m_andRoc.findViewById(R.id.android_buttonf6);
    f6.setText("F"+(6+m_iFunctionGroup*8));
    Button f7 = (Button) m_andRoc.findViewById(R.id.android_buttonf7);
    f7.setText("F"+(7+m_iFunctionGroup*8));
    Button f8 = (Button) m_andRoc.findViewById(R.id.android_buttonf8);
    f8.setText("F"+(8+m_iFunctionGroup*8));

  }
  

  @Override
  public void initView() {
    m_iLocoCount = 0;
    
    m_andRoc.setContentView(R.layout.throttle);
    Spinner s = (Spinner) m_andRoc.findViewById(R.id.spinnerLoco);
    s.setPrompt(new String("Select Loco"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(m_andRoc,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    Iterator it = m_andRoc.getSystem().m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = (Loco)it.next();
      m_adapterForSpinner.add(loco.ID);
      m_iLocoCount++;
    }
    
    s.setOnItemSelectedListener(this);
    if( m_iLocoCount > 0 && m_iSelectedLoco < m_iLocoCount)
      s.setSelection(m_iSelectedLoco);    
    
    SeekBar mSeekBar = (SeekBar)m_andRoc.findViewById(R.id.SeekBarSpeed);
    mSeekBar.setOnSeekBarChangeListener(this);
    
    Button Lights = (Button) m_andRoc.findViewById(R.id.android_buttonf0);
    Lights.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.lights();
          }
        }
    });

    Button fn = (Button) m_andRoc.findViewById(R.id.android_buttonfn);
    fn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_iFunctionGroup++;
          if( m_iFunctionGroup > 2 )
            m_iFunctionGroup = 0;
          updateFunctions();
        }
    });

    Button f1 = (Button) m_andRoc.findViewById(R.id.android_buttonf1);
    f1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(1+m_iFunctionGroup*8);
          }
        }
    });

    Button f2 = (Button) m_andRoc.findViewById(R.id.android_buttonf2);
    f2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(2+m_iFunctionGroup*8);
          }
        }
    });

    Button f3 = (Button) m_andRoc.findViewById(R.id.android_buttonf3);
    f3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(3+m_iFunctionGroup*8);
          }
        }
    });

    Button f4 = (Button) m_andRoc.findViewById(R.id.android_buttonf4);
    f4.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(4+m_iFunctionGroup*8);
          }
        }
    });

    Button f5 = (Button) m_andRoc.findViewById(R.id.android_buttonf5);
    f5.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(5+m_iFunctionGroup*8);
          }
        }
    });

    Button f6 = (Button) m_andRoc.findViewById(R.id.android_buttonf6);
    f6.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(6+m_iFunctionGroup*8);
          }
        }
    });

    Button f7 = (Button) m_andRoc.findViewById(R.id.android_buttonf7);
    f7.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(7+m_iFunctionGroup*8);
          }
        }
    });

    Button f8 = (Button) m_andRoc.findViewById(R.id.android_buttonf8);
    f8.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.function(8+m_iFunctionGroup*8);
          }
        }
    });

    Button Dir = (Button) m_andRoc.findViewById(R.id.android_buttondir);
    Dir.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Loco loco = findLoco();
          if( loco != null ) {
            loco.dir();
          }
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
              m_adapterForSpinner.add(loco.ID);

            }
          }
        });
      }
    }
  }

  @Override
  public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {
    Loco loco = findLoco();
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


  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longID) {
    // TODO Auto-generated method stub
    m_iSelectedLoco = position;
    Loco loco = findLoco();
    if( loco != null && loco.LocoBmp != null ) {
      ImageView image = (ImageView)m_andRoc.findViewById(R.id.locoImage);
      if( image != null ) {
        image.setImageBitmap(loco.LocoBmp);
      }
    }
  }


  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }

}
