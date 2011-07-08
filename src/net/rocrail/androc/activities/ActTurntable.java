package net.rocrail.androc.activities;

import java.util.Iterator;

import net.rocrail.androc.objects.Turntable;
import net.rocrail.androc.widgets.LEDButton;


import net.rocrail.androc.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActTurntable  extends ActBase implements OnItemSelectedListener {
  Turntable m_Turntable = null;
  public int GotoTrack = 0;
  
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

    final Button fyTrack = (Button) findViewById(R.id.fyGotoTrack);
    fyTrack.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("tt", 
              String.format("<tt id=\"%s\" cmd=\"%s\"/>", m_Turntable.ID, GotoTrack));
        }
    });

    final LEDButton fyOpen = (LEDButton) findViewById(R.id.fyOpen);
    fyOpen.ON = m_Turntable.Closed;
    fyOpen.setMinWidth(200);
    fyOpen.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Turntable.OpenClose();
          ((LEDButton)v).ON = !m_Turntable.Closed;
          v.invalidate();
          
        }
    });



    // Track spinner
    Spinner s = (Spinner) findViewById(R.id.fyTracks);
    s.setPrompt(new String("Select Track"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    
    Iterator<Turntable.TTTrack> it = m_Turntable.Tracks.iterator();
    while( it.hasNext() ) {
      Turntable.TTTrack track = it.next();
      
     if ( !track.Description.equals(""))
       m_adapterForSpinner.add(""+track.Description);
     else
       m_adapterForSpinner.add(""+track.Nr);
    }
    
    s.setOnItemSelectedListener(this);


    updateTitle(getText(R.string.Turntable) + " \'"+m_Turntable.ID+"\'");
 
  }
  
  @Override
  public void onItemSelected(AdapterView<?> arg0, View view, int position, long longid ) {
    Spinner s = (Spinner) findViewById(R.id.fyTracks);
    String trackNr = (String)s.getSelectedItem();
    GotoTrack = Integer.parseInt(trackNr);
    
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
  }
  
}
