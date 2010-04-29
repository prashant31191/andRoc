package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Turntable;
import net.rocrail.androc.widgets.LEDButton;
import android.os.Bundle;
import android.view.View;

public class ActTurntable  extends ActBase {
  Turntable m_Turntable = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
  }


  public void initView() {
    setContentView(R.layout.fiddleyard);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Turntable = m_RocrailService.m_Model.m_TurntableMap.get(id);
    }

    if( m_Turntable == null )
      return;
    
    final LEDButton fyNext = (LEDButton) findViewById(R.id.fyNext);
    fyNext.ON = false;
    fyNext.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("tt", 
              String.format("<tt id=\"%s\" cmd=\"next\"/>", m_Turntable.ID));
        }
    });

    final LEDButton fyPrev = (LEDButton) findViewById(R.id.fyPrev);
    fyPrev.ON = false;
    fyPrev.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("tt", 
              String.format("<tt id=\"%s\" cmd=\"prev\"/>", m_Turntable.ID));
        }
    });


    updateTitle("Turntable \'"+m_Turntable.ID+"\'");
 
  }
  
}
