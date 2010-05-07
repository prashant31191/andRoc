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
import net.rocrail.androc.interfaces.MessageListener;
import net.rocrail.androc.widgets.LEDButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActSystem extends ActBase implements MessageListener {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_MENU | ActBase.MENU_LAYOUT | ActBase.MENU_PREFERENCES;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    m_RocrailService.setMessageListener(this);
    updateTitle("System");
  }
  
  protected void onDestroy() {
    super.onDestroy();
    m_RocrailService.setMessageListener(null);
  }

  
  public void updateMessageList() {
    try {
      TextView msgList = (TextView) findViewById(R.id.systemMessages);
      msgList.setText("");
      Iterator<String> it = m_RocrailService.MessageList.iterator();
      while( it.hasNext()) {
        msgList.append(it.next()+"\n");
      }
    }
    catch(Exception e ) {
      // msgList is no longer valid
      e.printStackTrace();
    }
  }
  
  public void initView() {
    setContentView(R.layout.system);

    updateMessageList();
    
    final TextView msgList = (TextView) findViewById(R.id.systemMessages);
    msgList.setText("");
    Iterator<String> it = m_RocrailService.MessageList.iterator();
    while( it.hasNext()) {
      msgList.append(it.next()+"\n");
    }

    final LEDButton powerON = (LEDButton) findViewById(R.id.systemPowerON);
    powerON.ON = m_RocrailService.Power;
    powerON.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.Power = true;
          LEDButton powerOFF = (LEDButton) findViewById(R.id.systemPowerOFF);
          powerOFF.ON = !m_RocrailService.Power;
          powerOFF.invalidate();
          ((LEDButton)v).ON = m_RocrailService.Power;
          m_RocrailService.sendMessage("sys", "<sys cmd=\"go\"/>");
        }
    });

    final LEDButton powerOFF = (LEDButton) findViewById(R.id.systemPowerOFF);
    powerOFF.ON = !m_RocrailService.Power;
    powerOFF.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.Power = false;
          LEDButton powerON = (LEDButton) findViewById(R.id.systemPowerON);
          powerON.ON = m_RocrailService.Power;
          powerON.invalidate();
          ((LEDButton)v).ON = !m_RocrailService.Power;
          m_RocrailService.sendMessage("sys", "<sys cmd=\"stop\"/>");
        }
    });
    
    final Button initField = (Button) findViewById(R.id.systemInitField);
    initField.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("model", "<model cmd=\"initfield\"/>");
        }
    });
    
    final LEDButton autoON = (LEDButton) findViewById(R.id.systemAutoON);
    autoON.ON = m_RocrailService.AutoMode;
    autoON.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.AutoMode = !m_RocrailService.AutoMode;
          ((LEDButton)v).ON = m_RocrailService.AutoMode;
          m_RocrailService.sendMessage("sys", String.format("<auto cmd=\"%s\"/>", m_RocrailService.AutoMode?"on":"off") );
        }
    });

    final LEDButton autoStart = (LEDButton) findViewById(R.id.systemAutoStart);
    autoStart.ON = m_RocrailService.AutoStart;
    autoStart.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          // TODO: show alert if going from stop to start
          if(!m_RocrailService.AutoStart) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActSystem.this);
            builder.setMessage(R.string.start_all_locos)
                   .setCancelable(false)
                   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                          m_RocrailService.AutoStart = !m_RocrailService.AutoStart;
                          m_RocrailService.sendMessage("sys", String.format("<auto cmd=\"%s\"/>", m_RocrailService.AutoStart?"start":"stop") );
                          LEDButton v = (LEDButton) findViewById(R.id.systemAutoStart);
                          v.ON = m_RocrailService.AutoStart;
                       }
                   })
                   .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                       }
                   });
            AlertDialog alert = builder.create();
            alert.show();
          }
          else {
            m_RocrailService.AutoStart = !m_RocrailService.AutoStart;
            m_RocrailService.sendMessage("sys", String.format("<auto cmd=\"%s\"/>", m_RocrailService.AutoStart?"start":"stop") );
            ((LEDButton)v).ON = m_RocrailService.AutoStart;
          }
          ((LEDButton)v).ON = m_RocrailService.AutoStart;
        }
    });


    
  }

  @Override
  public void newMessages() {
    // use post method to refresh the message list
    final TextView msgList = (TextView) findViewById(R.id.systemMessages);
    if( msgList != null )
      msgList.post(new UpdateMessages(this));

  }

}


class UpdateMessages implements Runnable {
  ActSystem system = null;
  
  public UpdateMessages(ActSystem system) {
    this.system = system;
    
  }
  
  @Override
  public void run() {
    system.updateMessageList();
  }
  
}

