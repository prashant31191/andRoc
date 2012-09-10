package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.StageBlock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActStage extends ActBase  {
  StageBlock m_Stage = null;

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
    setContentView(R.layout.stage);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Stage = m_RocrailService.m_Model.m_StageBlockMap.get(id);
    }

    if( m_Stage == null )
      return;
    
    updateTitle("Stage \'"+m_Stage.ID+"\'");

    final Button OpenEnter = (Button) findViewById(R.id.stageEnter);
    OpenEnter.setText(m_Stage.ExitClosed?getText(R.string.OpenExit):getText(R.string.CloseExit));
    OpenEnter.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("sb", 
              String.format("<sb id=\"%s\" state=\"%s\"/>", m_Stage.ID, (m_Stage.Closed ? "open":"closed") ) );
          finish();
        }
    });

    final Button OpenExit = (Button) findViewById(R.id.stageExit);
    OpenExit.setText(m_Stage.ExitClosed?getText(R.string.OpenExit):getText(R.string.CloseExit));
    OpenExit.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("sb", 
              String.format("<sb id=\"%s\" exitstate=\"%s\"/>", m_Stage.ID, (m_Stage.ExitClosed ? "open":"closed") ) );
          finish();
        }
    });
    
    final Button Compress = (Button) findViewById(R.id.stageCompress);
    Compress.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("sb", 
              String.format("<sb id=\"%s\" cnd=\"compress\"/>", m_Stage.ID ) );
          finish();
        }
    });
    
    
  }
  
}
