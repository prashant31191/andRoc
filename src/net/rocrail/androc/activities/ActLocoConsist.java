package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LocoImage;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActLocoConsist extends ActBase {
  Loco m_Loco = null;

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
    setContentView(R.layout.consist);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Loco = m_RocrailService.m_Model.getLoco(id);
    }
    else {
      m_Loco = m_RocrailService.SelectedLoco;
    }

    if( m_Loco == null )
      return;
    
    setTitle("Consist " + m_Loco.ID);
    
    LocoImage image = (LocoImage)findViewById(R.id.locoImage);
    
    if( m_Loco.getLocoBmp(null) != null ) {
      if( image != null ) {
        image.setImageBitmap(m_Loco.getLocoBmp(null));
      }
    }
    
    Button ViewConsist = (Button) findViewById(R.id.consistView);
    ViewConsist.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoList.class);
            intent.putExtra("consist", m_Loco.Consist );
            startActivity(intent);
          }
        }
    });
    
    Button AddMember = (Button) findViewById(R.id.consistAdd);
    AddMember.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoList.class);
            intent.putExtra("exclude", m_Loco.ID + "," + m_Loco.Consist );
            startActivityForResult(intent, 1);
          }
        }
    });
    

    Button RemoveMember = (Button) findViewById(R.id.consistDelete);
    RemoveMember.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Loco != null ) {
            Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoList.class);
            intent.putExtra("consist", m_Loco.Consist );
            startActivityForResult(intent, 2);
          }
        }
    });
    
    
  }

  protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    if( requestCode == 1 ) {
      String ID = data.getCharSequenceExtra("selectedID").toString();
      m_Loco.addConsistMember(ID);
    }
    else if( requestCode == 2 ) {
      String ID = data.getCharSequenceExtra("selectedID").toString();
      m_Loco.removeConsistMember(ID);
    }
  }
  
  
  
  
}
