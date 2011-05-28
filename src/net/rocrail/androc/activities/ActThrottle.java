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

import java.util.Comparator;
import java.util.Iterator;

import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LEDButton;
import net.rocrail.androc.widgets.LocoImage;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import net.rocrail.androc.R;
import net.rocrail.android.widgets.Slider;

public class ActThrottle extends ActBase 
  implements ModelListener, AdapterView.OnItemSelectedListener, net.rocrail.android.widgets.SliderListener 
{
  int         m_iFunctionGroup = 0;
  int         m_iLocoCount     = 0;
  final static int FNGROUPSIZE = 6;
  private Loco m_Loco = null;
  boolean quitShowed = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //MenuSelection = ActBase.MENU_MENU | ActBase.MENU_LAYOUT | ActBase.MENU_SYSTEM | ActBase.MENU_LOCO | ActBase.MENU_PREFERENCES | ActBase.MENU_ACCESSORY;
    MenuSelection = 0;

    connectWithService();
  }
  

  public void connectedWithService() {
    m_RocrailService.m_Model.addListener(this);
    super.connectedWithService();
    initView();
    updateTitle();
    if( !m_RocrailService.m_Model.m_bDonKey && !m_RocrailService.m_bDidShowDonate ) {
      showDonate();
      m_RocrailService.m_bDidShowDonate = true;
    }
  }
  
  public void showDonate() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Rocrail runs entirely on volunteer labor. However, Rocrail also needs contributions of money. Your continued support is vital for keeping Rocrail available. If you already did donate you can ask a key to disable this on startup dialog: donate@rocrail.net")
           .setCancelable(false)
           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
               }
           });
    AlertDialog alert = builder.create();  
    alert.show();
  }

  
  void findLoco() {
    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
    if( s != null ) {
      String id = (String)s.getSelectedItem();
      if( id != null ) {
        m_Loco = m_RocrailService.m_Model.getLoco(id);
        if( m_Loco != null ) {
          m_RocrailService.Prefs.LocoID = m_Loco.toString();
        }
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
    LEDButton Lights = (LEDButton) findViewById(R.id.throttleLights);

    if( m_Loco != null ) {
      f1.ON = m_Loco.Function[1+m_iFunctionGroup*FNGROUPSIZE];
      f2.ON = m_Loco.Function[2+m_iFunctionGroup*FNGROUPSIZE];
      f3.ON = m_Loco.Function[3+m_iFunctionGroup*FNGROUPSIZE];
      f4.ON = m_Loco.Function[4+m_iFunctionGroup*FNGROUPSIZE];
      f5.ON = m_Loco.Function[5+m_iFunctionGroup*FNGROUPSIZE];
      f6.ON = m_Loco.Function[6+m_iFunctionGroup*FNGROUPSIZE];
      Direction.ON = m_Loco.Dir;
      Go.setEnabled(m_RocrailService.AutoMode);
      Go.ON = m_Loco.AutoStart;
      Lights.ON = m_Loco.Lights;
      Release.ON = false;
      f1.invalidate();
      f2.invalidate();
      f3.invalidate();
      f4.invalidate();
      f5.invalidate();
      f6.invalidate();
      Go.invalidate();
      Lights.invalidate();
      Release.invalidate();
      Direction.invalidate();
    }

  }
  

  class LocoComparator implements Comparator<String> {
    @Override
    public int compare(String loco1, String loco2) {
      return loco1.toLowerCase().compareTo(loco2.toLowerCase());
    }
  }
  
  public void initView() {
    m_iLocoCount = 0;
    String LocoID = "";
    
    LocoID = m_RocrailService.Prefs.LocoID;
    setContentView(R.layout.throttle);
    
    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
    s.setPrompt(getText(R.string.SelectLoco));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    int iSelectedLoco = 0;
    Iterator<Loco> it = m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      m_adapterForSpinner.add(loco.toString());
      m_iLocoCount++;
    }
    
    m_adapterForSpinner.sort(new LocoComparator());
    if( LocoID != null && LocoID.length() > 0 ) {
      iSelectedLoco = m_adapterForSpinner.getPosition(LocoID);
    }

    s.setOnItemSelectedListener(this);
    if( m_iLocoCount > 0 && iSelectedLoco < m_iLocoCount)
      s.setSelection(iSelectedLoco);    
    
    findLoco();
    
    Slider mSeekBar = (Slider)findViewById(R.id.Speed);
    //mSeekBar.setOnSeekBarChangeListener(this);
    mSeekBar.addListener(this);
    
    LEDButton Lights = (LEDButton) findViewById(R.id.throttleLights);
    Lights.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipLights();
            ((LEDButton)v).ON = m_Loco.Lights;
            v.invalidate();
          }
        }
    });

    LEDButton fn = (LEDButton) findViewById(R.id.throttleFn);
    fn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          m_iFunctionGroup++;
          if( m_iFunctionGroup > 3 )
            m_iFunctionGroup = 0;
          updateFunctions();
        }
    });

    LEDButton f1 = (LEDButton) findViewById(R.id.throttleF1);
    f1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(1+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[1+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton f2 = (LEDButton) findViewById(R.id.throttleF2);
    f2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(2+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[2+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton f3 = (LEDButton) findViewById(R.id.throttleF3);
    f3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(3+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[3+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton f4 = (LEDButton) findViewById(R.id.throttleF4);
    f4.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(4+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[4+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton f5 = (LEDButton) findViewById(R.id.throttleF5);
    f5.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(5+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[5+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton f6 = (LEDButton) findViewById(R.id.throttleF6);
    f6.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(6+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.Function[6+m_iFunctionGroup*FNGROUPSIZE];
            v.invalidate();
          }
        }
    });

    LEDButton Go = (LEDButton) findViewById(R.id.throttleGo);
    if( m_Loco != null )
      Go.ON = m_Loco.AutoStart;
    Go.setEnabled(m_RocrailService.AutoMode);
    Go.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipGo();
            ((LEDButton)v).ON = m_Loco.AutoStart;
            v.invalidate();
          }
        }
    });

    LEDButton Release = (LEDButton) findViewById(R.id.throttleRelease);
    Release.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.doRelease();
            ((LEDButton)v).ON = false;
          }
        }
    });

    LEDButton Dir = (LEDButton) findViewById(R.id.throttleDirection);
    Dir.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            quitShowed = false;
            m_Loco.flipDir();
            ((LEDButton)v).ON = m_Loco.Dir;
            v.invalidate();
            Slider mSeekBar = (Slider)findViewById(R.id.Speed);
            //mSeekBar.setProgress(m_Loco.Speed);
            mSeekBar.setV(m_Loco.Speed);
          }
        }
    });
    
    ImageView image = (ImageView)findViewById(R.id.locoImage);
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        quitShowed = false;
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
              m_adapterForSpinner.add(loco.toString());

            }
          }
        });
      }
    }
  }
