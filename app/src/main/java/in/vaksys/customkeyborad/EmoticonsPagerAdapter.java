package in.vaksys.customkeyborad;

import java.util.ArrayList;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class EmoticonsPagerAdapter extends PagerAdapter {

	ArrayList<String> emoticons;
	private static final int NO_OF_EMOTICONS_PER_PAGE = 20;
	FragmentActivity mActivity;
	EmoticonsGridAdapter.KeyClickListener mListener;
	Context mContext;
	LayoutInflater mLayoutInflater;

	public EmoticonsPagerAdapter(Context mContext,
			ArrayList<String> emoticons, EmoticonsGridAdapter.KeyClickListener listener) {
		this.emoticons = emoticons;
		this.mContext = mContext;
		this.mListener = listener;
	}

	@Override
	public int getCount() {
		return (int) Math.ceil((double) emoticons.size()
				/ (double) NO_OF_EMOTICONS_PER_PAGE);
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {

		View itemView = mLayoutInflater.inflate(R.layout.emoticons_grid, collection, false);

		int initialPosition = position * NO_OF_EMOTICONS_PER_PAGE;
		ArrayList<String> emoticonsInAPage = new ArrayList<String>();

		for (int i = initialPosition; i < initialPosition
				+ NO_OF_EMOTICONS_PER_PAGE
				&& i < emoticons.size(); i++) {
			emoticonsInAPage.add(emoticons.get(i));
		}

		GridView grid = (GridView) itemView.findViewById(R.id.emoticons_grid);
		EmoticonsGridAdapter adapter = new EmoticonsGridAdapter(
				mActivity.getApplicationContext(), emoticonsInAPage, position,
				mListener);
		grid.setAdapter(adapter);

		((ViewPager) collection).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}