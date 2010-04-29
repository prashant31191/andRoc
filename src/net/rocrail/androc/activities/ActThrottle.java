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
package net.rocrail.androc.activities;

import java.util.Iterator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SeekBar;
import android.widget.AdapterView;
import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LEDButton;
import net.rocrail.androc.widgets.LocoImage;

public class ActThrottle extends ActBase implements ModelListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {
  int         m_iFunctionGroup = 0;
  int         m_iLocoCount     = 0;
  final static int FNGROUPSIZE = 6;
  private Loco m_Loco = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = ActBase.MENU_MENU | ActBase.MENU_LAYOUT | ActBase.MENU_SYSTEM | ActBase.MENU_LOCO;

    connectWithService();
  }
  

  public void connectedWithService() {
    m_RocrailService.m_Model.addListener(this);
    initView();
    updateTitle();
  }

  
  void findLoco() {
    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
    if( s != null ) {
      String id = (String)s.getSelectedItem();
      if( id != null ) {
        m_Loco = m_RocrailService.m_Model.getLoco(id);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("loco", id);
        editor.commit();
      }
    }
  }
  
  void updateFunctions() {
    LEDButton f1 = (LEDButton) findViewById(R.id.throttleF1);
    f1.setText("F"+(1+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton f2 = (LEDButton) findViewById(R.id.throttleF2);
    f2.setText("F"+(2+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton f3 = (LEDButton) findViewById(R.id.throttleF3);
    f3.setText("F"+(3+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton f4 = (LEDButton) findViewById(R.id.throttleF4);
    f4.setText("F"+(4+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton f5 = (LEDButton) findViewById(R.id.throttleF5);
    f5.setText("F"+(5+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton f6 = (LEDButton) findViewById(R.id.throttleF6);
    f6.setText("F"+(6+m_iFunctionGroup*FNGROUPSIZE));
    LEDButton Go = (LEDButton) findViewById(R.id.throttleGo);
    LEDButton Release = (LEDButton) findViewById(R.id.throttleRelease);
    LEDButton Direction = (LEDButton) findViewById(R.id.throttleDirection);

    if( m_Loco != null ) {
      f1.ON = m_Loco.Function[1+m_iFunctionGroup*FNGROUPSIZE];
      f2.ON = m_Loco.Function[2+m_iFunctionGroup*FNGROUPSIZE];
      f3.ON = m_Loco.Function[3+m_iFunctionGroup*FNGROUPSIZE];
      f4.ON = m_Loco.Function[4+m_iFunctionGroup*FNGROUPSIZE];
      f5.ON = m_Loco.Function[5+m_iFunctionGroup*FNGROUPSIZE];
      f6.ON = m_Loco.Function[6+m_iFunctionGroup*FNGROUPSIZE];
      Direction.ON = m_Loco.Dir;
      Go.setEnabled(m_RocrailService.AutoMode);
      Go.ON = m_Loco.Go;
      Release.ON = false;
      f1.invalidate();
      f2.invalidate();
      f3.invalidate();
      f4.invalidate();
      f5.invalidate();
      f6.invalidate();
      Go.invalidate();
      Release.invalidate();
      Direction.invalidate();
    }

  }
  

  public void initView() {
    m_iLocoCount = 0;
    String LocoID = "";
    
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    LocoID = settings.getString("locoid", "");

    
    setContentView(R.layout.throttle);
    
    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
    s.setPrompt(new String("Select Loco"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    Iterator<Loco> it = m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      m_adapterForSpinner.add(loco.ID);
      if( LocoID.length() > 0 && LocoID.equals(loco.ID) ) {
        m_RocrailService.m_iSelectedLoco = m_iLocoCount;
      }
      m_iLocoCount++;
    }
    
    s.setOnItemSelectedListener(this);
    if( m_iLocoCount > 0 && m_RocrailService.m_iSelectedLoco < m_iLocoCount)
      s.setSelection(m_RocrailService.m_iSelectedLoco);    
    
    findLoco();
    
    SeekBar mSeekBar = (SeekBar)findViewById(R.id.SeekBarSpeed);
    mSeekBar.setOnSeekBarChangeListener(this);
    
    Button Lights = (Button) findViewById(R.id.throttleLights);
    Lights.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipLights();
            ((LEDButton)v).ON = m_Loco.Lights;
            v.invalidate();
          }
        }
    });

    Button fn = (Button) findViewById(R.id.throttleFn);
    fn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_iFunctionGroup++;
          if( m_iFunctionGroup > 3 )
            m_iFunctionGroup = 0;
          updateFunctions();
        }
    });

    LEDButton f1 = (LEDButton) findViewById(R.id.throttleF1);
    f1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(1+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[1+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button f2 = (Button) findViewById(R.id.throttleF2);
    f2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(2+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[2+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button f3 = (Button) findViewById(R.id.throttleF3);
    f3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(3+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[3+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button f4 = (Button) findViewById(R.id.throttleF4);
    f4.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(4+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[4+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button f5 = (Button) findViewById(R.id.throttleF5);
    f5.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(5+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[5+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button f6 = (Button) findViewById(R.id.throttleF6);
    f6.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipFunction(6+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[6+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    Button Go = (Button) findViewById(R.id.throttleGo);
    Go.setEnabled(m_RocrailService.AutoMode);
    Go.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipGo();
            ((LEDButton)v).ON = m_Loco.Go;
            v.invalidate();
          }
        }
    });

    Button Release = (Button) findViewById(R.id.throttleRelease);
    Release.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.doRelease();
            ((LEDButton)v).ON = false;
          }
        }
    });

    Button Dir = (Button) findViewById(R.id.throttleDirection);
    Dir.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            m_Loco.flipDir();
            ((LEDButton)v).ON = m_Loco.Dir;
            v.invalidate();
            SeekBar mSeekBar = (SeekBar)findViewById(R.id.SeekBarSpeed);
            mSeekBar.setProgress(m_Loco.Speed);
          }
        }
    });
    
    ImageView image = (ImageView)findViewById(R.id.locoImage);
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if( m_Loco != null ) {
          Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLoco.class);
          intent.putExtra("id", m_Loco.ID);
          startActivity(intent);
        }
      }
  });



    
  }

  
  
  
  @Override
  public void modelListLoaded(int MODELLIST) {
    if (MODELLIST == ModelListener.MODELLIST_LC) {
      Spinner s = (Spinner) findViewById(R.id.spinnerLoco);

      if (s != null) {
        s.post(new Runnable() {
          public void run() {
            Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
            ArrayAdapter m_adapterForSpinner = (ArrayAdapter) s.getAdapter();
            // get the loco ids from the model
            Iterator it = m_RocrailService.m_Model.m_LocoMap.values()
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
    if( m_Loco != null )
      m_Loco.setSpeed(progress, false);
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekbar) {
    if( m_Loco != null )
      m_Loco.setSpeed(seekbar.getProgress(), true);
  }

  protected void  onResume() {
    super.onResume();
    if( m_Loco != null ) {
      updateFunctions();
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longID) {
    m_RocrailService.m_iSelectedLoco = position;
    findLoco();
    if( m_Loco != null ) {
      m_RocrailService.SelectedLoco = m_Loco;
      
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString("locoid", m_Loco.ID);
      editor.commit();

      LEDButton f0 = (LEDButton) findViewById(R.id.throttleLights);
      f0.ON = m_Loco.Lights;
      updateFunctions();
      
      LocoImage image = (LocoImage)findViewById(R.id.locoImage);
      image.ID = m_Loco.ID;
      if( m_Loco.getLocoBmp(image) != null ) {
        image.setImageBitmap(m_Loco.getLocoBmp(null));
      }
      else {
        image.setImageResource(R.drawable.noimg);
      }

      SeekBar mSeekBar = (SeekBar)findViewById(R.id.SeekBarSpeed);
      mSeekBar.setProgress(m_Loco.Speed);

    }
  }


  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }

}
