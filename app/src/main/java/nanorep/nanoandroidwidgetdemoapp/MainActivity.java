package nanorep.nanoandroidwidgetdemoapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import NanoRep.NanoRep;
import nanorep.nanowidget.DataClasse.NRFetchedDataManager;
import nanorep.nanowidget.NRWidgetFragment;

public class MainActivity extends AppCompatActivity implements NRWidgetFragment.OnFragmentInteractionListener {

    private NRWidgetFragment nanoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        nanoFragment = NRWidgetFragment.newInstance(null, null);
        nanoFragment.setNanoRep(new NanoRep("us", null));
        Button loadButton = (Button)findViewById(R.id.button);
        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().add(R.id.root_layout, nanoFragment, "test").commit();
//                    NanoRep nanoRep = new NanoRep("Main", null);
//                    NRFetchedDataManager mFetchedDataManager = new NRFetchedDataManager(nanoRep);
                }
            });
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
