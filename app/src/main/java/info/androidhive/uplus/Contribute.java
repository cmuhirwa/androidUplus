package info.androidhive.uplus;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

public class Contribute extends AppCompatActivity {
    Spinner spnContribute;
    android.support.v7.widget.Toolbar toolbar;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        spnContribute=(Spinner)findViewById(R.id.spnContribute);
        try
        {
            toolbar=(Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBack();
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        try
        {
            ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,
                    R.array.Account, android.R.layout.simple_list_item_1);

            adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnContribute.setAdapter(adp3);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void getBack()
    {
        finish();
    }
}
