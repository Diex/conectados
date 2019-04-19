package com.diex.android.conectados;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VisitorForm extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {


    Typeface merloBold;
    Typeface merloLight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // muestro primero el formulario
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        merloBold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_bolditalic.otf");

        merloLight = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_lightitalic.otf");

        beautyfyForm();


    }


    TextView name;
    TextView localidad;
    TextView email;

    void beautyfyForm(){

        final ArrayList<TextView> fields = new ArrayList<TextView>();

        name = findViewById(R.id.name);
        name.setTypeface(merloLight);

        fields.add(name);

        localidad = findViewById(R.id.location);
        localidad.setTypeface(merloLight);

        fields.add(localidad);

        email = findViewById(R.id.email);
        email.setTypeface(merloLight);

        fields.add(email);


        Button submit = findViewById(R.id.submit);
        submit.setTypeface(merloBold);
        submit.requestFocus(); // cambio el foco aca y despues agrego los callbacks

        Button clear = findViewById(R.id.clear);
        clear.setTypeface(merloBold);

        name.setOnFocusChangeListener(this);
        localidad.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);


        clear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        name.setText(R.string.form_name);
        localidad.setText(R.string.form_location);
        email.setText(R.string.form_email);

        // le agrego de nuevo los listeners...
        name.setOnFocusChangeListener(this);
        localidad.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ((TextView) v).setText("");
        ((TextView) v).setOnFocusChangeListener(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_finish) {
//            setCurrentItem(installations.get((int) (Math.random()*installations.size())));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}