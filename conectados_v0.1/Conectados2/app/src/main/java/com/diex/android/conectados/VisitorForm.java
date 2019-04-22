package com.diex.android.conectados;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class VisitorForm extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {


    public static Typeface merloBold;
    public static Typeface merloLight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // muestro primero el formulario
        setContentView(R.layout.activity_form);

//        Toolbar toolbar;
//        toolbar = (Toolbar) findViewById(R.id.instructions);
//        setSupportActionBar(toolbar);






        merloBold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_bolditalic.otf");

        merloLight = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/merloneueround_lightitalic.otf");

        beautyfyForm();


    }

    Spinner ages;
    TextView name;
    TextView localidad;
    TextView email;
    Button submit;

    void beautyfyForm(){
        MySpinnerAdapter adapter = new MySpinnerAdapter(
                this,
                R.layout.spinner_item,
                Arrays.asList(getResources().getStringArray(R.array.form_ages))
        );
        ages = findViewById(R.id.age);
        ages.setAdapter(adapter);


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

            final Postino postino = new Postino(this);

            postino.setSesion(MainActivity.uniqueID);
            postino.setName(name.getText().toString());
            postino.setAge(""+ages.getSelectedItemPosition());
            postino.setLoc(localidad.getText().toString());
            postino.setEmail(email.getText().toString());

//            postino.execute();
            postino.setMessageType(Postino.VISITOR);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            setResult(1);
            this.finish();



        }else if(v.getId() == R.id.noinfo){

            final Postino postino = new Postino(this);

            postino.setSesion(MainActivity.uniqueID);
            postino.setName("anonymous user");
            postino.setLoc("-");
            postino.setEmail("-");

            postino.execute();

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
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_favorite) {
//            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(VisitorForm.merloLight);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(VisitorForm.merloLight);
            return view;
        }


    }
}