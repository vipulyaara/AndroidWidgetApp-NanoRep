package nanorep.nanoandroidwidgetdemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crittercism.app.Crittercism;
import com.nanorep.nanorepsdk.Connection.NRError;


import nanorep.Nanorep;
import nanorep.NanorepBuilder;
import nanorep.ResponseParams.NRConfiguration;
import nanorep.nanowidget.NRWidgetFragment;

public class MainActivity extends AppCompatActivity implements NRWidgetFragment.NRWidgetFragmentListener {

    private NRWidgetFragment nanoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        nanoFragment = NRWidgetFragment.newInstance(null, null);
        nanoFragment.setListener(this);
        Crittercism.initialize(getApplicationContext(), "d59e30ede3c34d0bbf19d0237c2f1bc800444503");
        Button loadButton = (Button)findViewById(R.id.button);
        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setVisibility(View.INVISIBLE);
                    Nanorep.AccountParams accountParams = new Nanorep.AccountParams();
                    accountParams.setAccount("yatra");
                    accountParams.setKnowledgeBase("English");
                    Nanorep test = NanorepBuilder.createNanorep(getApplicationContext(), accountParams);
                    nanoFragment.setNanoRep(test);
                    test.fetchConfiguration(new Nanorep.OnConfigurationFetchedListener() {
                        @Override
                        public void onConfigurationFetched(NRConfiguration configuration, NRError error) {
                            getSupportFragmentManager().beginTransaction().add(R.id.root_layout, nanoFragment, "test").commit();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onCancelWidget(NRWidgetFragment widgetFragment) {
        ((Button)findViewById(R.id.button)).setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(nanoFragment).commit();
    }
}
