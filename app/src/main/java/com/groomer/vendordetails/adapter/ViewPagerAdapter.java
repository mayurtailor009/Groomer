package com.groomer.vendordetails.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.groomer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by Deepak Singh on 27-Mar-16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageList;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private int[] images = new int[] {
            R.drawable.slide_img, R.drawable.slide_img
    };


    public ViewPagerAdapter(Context context, List<String> imageList) {
        this.context = context;
       // this.imageList = imageList;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .build();
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.viewpager_adapter_layout, container, false);
        assert imageLayout != null;
        final ImageView image = (ImageView) imageLayout.findViewById(R.id.image);
        //ImageLoader.getInstance().displayImage(imageList.get(position), image, options);
        image.setImageResource(images[position]);
        container.addView(image, 0);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
