package net.rocrail.androc.activities;

import java.util.Iterator;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Block;
import net.rocrail.androc.objects.FiddleYard;
import net.rocrail.androc.widgets.LEDButton;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActFiddleYard  extends ActBase implements OnItemSelectedListener {
  FiddleYard m_FiddleYard = null;
  int GotoTrack = 1;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
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

    final Button fyTrack = (Button) findViewById(R.id.fyGotoTrack);
    fyTrack.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("seltab", 
              String.format("<seltab id=\"%s\" cmd=\"%s\"/>", m_FiddleYard.ID, GotoTrack));
        }
    });


    // Track spinner
    Spinner s = (Spinner) findViewById(R.id.fyTracks);
    s.setPrompt(new String("Select Track"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    for( int i = 0; i < m_FiddleYard.NrTracks; i++ ) {
      m_adapterForSpinner.add(""+(i+1));
    }
    
    s.setOnItemSelectedListener(this);

    
    
    updateTitle("FiddleYard \'"+m_FiddleYard.ID+"\'");
 
  }

  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longid ) {
    GotoTrack = position + 1;
    
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
  }
  
}