/*
  @Override
  public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {
    quitShowed = false;
    if( m_Loco != null && fromTouch )
      m_Loco.setSpeed(progress, false);
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekbar) {
    quitShowed = false;
    if( m_Loco != null )
      m_Loco.setSpeed(seekbar.getProgress(), true);
  }
*/
  protected void  onResume() {
    super.onResume();
    quitShowed = false;
    if( m_Loco != null ) {
      updateFunctions();
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longID) {
    quitShowed = false;
    findLoco();
    if( m_Loco != null ) {
      m_RocrailService.SelectedLoco = m_Loco;
      
      m_RocrailService.Prefs.saveLoco(m_Loco.toString());

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

      Slider mSeekBar = (Slider)findViewById(R.id.Speed);
      //mSeekBar.setProgress(m_Loco.Speed);
      mSeekBar.setV(m_Loco.Speed);

    }
  }


  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }
  

  /*
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      //Handle the back button
      if(keyCode == KeyEvent.KEYCODE_BACK) {
        
        if(quitShowed) {
          ActThrottle.this.finish();
          return true;
        }
        
        Toast.makeText(getApplicationContext(), R.string.BackAgainQuit,
            Toast.LENGTH_SHORT).show();
        quitShowed = true;
        
        return true;
      }
      else {
        quitShowed = false;
        return super.onKeyDown(keyCode, event);
      }

  }
  */

  @Override
  public void modelUpdate(int MODELLIST, String ID) {
    if( MODELLIST == ModelListener.MODELLIST_LC && m_Loco != null && m_Loco.ID == ID ) {
      Slider bar = (Slider)findViewById(R.id.Speed);
      if( bar != null ) {
        bar.post(new Runnable() {
          public void run() {
            ActThrottle.this.updateFunctions();
            Slider mSeekBar = (Slider)findViewById(R.id.Speed);
            if( !mSeekBar.isPressed() )
              mSeekBar.setV(m_Loco.Speed);
          }
        });
      }
    }
  }


  @Override
  public void onSliderChange(Slider slider, int V) {
    quitShowed = false;
    if( m_Loco != null )
      m_Loco.setSpeed(V, false);
  }


}
