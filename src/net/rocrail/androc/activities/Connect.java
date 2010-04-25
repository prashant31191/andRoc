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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.interfaces.SystemListener;

public class Connect extends Base implements ModelListener, SystemListener {
  static final int PROGRESS_DIALOG = 0;
  boolean progressPlan = false;
  int progressValue = 0;
  ProgressDialog progressDialog = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    restorePreferences();
    TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    if(tm.getLine1Number()!=null)
      m_RocrailService.m_DevideId = tm.getLine1Number();
    else
      m_RocrailService.m_DevideId = tm.getDeviceId();
    
    m_RocrailService.m_Model.addListener(this);
    m_RocrailService.addListener(this);
    initView();
  }

  @Override
  public void modelListLoaded(int MODELLIST) {
    if( !progressPlan )
      return;
    
    if( MODELLIST == ModelListener.MODELLIST_PLAN ) {
      progressPlan  = false;
      progressValue = 100;
    }
    else {
      // progress
      progressValue += 10;
    }
    
    Message msg = handler.obtainMessage();
    Bundle b = new Bundle();
    b.putInt("total", progressValue);
    msg.setData(b);
    handler.sendMessage(msg);
    
    if( progressValue >= 100 ) {
      Connect.this.throttleView();
      Connect.this.finish();
    }

  }
  
  protected Dialog onCreateDialog(int id) {
    switch(id) {
    case PROGRESS_DIALOG:
        progressDialog = new ProgressDialog(Connect.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading plan...");
        return progressDialog;
    default:
        return null;
    }
  }

  // Define the Handler that receives messages from the thread and update the progress
  final Handler handler = new Handler() {
      public void handleMessage(Message msg) {
          int total = msg.getData().getInt("total");
          progressDialog.setProgress(total);
          if (total >= 100){
              dismissDialog(PROGRESS_DIALOG);
          }
      }
  };

  
  public void initView() {
    setContentView(R.layout.connect);
    
    final Button button = (Button) findViewById(R.id.ButtonConnect);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click
          EditText s = (EditText) findViewById(R.id.connectHost);
          m_RocrailService.m_Host = s.getText().toString();
          s = (EditText) findViewById(R.id.connectPort);
          m_RocrailService.m_iPort = Integer.parseInt(s.getText().toString());
          // TODO: progress dialog
          try {
            m_RocrailService.connect();
            progressPlan = true;
            progressValue = 0;
            
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("host", m_RocrailService.m_Host);
            editor.putInt("port", m_RocrailService.m_iPort);
            editor.commit();
            
            /*
            progressDialog = ProgressDialog.show(Connect.this, "", 
                "Loading. Please wait...", true, true);
            */
            showDialog(PROGRESS_DIALOG);
            
          }
          catch( Exception e ) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(Connect.this); 
            builder.setMessage(e.getClass().getName()+"\nCould not connect to " + 
                m_RocrailService.m_Host+":"+m_RocrailService.m_iPort)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //andRoc.this.finish();
              dialog.cancel();
            }});
            
            AlertDialog alert = builder.create();
            alert.show();
          
          }
        }
    });

    if( m_RocrailService != null ) {
      EditText s = (EditText) findViewById(R.id.connectHost);
      s.setText(m_RocrailService.m_Host);
      s = (EditText) findViewById(R.id.connectPort);
      s.setText(""+m_RocrailService.m_iPort);
    }

  }

  @Override
  public void SystemDisconnected() {
    Connect.this.connectView();
    Connect.this.setVisible(true);
  }

  @Override
  public void SystemShutdown() {
    Connect.this.connectView();
    Connect.this.setVisible(true);
  }

}
