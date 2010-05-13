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

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LEDButton;
import net.rocrail.androc.widgets.LocoImage;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActLocoSetup extends ActBase implements OnItemSelectedListener, OnSeekBarChangeListener {
  Loco m_Loco = null;

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

    SeekBar Vmin = (SeekBar)findViewById(R.id.locoVmin);
    Vmin.setOnSeekBarChangeListener(this);
    Vmin.setProgress(m_Loco.Vmin);
 
    SeekBar Vmid = (SeekBar)findViewById(R.id.locoVmid);
    Vmid.setOnSeekBarChangeListener(this);
    Vmid.setProgress(m_Loco.Vmid);

    SeekBar Vmax = (SeekBar)findViewById(R.id.locoVmax);
    Vmax.setOnSeekBarChangeListener(this);
    Vmax.setProgress(m_Loco.Vmax);

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
  public void onStopTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

}
