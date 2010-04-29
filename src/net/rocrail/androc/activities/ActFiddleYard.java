package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.FiddleYard;
import net.rocrail.androc.widgets.LEDButton;
import android.os.Bundle;
import android.view.View;

public class ActFiddleYard  extends ActBase {
  FiddleYard m_FiddleYard = null;
  
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
      m_FiddleYard = m_RocrailService.m_Model.m_FiddleYardMap.get(id);
    }

    if( m_FiddleYard == null )
      return;
    
    final LEDButton fyNext = (LEDButton) findViewById(R.id.fyNext);
    fyNext.ON = false;
    fyNext.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("seltab", 
              String.format("<seltab id=\"%s\" cmd=\"next\"/>", m_FiddleYard.ID));
        }
    });

    final LEDButton fyPrev = (LEDButton) findViewById(R.id.fyPrev);
    fyPrev.ON = false;
    fyPrev.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("seltab", 
              String.format("<seltab id=\"%s\" cmd=\"prev\"/>", m_FiddleYard.ID));
        }
    });


    updateTitle("FiddleYard \'"+m_FiddleYard.ID+"\'");
 
  }
  
}
