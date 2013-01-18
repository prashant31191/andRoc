package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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
          EditText id = (EditText)findViewById(R.id.guestShortID);
          RadioButton step14 = (RadioButton)findViewById(R.id.Speedsteps14);
          RadioButton step28 = (RadioButton)findViewById(R.id.Speedsteps28);
          RadioButton step128 = (RadioButton)findViewById(R.id.Speedsteps128);
          
          RadioButton dcc = (RadioButton)findViewById(R.id.ProtocolDCC);
          RadioButton mm  = (RadioButton)findViewById(R.id.ProtocolMM);

          int addr = Integer.parseInt(et.getText().toString());
          
          int steps = 128;
          if( step28.isChecked() )
            steps = 28;
          else if( step14.isChecked() )
            steps = 14;
          
          String prot = "P";
          if( mm.isChecked() )
            prot = "M";
          if( dcc.isChecked() )
            prot = ((addr > 127) ? "L":"N");
          
          if( addr > 0 ) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%d\" shortid=\"%s\" spcnt=\"%d\" prot=\"%s\" V=\"0\"/>", 
                    addr, id.getText().toString(), steps, prot));
          }
          finish();
        }
    });
    

  }
  
}
