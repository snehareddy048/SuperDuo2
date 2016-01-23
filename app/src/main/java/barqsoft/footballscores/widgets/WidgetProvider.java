package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

public class WidgetProvider extends AppWidgetProvider {

	public static final String ACTION_TOAST = "barqsoft.footballscores.widgets.ACTION_TOAST";
	public static final String EXTRA_STRING = "barqsoft.footballscores.widgets.EXTRA_STRING";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_TOAST)) {
			String item = intent.getExtras().getString(EXTRA_STRING);
			Toast.makeText(context, item, Toast.LENGTH_LONG).show();
		}
		super.onReceive(context, intent);
	}

	@SuppressLint("NewApi")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int widgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);

//			// Create an Intent to launch MainActivity
//			Intent intent = new Intent(context, MainActivity.class);
//			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//			views.setOnClickPendingIntent(R.id.widgetCollectionList, pendingIntent);

			// Set up the collection
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				setRemoteAdapter(context, views);
			} else {
				setRemoteAdapterV11(context, views);
			}

			Intent clickIntentTemplate =  new Intent(context, MainActivity.class);
			PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
					.addNextIntentWithParentStack(clickIntentTemplate)
					.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setPendingIntentTemplate(R.id.widgetCollectionList, clickPendingIntentTemplate);
			views.setEmptyView(R.id.widgetCollectionList, R.id.widget_empty);

			// Tell the AppWidgetManager to perform an update on the current app widget
			appWidgetManager.updateAppWidget(widgetId, views);

//
//			RemoteViews mView = initViews(context, appWidgetManager, widgetId);
//
//			// Adding collection list item handler
//			final Intent onItemClick = new Intent(context, WidgetProvider.class);
//			onItemClick.setAction(ACTION_TOAST);
//			onItemClick.setData(Uri.parse(onItemClick
//					.toUri(Intent.URI_INTENT_SCHEME)));
//			final PendingIntent onClickPendingIntent = PendingIntent
//					.getBroadcast(context, 0, onItemClick,
//							PendingIntent.FLAG_UPDATE_CURRENT);
//			mView.setPendingIntentTemplate(R.id.widgetCollectionList,
//					onClickPendingIntent);
//
//			//Shyam Add mView.setempty view
//
//			appWidgetManager.updateAppWidget(widgetId, mView);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private RemoteViews initViews(Context context,
			AppWidgetManager widgetManager, int widgetId) {

		RemoteViews mView = new RemoteViews(context.getPackageName(),
				R.layout.widget_provider_layout);

		Intent intent = new Intent(context, WidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);

		return mView;
	}

	/**
	 * Sets the remote adapter used to fill in the list items
	 *
	 * @param views RemoteViews to set the RemoteAdapter
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(R.id.widgetCollectionList,
				new Intent(context, WidgetService.class));
	}

	/**
	 * Sets the remote adapter used to fill in the list items
	 *
	 * @param views RemoteViews to set the RemoteAdapter
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(0, R.id.widgetCollectionList,
				new Intent(context, WidgetService.class));
	}
}
