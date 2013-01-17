package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActGuestLoco extends ActBase {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    Finish = true;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }


  public void initView() {
    setContentView(R.layout.guestloco);
    setTitle(R.string.GuestLoco);

    Button AddMember = (Button) findViewById(R.id.guestAdd);
    AddMember.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          EditText et = (EditText)findViewById(R.id.guestAddress);
          int addr = Integer.parseInt(et.getText().toString());
          if( addr > 0 ) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%d\" V=\"0\"/>", addr));
          }
          finish();
        }
    });
    

  }
  
}
