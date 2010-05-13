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

import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.PoMListener;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LocoImage;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActLocoSetup extends ActBase implements OnItemSelectedListener, OnSeekBarChangeListener, PoMListener {
  Loco m_Loco = null;
  int CvVal = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_LOCO;
    Finish = true;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    updateTitle(m_Loco!=null?m_Loco.ID:"Loco setup");
    m_RocrailService.addPoMListener(this);
  }


  public void initView() {
    setContentView(R.layout.locosetup);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Loco = m_RocrailService.m_Model.getLoco(id);
    }
    else {
      m_Loco = m_RocrailService.SelectedLoco;
    }

    if( m_Loco == null )
      return;

    LocoImage image = (LocoImage)findViewById(R.id.locoImage);
    
    if( m_Loco.getLocoBmp(null) != null ) {
      if( image != null ) {
        image.setImageBitmap(m_Loco.getLocoBmp(null));
      }
    }
    
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        throttleView();
      }
    });

    SeekBar Vmin = (SeekBar)findViewById(R.id.locoVmin);
    Vmin.setOnSeekBarChangeListener(this);
    Vmin.setProgress(m_Loco.Vmin);
 
    SeekBar Vmid = (SeekBar)findViewById(R.id.locoVmid);
    Vmid.setOnSeekBarChangeListener(this);
    Vmid.setProgress(m_Loco.Vmid);

    SeekBar Vmax = (SeekBar)findViewById(R.id.locoVmax);
    Vmax.setOnSeekBarChangeListener(this);
    Vmax.setProgress(m_Loco.Vmax);
    

    EditText cvTxt = (EditText)findViewById(R.id.locoCV);
    cvTxt.setText(""+m_RocrailService.Prefs.CvNr);

    Button Write = (Button) findViewById(R.id.locoCVWrite);
    Write.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            EditText cvTxt = (EditText)findViewById(R.id.locoCV);
            int cv = Integer.parseInt(cvTxt.getText().toString());
            EditText valTxt = (EditText)findViewById(R.id.locoVal);
            int val = Integer.parseInt(valTxt.getText().toString());
            m_Loco.CVWrite(cv, val);
            m_RocrailService.Prefs.saveProgramming(cv);
          }
        }
    });
    

    Button Read = (Button) findViewById(R.id.locoCVRead);
    Read.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            EditText cvTxt = (EditText)findViewById(R.id.locoCV);
            int cv = Integer.parseInt(cvTxt.getText().toString());
            m_Loco.CVRead(cv);
            m_RocrailService.Prefs.saveProgramming(cv);
          }
        }
    });
    


  }
  
  
  @Override
  public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar bar) {
    if( m_Loco == null )
      return;
    
    if( bar.getId() == R.id.locoVmin ) {
      m_Loco.setVmin(bar.getProgress());
    }
    else if( bar.getId() == R.id.locoVmid ) {
      m_Loco.setVmid(bar.getProgress());
    }
    else if( bar.getId() == R.id.locoVmax ) {
      m_Loco.setVmax(bar.getProgress());
    }
    
  }

  @Override
  public void ReadResponse(int addr, int cv, int value) {
    CvVal = value;
    EditText valTxt = (EditText)findViewById(R.id.locoVal);
    valTxt.post(new Runnable() {
      public void run() {
        EditText valTxt = (EditText)findViewById(R.id.locoVal);
        valTxt.setText(""+CvVal);
      }
    });
  }

}
