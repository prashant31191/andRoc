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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LEDButton;
import net.rocrail.androc.widgets.LocoImage;
import net.rocrail.android.widgets.Slider;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActThrottle extends ActBase 
  implements ModelListener, 
  net.rocrail.android.widgets.SliderListener, OnLongClickListener 
{
  int         m_iFunctionGroup = 0;
  int         m_iLocoCount     = 0;
  int         m_iLocoSelected  = 0;
  final static int FNGROUPSIZE = 6;
  private Mobile m_Loco = null;
  boolean quitShowed = false;
  List<Mobile> m_MobileList = new ArrayList<Mobile>();


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //MenuSelection = ActBase.MENU_MENU | ActBase.MENU_LAYOUT | ActBase.MENU_SYSTEM | ActBase.MENU_LOCO | ActBase.MENU_PREFERENCES | ActBase.MENU_ACCESSORY;
    MenuSelection = ActBase.MENU_SYSTEM | ActBase.MENU_LOCO | ActBase.MENU_PREFERENCES | ActBase.MENU_POM;

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
                 ActThrottle.this.finish();
               }
           });
    AlertDialog alert = builder.create();  
    alert.show();
  }

  
  void findLoco(int pos) {
    System.out.println("findLoco " + pos + " out of " + m_MobileList.size()  );
    if( pos >= 0 && pos < m_MobileList.size() ) 
      m_Loco = m_MobileList.get(pos);
    else {
      // ToDo: Look up last used...
    }
  
    if( m_Loco != null ) {
      System.out.println("findLoco: " + m_Loco.getID() );
      m_RocrailService.Prefs.saveLoco(m_Loco.getID(), m_RocrailService.ThrottleNr);
    }
    else {
      System.out.println("findLoco: loco not found " + pos );
    }

  }
  
  void updateFunctions() {
    if( m_Loco == null )
      return;

    LEDButton f0 = (LEDButton) findViewById(R.id.throttleLights);
    f0.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon0 = m_Loco.getFunctionIcon(0);
    if( icon0 != null ) {
      f0.setBackgroundDrawable(new BitmapDrawable(icon0));
      f0.setText("");
    }
    else
      f0.setText(m_Loco.getFunctionText(0));
    
    LEDButton f1 = (LEDButton) findViewById(R.id.throttleF1);
    f1.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon1 = m_Loco.getFunctionIcon(1+m_iFunctionGroup*FNGROUPSIZE);
    if( icon1 != null ) {
      f1.setBackgroundDrawable(new BitmapDrawable(icon1));
      f1.setText("");
    }
    else
      f1.setText(m_Loco.getFunctionText(1+m_iFunctionGroup*FNGROUPSIZE));
    
    LEDButton f2 = (LEDButton) findViewById(R.id.throttleF2);
    f2.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon2 = m_Loco.getFunctionIcon(2+m_iFunctionGroup*FNGROUPSIZE);
    if( icon2 != null ) {
      f2.setBackgroundDrawable(new BitmapDrawable(icon2));
      f2.setText("");
    }
    else
      f2.setText(m_Loco.getFunctionText(2+m_iFunctionGroup*FNGROUPSIZE));

    LEDButton f3 = (LEDButton) findViewById(R.id.throttleF3);
    f3.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon3 = m_Loco.getFunctionIcon(3+m_iFunctionGroup*FNGROUPSIZE);
    if( icon3 != null ) {
      f3.setBackgroundDrawable(new BitmapDrawable(icon3));
      f3.setText("");
    }
    else
      f3.setText(m_Loco.getFunctionText(3+m_iFunctionGroup*FNGROUPSIZE));
    
    LEDButton f4 = (LEDButton) findViewById(R.id.throttleF4);
    f4.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon4 = m_Loco.getFunctionIcon(4+m_iFunctionGroup*FNGROUPSIZE);
    if( icon4 != null ) {
      f4.setBackgroundDrawable(new BitmapDrawable(icon4));
      f4.setText("");
    }
    else
      f4.setText(m_Loco.getFunctionText(4+m_iFunctionGroup*FNGROUPSIZE));
    
    LEDButton f5 = (LEDButton) findViewById(R.id.throttleF5);
    f5.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon5 = m_Loco.getFunctionIcon(5+m_iFunctionGroup*FNGROUPSIZE);
    if( icon5 != null ) {
      f5.setBackgroundDrawable(new BitmapDrawable(icon5));
      f5.setText("");
    }
    else
      f5.setText(m_Loco.getFunctionText(5+m_iFunctionGroup*FNGROUPSIZE));
    
    LEDButton f6 = (LEDButton) findViewById(R.id.throttleF6);
    f6.setBackgroundResource(android.R.drawable.btn_default);
    Bitmap icon6 = m_Loco.getFunctionIcon(6+m_iFunctionGroup*FNGROUPSIZE);
    if( icon6 != null ) {
      f6.setBackgroundDrawable(new BitmapDrawable(icon6));
      f6.setText("");
    }
    else
      f6.setText(m_Loco.getFunctionText(6+m_iFunctionGroup*FNGROUPSIZE));
    
    f0.setLines(1);
    f1.setLines(1);
    f2.setLines(1);
    f3.setLines(1);
    f4.setLines(1);
    f5.setLines(1);
    f6.setLines(1);
    /* This mixes up the layout. */
    if(f0.getText().length() > 3)
      f0.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f0.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f1.getText().length() > 3)
      f1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f2.getText().length() > 3)
      f2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f3.getText().length() > 3)
      f3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f4.getText().length() > 3)
      f4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f4.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f5.getText().length() > 3)
      f5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f5.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);

    if(f6.getText().length() > 3)
      f6.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    else
      f6.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), Typeface.BOLD);
    
    
    LEDButton Go = (LEDButton) findViewById(R.id.throttleGo);
    LEDButton Release = (LEDButton) findViewById(R.id.throttleRelease);
    LEDButton Direction = (LEDButton) findViewById(R.id.throttleDirection);
    LEDButton Lights = (LEDButton) findViewById(R.id.throttleLights);

    if( m_Loco != null ) {
      f1.ON = m_Loco.isFunction(1+m_iFunctionGroup*FNGROUPSIZE);
      f2.ON = m_Loco.isFunction(2+m_iFunctionGroup*FNGROUPSIZE);
      f3.ON = m_Loco.isFunction(3+m_iFunctionGroup*FNGROUPSIZE);
      f4.ON = m_Loco.isFunction(4+m_iFunctionGroup*FNGROUPSIZE);
      f5.ON = m_Loco.isFunction(5+m_iFunctionGroup*FNGROUPSIZE);
      f6.ON = m_Loco.isFunction(6+m_iFunctionGroup*FNGROUPSIZE);
      //Direction.ON = m_Loco.Dir;
      Go.setEnabled(m_RocrailService.AutoMode);
      Go.ON = m_Loco.isAutoStart();
      Lights.ON = m_Loco.isLights();
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
    
    LocoID = m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr);
    setContentView(R.layout.throttle);
    
    getWindow().setLayout((m_RocrailService.Prefs.SmallThrottle ? m_RocrailService.Prefs.ThrottleWidth:LayoutParams.WRAP_CONTENT), 
        (m_RocrailService.Prefs.SmallThrottle ? m_RocrailService.Prefs.ThrottleHeight:LayoutParams.FILL_PARENT) );

    Iterator<Mobile> it = m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Mobile loco = it.next();
      if( loco.isShow() )
        m_MobileList.add(loco);
    }
    
    it = m_RocrailService.m_Model.m_CarMap.values().iterator();
    while( it.hasNext() ) {
      Mobile car = it.next();
      if( car.isShow() )
        m_MobileList.add(car);
    }
    
    Collections.sort(m_MobileList, new LocoSort(m_RocrailService.Prefs.SortByAddr));

    if( LocoID != null ) {
      it = m_MobileList.iterator();
      int idx = 0;
      while( it.hasNext() ) {
        Mobile loco = it.next();
        if( LocoID.equals(loco.getID()) ) {
          m_iLocoSelected = idx;
        }
        idx++;
      }
    }    
    
    m_Loco = m_RocrailService.m_Model.m_LocoMap.get(LocoID);
    if( m_Loco == null )
      m_Loco = m_RocrailService.m_Model.m_CarMap.get(LocoID);
    locoSelected();

    Slider mSeekBar = (Slider)findViewById(R.id.Speed);
    mSeekBar.addListener(this);
    
    LEDButton Lights = (LEDButton) findViewById(R.id.throttleLights);
    Lights.setLongClickable(true);
    Lights.setOnLongClickListener(this);
    Lights.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipLights();
            ((LEDButton)v).ON = m_Loco.isLights();
            v.invalidate();
          }
        }
    });

    LEDButton fn = (LEDButton) findViewById(R.id.throttleFn);
    fn.noLED();
    fn.setLongClickable(true);
    fn.setOnLongClickListener(this);
    fn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          m_iFunctionGroup++;
          if( m_iFunctionGroup > 4 )
            m_iFunctionGroup = 0;
          LEDButton f5 = (LEDButton) findViewById(R.id.throttleF5);
          f5.setEnabled(m_iFunctionGroup< 4);
          LEDButton f6 = (LEDButton) findViewById(R.id.throttleF6);
          f6.setEnabled(m_iFunctionGroup< 4);
          updateFunctions();
        }
    });

    LEDButton f1 = (LEDButton) findViewById(R.id.throttleF1);
    f1.setLongClickable(true);
    f1.setOnLongClickListener(this);
    f1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(1+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(1+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton f2 = (LEDButton) findViewById(R.id.throttleF2);
    f2.setLongClickable(true);
    f2.setOnLongClickListener(this);
    f2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(2+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(2+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton f3 = (LEDButton) findViewById(R.id.throttleF3);
    f3.setLongClickable(true);
    f3.setOnLongClickListener(this);
    f3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(3+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(3+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton f4 = (LEDButton) findViewById(R.id.throttleF4);
    f4.setLongClickable(true);
    f4.setOnLongClickListener(this);
    f4.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(4+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(4+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton f5 = (LEDButton) findViewById(R.id.throttleF5);
    f5.setLongClickable(true);
    f5.setOnLongClickListener(this);
    f5.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(5+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(5+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton f6 = (LEDButton) findViewById(R.id.throttleF6);
    f6.setLongClickable(true);
    f6.setOnLongClickListener(this);
    f6.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipFunction(6+m_iFunctionGroup*FNGROUPSIZE);
            ((LEDButton)v).ON = m_Loco.isFunction(6+m_iFunctionGroup*FNGROUPSIZE);
            v.invalidate();
          }
        }
    });

    LEDButton Go = (LEDButton) findViewById(R.id.throttleGo);
    Go.noLED();
    if( m_Loco != null )
      Go.ON = m_Loco.isAutoStart();
    Go.setEnabled(m_RocrailService.AutoMode);
    Go.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.flipGo();
            ((LEDButton)v).ON = m_Loco.isAutoStart();
            v.invalidate();
          }
        }
    });

    LEDButton Release = (LEDButton) findViewById(R.id.throttleRelease);
    Release.noLED();
    Release.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          quitShowed = false;
          if( m_Loco != null ) {
            m_Loco.doRelease();
            ((LEDButton)v).ON = false;
          }
        }
    });
    Release.setLongClickable(true);
    Release.setOnLongClickListener(this);


    

    LEDButton Dir = (LEDButton) findViewById(R.id.throttleDirection);
    Dir.noLED();
    Dir.setLongClickable(true);
    Dir.setOnLongClickListener(this);
    Dir.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            quitShowed = false;
            m_Loco.flipDir();
            //((LEDButton)v).ON = m_Loco.Dir;
            v.invalidate();
            Slider mSeekBar = (Slider)findViewById(R.id.Speed);
            mSeekBar.setV(m_Loco.getSpeed());
            setDirSpeed((LEDButton)v);
          }
        }
    });
    
    ImageView image = (ImageView)findViewById(R.id.locoImage);
    image.setLongClickable(true);
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        quitShowed = false;
        Intent intent = null;
        if( m_RocrailService.Prefs.LocoCatList )
          intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoExpList.class);
        else
          intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoList.class);
        intent.putExtra("selected", m_iLocoSelected );
        startActivityForResult(intent, 1);
      }
  });

    image.setOnLongClickListener(new View.OnLongClickListener() {
      public boolean onLongClick(View v) {
        quitShowed = false;
        if( m_Loco != null ) {
          Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLoco.class);
          intent.putExtra("id", m_Loco.getID());
          startActivity(intent);
        }
        return true;
      }
  });


    
  }
  
  void setDirSpeed(LEDButton v) {
    //v.ON = m_Loco.Dir;
    if( m_Loco.isDir() )
      v.setText(""+m_Loco.getSpeed()+" >");
    else
      v.setText("< "+m_Loco.getSpeed());
  }

  void setConsist() {
    TextView Consist = (TextView)findViewById(R.id.LocoThrottleConsist);
    Consist.setBackgroundColor(Color.TRANSPARENT);
    Consist.setText("");
    if( m_Loco.getConsist().length() > 0 ) {
      Consist.setText(m_Loco.getConsist());
    }
    else {
      String master = m_RocrailService.m_Model.findMaster(m_Loco.getID());
      if( master.length() > 0 ) {
        Consist.setBackgroundColor(Color.rgb(180, 0, 0));
        Consist.setText(m_RocrailService.m_Model.findMaster(m_Loco.getID()));
        /*
        Consist.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            quitShowed = false;
            if( m_Loco != null ) {
              StringTokenizer tok = new StringTokenizer(((TextView)v).getText().toString(), "=");
              String masterID = tok.nextToken();
              Loco master = m_RocrailService.m_Model.getLoco(masterID);
              if( master != null ) {
                m_Loco = master;
                locoSelected();
              }
            }
          }
      });
      */
      }
    }
    
  }
  
  protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    if( requestCode == 1 ) {
      locoSelected(resultCode);
    }
    else if( requestCode == 2 ) {
      setConsist();
    }
  }
  
  
  @Override
  public void modelListLoaded(int MODELLIST) {
    if (MODELLIST == ModelListener.MODELLIST_LC) {
    }
  }

  protected void  onResume() {
    super.onResume();
    quitShowed = false;
    if( m_Loco != null ) {
      updateFunctions();
    }
  }

  public void locoSelected( int position) {
    System.out.println("locoSelected " + position);
    quitShowed = false;
    if( position != -1 ) {
      m_iLocoSelected = position;
      findLoco(position);
      locoSelected();
    }
  }
  
  public void locoSelected() {
    if( m_Loco != null ) {
      m_RocrailService.SelectedLoco = m_Loco;
      
      m_RocrailService.Prefs.saveLoco(m_Loco.getID(), m_RocrailService.ThrottleNr);
      
      System.out.println("locoSelected: "+m_Loco.getID()+"["+m_RocrailService.ThrottleNr+"]");

      LEDButton f0 = (LEDButton) findViewById(R.id.throttleLights);
      f0.ON = m_Loco.isLights();
      updateFunctions();
      
      LocoImage image = (LocoImage)findViewById(R.id.locoImage);
      image.ID = m_Loco.getID();
      if( m_Loco.getBmp(image) != null ) {
        image.setImageBitmap(m_Loco.getBmp(null));
      }
      else {
        image.setImageResource(R.drawable.noimg);
      }
      TextView ID = (TextView)findViewById(R.id.LocoThrottleID);
      ID.setText(m_Loco.getID());
      TextView Desc = (TextView)findViewById(R.id.LocoThrottleDesc);
      Desc.setText(m_Loco.getDescription());
      setConsist();
      
      Slider mSeekBar = (Slider)findViewById(R.id.Speed);
      mSeekBar.setRange(m_Loco.getVMax());
      mSeekBar.setV(m_Loco.getSpeed());
      mSeekBar.setButtonView(m_RocrailService.Prefs.ButtonView);
      mSeekBar.setDelta(m_RocrailService.Prefs.UseAllSpeedSteps?1:m_RocrailService.Prefs.VDelta);
      LEDButton mDir = (LEDButton)findViewById(R.id.throttleDirection);
      setDirSpeed(mDir);


    }
    else {
      LEDButton f0 = (LEDButton) findViewById(R.id.throttleLights);
      f0.ON = false;
      LocoImage image = (LocoImage)findViewById(R.id.locoImage);
      image.setImageResource(R.drawable.noimg);
      Slider mSeekBar = (Slider)findViewById(R.id.Speed);
      mSeekBar.setRange(100);
      mSeekBar.setButtonView(m_RocrailService.Prefs.ButtonView);
      mSeekBar.setDelta(m_RocrailService.Prefs.UseAllSpeedSteps?1:m_RocrailService.Prefs.VDelta);
      mSeekBar.setV(0);
      LEDButton mDir = (LEDButton)findViewById(R.id.throttleDirection);
      mDir.setText(""+0);
      TextView ID = (TextView)findViewById(R.id.LocoThrottleID);
      ID.setText("");
      TextView Desc = (TextView)findViewById(R.id.LocoThrottleDesc);
      Desc.setText("");
      TextView Consist = (TextView)findViewById(R.id.LocoThrottleConsist);
      Consist.setText("");
    }
  }


  @Override
  public void modelUpdate(int MODELLIST, String ID) {
    if( MODELLIST == ModelListener.MODELLIST_LC && m_Loco != null && m_Loco.getID() == ID ) {
      Slider bar = (Slider)findViewById(R.id.Speed);
      if( bar != null ) {
        bar.post(new Runnable() {
          public void run() {
            ActThrottle.this.updateFunctions();
            if( m_RocrailService.Prefs.SyncSpeed ) {
              Slider mSeekBar = (Slider)findViewById(R.id.Speed);
              if( !mSeekBar.isPressed() )
                mSeekBar.setV(m_Loco.getSpeed());
              LEDButton mDir = (LEDButton)findViewById(R.id.throttleDirection);
              setDirSpeed(mDir);
            }
          }
        });
      }
    }
  }


  @Override
  public void onSliderChange(Slider slider, int V) {
    quitShowed = false;
    if( m_Loco != null ) {
      m_Loco.setSpeed(V, m_RocrailService.Prefs.UseAllSpeedSteps);
      LEDButton mDir = (LEDButton)findViewById(R.id.throttleDirection);
      setDirSpeed(mDir);
    }
  }


  @Override
  public boolean onLongClick(View view) {
    if( view.getId() == R.id.throttleFn ) {
      Toast.makeText(getApplicationContext(), R.string.EmergencyStop,
          Toast.LENGTH_SHORT).show();
      if( m_RocrailService.Prefs.PowerOff4EBreak )
        m_RocrailService.sendMessage("sys", "<sys cmd=\"stop\"  informall=\"true\"/>");
      else
        m_RocrailService.sendMessage("sys", "<sys cmd=\"ebreak\" informall=\"true\"/>");
      return true;
    }
    if( view.getId() == R.id.throttleLights ) {
      Toast.makeText(getApplicationContext(), R.string.Dispatch,
          Toast.LENGTH_SHORT).show();
      if( m_Loco != null ) {
        m_Loco.Dispatch();
      }
      return true;
    }
    if( view.getId() == R.id.throttleRelease ) {
      Toast.makeText(getApplicationContext(), R.string.Power_OFF,
          Toast.LENGTH_SHORT).show();
      m_RocrailService.Power = false;
      m_RocrailService.sendMessage("sys", "<sys cmd=\"stop\" informall=\"true\"/>");
      return true;
    }
    if( view.getId() == R.id.throttleF1 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 1",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 1;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleF2 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 2",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 2;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleF3 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 3",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 3;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleF4 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 4",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 4;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleF5 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 5",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 5;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleF6 ) {
      Toast.makeText(getApplicationContext(), getString(R.string.Throttle) + " 6",
          Toast.LENGTH_SHORT).show();
      m_RocrailService.ThrottleNr = 6;
      m_Loco = m_RocrailService.m_Model.m_LocoMap.get(m_RocrailService.Prefs.getLocoID(m_RocrailService.ThrottleNr));
      locoSelected();
      return true;
    }
    if( view.getId() == R.id.throttleDirection ) {
      if( m_Loco != null ) {
        String masterConsist = m_RocrailService.m_Model.findMaster(m_Loco.getID());
        if( masterConsist.length() == 0 ) {
          Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoConsist.class);
          intent.putExtra("id", m_Loco.getID());
          startActivityForResult(intent, 2);
        }
        else {
          StringTokenizer tok = new StringTokenizer(masterConsist, "=");
          String masterID = tok.nextToken();
          Loco master = m_RocrailService.m_Model.getLoco(masterID);
          if( master != null ) {
            m_Loco = master;
            locoSelected();
          }
          
        }
      }
      return true;
    }
    return false;
  }
  
  @Override
  protected void onStop() {
    super.onPause();
    //if( RocrailServiceConnection != null)
      //unbindService(RocrailServiceConnection);
  }
}





