package barqsoft.footballscores.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;


@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		WidgetDataProvider dataProvider = new WidgetDataProvider(
				getApplicationContext(), intent);
		return dataProvider;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}


}
