package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

@SuppressLint("NewApi")
public class WidgetDataProvider implements RemoteViewsFactory {

	private String[] fragmentDate = new String[1];

	Context mContext = null;
	Cursor mCursor = null;
	public static final int COL_HOME = 3;
	public static final int COL_AWAY = 4;
	public static final int COL_HOME_GOALS = 6;
	public static final int COL_AWAY_GOALS = 7;
	public static final int COL_DATE = 1;
	public static final int COL_LEAGUE = 5;
	public static final int COL_MATCHDAY = 9;
	public static final int COL_ID = 8;
	public static final int COL_MATCHTIME = 2;

	public WidgetDataProvider(Context context, Intent intent) {
		mContext = context;
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
				R.layout.scores_list_item);
		mCursor.moveToPosition(position);
		mView.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
		mView.setContentDescription(R.id.home_name, mCursor.getString(COL_HOME));
		mView.setTextColor(R.id.home_name, Color.BLACK);

		mView.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
		mView.setContentDescription(R.id.away_name, mCursor.getString(COL_AWAY));
		mView.setTextColor(R.id.away_name, Color.BLACK);

		mView.setTextViewText(R.id.score_textview, Utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS)));
		mView.setContentDescription(R.id.score_textview, Utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS)));
		mView.setTextColor(R.id.score_textview, Color.BLACK);

		mView.setTextViewText(R.id.data_textview, mCursor.getString(COL_MATCHTIME));
		mView.setContentDescription(R.id.data_textview, mCursor.getString(COL_MATCHTIME));
		mView.setTextColor(R.id.data_textview, Color.BLACK);

		mView.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(
				mCursor.getString(COL_HOME)));
		mView.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(
				mCursor.getString(COL_HOME)));


		final Intent fillInIntent = new Intent();
		fillInIntent.setAction(WidgetProvider.ACTION_TOAST);
		final Bundle bundle = new Bundle();
		bundle.putString(WidgetProvider.EXTRA_STRING,
				mCursor.getString(COL_ID));
		fillInIntent.putExtras(bundle);
		mView.setOnClickFillInIntent(R.id.scores_full_list, fillInIntent);
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
		mCursor.moveToFirst();
		while (!mCursor.isAfterLast())
		{
			mCursor.moveToNext();
		}
	}

	@Override
	public void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
	}

}
