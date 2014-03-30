import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class GalleryPagerAdapter extends PagerAdapter {

    private Activity activity;
    private int[] drawableIDs;

    public GalleryPagerAdapter(Activity activity,int[] drawableIDs){

        this.activity = activity;
        this.drawableIDs = drawableIDs;
    }

    @Override
    public int getCount() {
        return drawableIDs.length;
    }


    @Override
    public Object instantiateItem(View collection, int position) {

        ImageView imageView = new ImageView(activity);

        imageView.setBackgroundResource(drawableIDs[position]);

        ((ViewPager) collection).addView(imageView,0);

        return imageView;
    }

    /**
     * Remove a page for the given position. The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate()}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((ImageView) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((ImageView)object);
    }

    @Override
    public void finishUpdate(View arg0) {}

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {}

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {}
}