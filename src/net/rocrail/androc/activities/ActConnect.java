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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.ModelListener;
import net.rocrail.androc.interfaces.SystemListener;
import net.rocrail.androc.objects.Loco;

public class ActConnect extends ActBase implements ModelListener, SystemListener, OnItemSelectedListener {
  static final int PROGRESS_DIALOG = 0;
  boolean progressPlan = false;
  int progressValue = 0;
  ProgressDialog progressDialog = null;
  List<ConnectionDetails> conList = null; 

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    restorePreferences();
    conList = ConHisto.parse(m_RocrailService.m_Recent);
    
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
    else if( MODELLIST == ModelListener.MODELLIST_PLAN_START ) {
      progressValue = 5;
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
      ActConnect.this.throttleView();
      ActConnect.this.finish();
    }

  }
  
  protected Dialog onCreateDialog(int id) {
    switch(id) {
    case PROGRESS_DIALOG:
        progressDialog = new ProgressDialog(ActConnect.this);
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

  
  void doConnect(String host, int port) {
    m_RocrailService.m_Host  = host;
    m_RocrailService.m_iPort = port;
    
    try {
      m_RocrailService.connect();
      progressPlan = true;
      progressValue = 0;
      
      showDialog(PROGRESS_DIALOG);
      
    }
    catch( Exception e ) {
      e.printStackTrace();
      AlertDialog.Builder builder = new AlertDialog.Builder(ActConnect.this); 
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
  
  
  public void initView() {
    setContentView(R.layout.connect);
    
    Spinner recent = (Spinner) findViewById(R.id.connectRecent);
    
    recent.setPrompt(new String("Select connection"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    recent.setAdapter(m_adapterForSpinner);

    
    m_adapterForSpinner.add("none");
    // get the connections
    Iterator<ConnectionDetails> it = conList.iterator();
    while (it.hasNext()) {
      ConnectionDetails con = it.next();
      m_adapterForSpinner.add(con.toString());
    }
    recent.setOnItemSelectedListener(this);

    
    final Button button = (Button) findViewById(R.id.ButtonConnect);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click
          EditText s = (EditText) findViewById(R.id.connectHost);
          m_RocrailService.m_Host = s.getText().toString();
          s = (EditText) findViewById(R.id.connectPort);
          m_RocrailService.m_iPort = Integer.parseInt(s.getText().toString());

          ConHisto.addToList("-", m_RocrailService.m_Host, m_RocrailService.m_iPort, conList);
          m_RocrailService.m_Recent = ConHisto.serialize(conList);
          SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
          SharedPreferences.Editor editor = settings.edit();
          editor.putString("host", m_RocrailService.m_Host);
          editor.putInt("port", m_RocrailService.m_iPort);
          editor.putString("recent", m_RocrailService.m_Recent);
          editor.commit();

          doConnect(m_RocrailService.m_Host, m_RocrailService.m_iPort);
          
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
    ActConnect.this.connectView();
    ActConnect.this.setVisible(true);
  }

  @Override
  public void SystemShutdown() {
    ActConnect.this.connectView();
    ActConnect.this.setVisible(true);
  }

  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longID) {
    if( position > 0 ) {
      ConnectionDetails con = conList.get(position-1);
      doConnect(con.HostName, con.Port);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }

}

class ConHisto {
  /*
   * Add a connection to the list.
   * An existing connection with the same key will be removed first. 
   */
  public static void addToList(ConnectionDetails con, List<ConnectionDetails> conList) {
    for (Iterator<ConnectionDetails> it = conList.iterator(); it.hasNext();) {
      ConnectionDetails c = it.next();
      if( c.equals(con) ) {
        conList.remove(c);
      }
    }
    conList.add(0,con);
  }

  public static void addToList(String alias, String host, int port, List<ConnectionDetails> conList) {
    ConnectionDetails con = new ConnectionDetails(alias, host, port);
    ConHisto.addToList(con, conList);
  }

  public static List<ConnectionDetails> parse(String recent ) {
    List<ConnectionDetails> conList = new ArrayList<ConnectionDetails>();
    // parse the recent string and instantiate a class for each
    StringTokenizer tok = new StringTokenizer( recent, ";");
    while( tok.hasMoreTokens()) {
      StringTokenizer constr = new StringTokenizer( tok.nextToken(), ":");
      ConnectionDetails con = new ConnectionDetails("-", constr.nextToken(), Integer.parseInt(constr.nextToken()));
      ConHisto.addToList(con, conList);
    }
    return conList;
  }
    
  public static String serialize(List<ConnectionDetails> conList ) {
    // construct the recent string new
    String recent = "";
    for (Iterator<ConnectionDetails> it = conList.iterator(); it.hasNext();) {
      ConnectionDetails con = it.next();
      recent = recent.concat(con.HostName+":"+con.Port+";");
    }
    return recent;
  }
  
}

class ConnectionDetails {
  String Alias    = "";
  String HostName = "";
  int    Port     = 0;
  public ConnectionDetails(String Alias, String HostName, int Port) {
    this.Alias    = Alias;
    this.HostName = HostName;
    this.Port     = Port;
  }
  public boolean equals( ConnectionDetails con ) {
    if( con.HostName.equals(this.HostName) && con.Port == this.Port )
      return true;
    return false;
  }
  public String toString() {
    return HostName+":"+Port;
  }
}
