package com.diex.android.conectados;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diex.android.conectados.estimote.VisitPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ItemsAdapter extends PagerAdapter {

    //Implement PagerAdapter Class to handle individual page creation


    Typeface merloBold;
    Typeface merloLight;


    Context ctx;

    LayoutInflater inflater;    //Used to create individual pages
    ArrayList<VisitPoint> installations;
    public ItemsAdapter(Context ctx, ArrayList<VisitPoint> installations){
        this.ctx = ctx;
        this.installations = installations;


        merloBold = Typeface.createFromAsset(this.ctx.getAssets(),
                "fonts/merloneueround_bolditalic.otf");

        merloLight = Typeface.createFromAsset(this.ctx.getAssets(),
                "fonts/merloneueround_lightitalic.otf");

        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return installations.size();

    }
    //Create the given page (indicated by position)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View page = inflater.inflate(R.layout.item, null);

        ((TextView)page.findViewById(R.id.title)).setTypeface(merloBold);


//        ((TextView)page.findViewById(R.id.description)).setTypeface(merloLight);
//        ((TextView)page.findViewById(R.id.description)).setMovementMethod(new ScrollingMovementMethod());


        VisitPoint current = installations.get(position);
        ((TextView)page.findViewById(R.id.title)).setText(current.getTitle());
//        ((TextView)page.findViewById(R.id.description)).setText(current.getDescription());

        int id = ctx.getResources().getIdentifier(current.getImg(),
                "drawable", ctx.getPackageName());


        Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), id);
        ((ImageView)page.findViewById(R.id.imageView)).setImageBitmap(bm);


        //Add the page to the front of the queue
        ((ViewPager) container).addView(page, 0);

        return page;
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        //See if object from instantiateItem is related to the given view
        //required by API
        return arg0==(View)arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
        object=null;
    }



}
