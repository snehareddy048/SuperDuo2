package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory {

	List<String> mCollections = new ArrayList<String>();
	private String[] fragmentDate = new String[1];

	Context mContext = null;
	Cursor mCursor = null;

	public WidgetDataProvider(Context context, Intent intent,List<String> mCollections) {
		mContext = context;
		this.mCollections=mCollections;
	}

	@Override
	public int getCount() {
		 return mCursor == null ? 0 : mCursor.getCount();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews mView = new RemoteViews(mContext.getPackageName(),
				android.R.layout.simple_list_item_1);
		mView.setTextViewText(android.R.id.text1, mCollections.get(position));

		mView.setTextColor(android.R.id.text1, Color.BLACK);
		
		final Intent fillInIntent = new Intent();
		fillInIntent.setAction(WidgetProvider.ACTION_TOAST);
		final Bundle bundle = new Bundle();
		bundle.putString(WidgetProvider.EXTRA_STRING,
				mCollections.get(position));
		fillInIntent.putExtras(bundle);
		mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
		return mView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
		initData();
	}

	@Override
	public void onDataSetChanged() {
		initData();
	}

	private void initData() {
		if (mCursor != null) {
			mCursor.close();
		}
		// This method is called by the app hosting the widget (e.g., the launcher)
		// However, our ContentProvider is not exported so it doesn't have access to the
		// data. Therefore we need to clear (and finally restore) the calling identity so
		// that calls use our process and permission
		final long identityToken = Binder.clearCallingIdentity();
		Date fragmentdate = new Date(System.currentTimeMillis()+((-1)*86400000));
		SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
		fragmentDate[0]=mformat.format(fragmentdate);

		mCursor =mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
				null, null, fragmentDate, null);
		Binder.restoreCallingIdentity(identityToken);
		mCollections.clear();
		mCursor.moveToFirst();
		while (!mCursor.isAfterLast())
		{
			mCollections.add(mCursor.getString(0));
			mCursor.moveToNext();
		}

//		mCollections.clear();
//		for (int i = 1; i <= 10; i++) {
//			mCollections.add("Sneha " + i);
//		}
	}

	@Override
	public void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
	}

}
