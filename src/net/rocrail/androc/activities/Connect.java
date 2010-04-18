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
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.rocrail.androc.R;
import net.rocrail.androc.andRoc;

public class Connect extends Base {
  andRoc      m_andRoc    = null;
/* 
  public Connect(andRoc androc) {
    m_andRoc = androc;
  }
*/
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initView();
  }
  

  public void initView() {
    setContentView(R.layout.connect);
    
    final Button button = (Button) findViewById(R.id.ButtonConnect);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click
          EditText s = (EditText) findViewById(R.id.connectHost);
          m_andRoc.getSystem().m_Host = s.getText().toString();
          s = (EditText) findViewById(R.id.connectPort);
          m_andRoc.getSystem().m_iPort = Integer.parseInt(s.getText().toString());
          // TODO: progress dialog
          try {
            m_andRoc.getSystem().connect();
            m_andRoc.Connected();
          }
          catch( Exception e ) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(Connect.this); 
            builder.setMessage(e.getClass().getName()+"\nCould not connect to " + m_andRoc.getSystem().m_Host+":"+m_andRoc.getSystem().m_iPort)
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

    /*
    EditText s = (EditText) findViewById(R.id.connectHost);
    s.setText(m_andRoc.getSystem().m_Host);
    s = (EditText) findViewById(R.id.connectPort);
    s.setText(""+m_andRoc.getSystem().m_iPort);
*/
  }

}
