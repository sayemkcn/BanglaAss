package sayem.picosoft.banglaassistant.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import sayem.picosoft.banglaassistant.R;
import sayem.picosoft.banglaassistant.TransparentActivity;
import sayem.picosoft.banglaassistant.helper.PageProcessHelper;

/**
 * Implementation of App Widget functionality.
 */
public class BoostWidget extends AppWidgetProvider {
    private static String WIDGET_ACTION = "kill_process";

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.boost_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Intent intent = new Intent(context, BoostWidget.class);
        intent.setAction(WIDGET_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.boost_widget);
        remoteViews.setOnClickPendingIntent(R.id.bootWidgetLayout, pendingIntent);
        ComponentName widget = new ComponentName(context, BoostWidget.class);
        appWidgetManager.updateAppWidget(widget, remoteViews);
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//
//        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(WIDGET_ACTION)) {
            context.startActivity(new Intent(context, TransparentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction("kill_process"));
        }
        super.onReceive(context, intent);
    }
}

