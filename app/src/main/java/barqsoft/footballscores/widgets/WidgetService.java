package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.service.myFetchService;


@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService implements Loader.OnLoadCompleteListener<Cursor>,LoaderManager.LoaderCallbacks<Cursor> {
	private String[] fragmentDate = new String[1];
	List<String> mCollections = new ArrayList<String>();
	public static final int SCORES_LOADER = 0;
	CursorLoader mCursorLoader;
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		//getLoaderManager().initLoader(SCORES_LOADER,null,this);
		WidgetDataProvider dataProvider = new WidgetDataProvider(
				getApplicationContext(), intent,mCollections);
		return dataProvider;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Date fragmentdate = new Date(System.currentTimeMillis()+((-1)*86400000));
		SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
		fragmentDate[0]=mformat.format(fragmentdate);
		mCursorLoader = new CursorLoader(WidgetService.this, DatabaseContract.scores_table.buildScoreWithDate(),
				null,null,fragmentDate,null);
		mCursorLoader.startLoading();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			i++;
			cursor.moveToNext();
			mCollections.add(cursor.getString(0));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
	{
//		Date fragmentdate = new Date(System.currentTimeMillis()+((-1)*86400000));
//		SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
//		fragmentDate[0]=mformat.format(fragmentdate);
//		CursorLoader mCursorLoader = new CursorLoader(WidgetService.this, DatabaseContract.scores_table.buildScoreWithDate(),
//				null,null,fragmentDate,null);
//		mCursorLoader.startLoading();

		return mCursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
	{
		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			i++;
			cursor.moveToNext();
			mCollections.add(cursor.getString(0));
		}
		//mAdapter.swapCursor(cursor.);


	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader)
	{
		//mAdapter.swapCursor(null);
	}

}
