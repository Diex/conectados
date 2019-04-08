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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ItemsAdapter extends PagerAdapter {

    //Implement PagerAdapter Class to handle individual page creation


    Typeface merloBold;
    Typeface merloLight;

    JSONObject itemsData;
    Context ctx;

    LayoutInflater inflater;    //Used to create individual pages

    public ItemsAdapter(Context ctx){
        this.ctx = ctx;

        merloBold = Typeface.createFromAsset(this.ctx.getAssets(),
                "fonts/merloneueround_bolditalic.otf");

        merloLight = Typeface.createFromAsset(this.ctx.getAssets(),
                "fonts/merloneueround_lightitalic.otf");

        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            itemsData =  new JSONObject(loadJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = ctx.getAssets().open("json/itemsData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public int getCount() {
        //Return total pages, here one for each data item

        try {
            return itemsData.getJSONArray("items").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
    //Create the given page (indicated by position)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View page = inflater.inflate(R.layout.item, null);

        ((TextView)page.findViewById(R.id.title)).setTypeface(merloBold);
        ((TextView)page.findViewById(R.id.description)).setTypeface(merloLight);
        ((TextView)page.findViewById(R.id.description)).setMovementMethod(new ScrollingMovementMethod());

        JSONObject item;

        try {

            item = itemsData.getJSONArray("items").getJSONObject(position%2);

            ((TextView)page.findViewById(R.id.title)).setText(item.getString("name"));
            ((TextView)page.findViewById(R.id.description)).setText(item.getString("description"));

            int id = ctx.getResources().getIdentifier(item.getString("img"),
                    "drawable", ctx.getPackageName());

            Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), id);
            ((ImageView)page.findViewById(R.id.imageView)).setImageBitmap(bm);


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
