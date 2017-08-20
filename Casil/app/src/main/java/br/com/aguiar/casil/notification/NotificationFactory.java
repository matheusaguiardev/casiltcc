package br.com.aguiar.casil.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import br.com.aguiar.casil.R;

public class NotificationFactory<T> {

    public void generateNotification(Context context, String titulo, String message, Class<T> classe) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, classe);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = buildNotification((Activity) context, intent, titulo, message);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }


    private Notification buildNotification(Activity activity, PendingIntent pendingIntent, String titulo, String msg) {
        Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(titulo)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(msg)
                .setContentIntent(pendingIntent);

        return builder.getNotification();
    }

}
