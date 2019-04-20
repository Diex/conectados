package com.diex.android.conectados;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class VisitorForm extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {


    Typeface merloBold;
    Typeface merloLight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // muestro primero el formulario
        setContentView(R.layout.activity_form);
        Toolbar toolbar = findViewById(R.id.toolbar);



        merloBold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_bolditalic.otf");

        merloLight = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_lightitalic.otf");

        beautyfyForm();


    }


    TextView name;
    TextView localidad;
    TextView email;
    Button submit;

    void beautyfyForm(){


        name = findViewById(R.id.name);
        name.setTypeface(merloLight);


        localidad = findViewById(R.id.location);
        localidad.setTypeface(merloLight);

        email = findViewById(R.id.email);
        email.setTypeface(merloLight);


        submit = findViewById(R.id.submit);
        submit.setTypeface(merloBold);
        submit.requestFocus(); // cambio el foco aca y despues agrego los callbacks


        Button clear = findViewById(R.id.clear);
        clear.setTypeface(merloBold);


        Button noinfo = findViewById(R.id.noinfo);
        noinfo.setTypeface(merloBold);


        name.setOnFocusChangeListener(this);
        localidad.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);


        submit.setOnClickListener(this);
        clear.setOnClickListener(this);
        noinfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.submit){

            final PostVisitor postVisitor = new PostVisitor(this);

            postVisitor.setSesion(MainActivity.uniqueID);
            postVisitor.setName(name.getText().toString());
            postVisitor.setLoc(localidad.getText().toString());
            postVisitor.setEmail(email.getText().toString());

            postVisitor.execute();

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            setResult(1);
            this.finish();



        }else if(v.getId() == R.id.noinfo){

            final PostVisitor postVisitor = new PostVisitor(this);

            postVisitor.setSesion(MainActivity.uniqueID);
            postVisitor.setName("anonymous user");
            postVisitor.setLoc("-");
            postVisitor.setEmail("-");

            postVisitor.execute();

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            setResult(1);
            this.finish();


        }else{

            submit.requestFocus(); // cambio el foco aca y despues agrego los callbacks
            // le agrego de nuevo los listeners...
            name.setOnFocusChangeListener(this);
            localidad.setOnFocusChangeListener(this);
            email.setOnFocusChangeListener(this);

            name.setText(R.string.form_name);
            localidad.setText(R.string.form_location);
            email.setText(R.string.form_email);
        }



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