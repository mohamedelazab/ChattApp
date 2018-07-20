package com.example.mohamed.chattapp.notification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.activities.ChatActivity;
import com.example.mohamed.chattapp.model.Message;
import com.example.mohamed.chattapp.utils.Constants;
import com.example.mohamed.chattapp.utils.Session;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0){
            Log.e("MESSAGE_FROM", remoteMessage.getFrom());
            Log.e("MESSAGE_CONTENT", remoteMessage.getData().get("message"));
            String messageContent =remoteMessage.getData().get("message");
            String roomId =remoteMessage.getData().get("room_id");
            String userId = remoteMessage.getData().get("user_id");
            String username =remoteMessage.getData().get("username");
            String messageType =remoteMessage.getData().get("type");
            String timeStamp =remoteMessage.getData().get("timestamp");
            Message message =new Message();
            message.setRoomId(roomId);
            message.setUserId(userId);
            message.setUserName(username);
            message.setType(messageType);
            message.setTimestamp(timeStamp);
            message.setContent(messageContent);

            if (!(Session.newInstance().getUser().getId() ==Integer.valueOf(userId))) {
                if (isAppInBackground(this)) {
                    sendNotification(message);
                } else {
                    Intent intent = new Intent(Constants.UPDATE_CHAT_ACTIVITY_BROADCAST);
                    intent.putExtra(Constants.MESSAGE_PASS_INTENT, message);
                    sendBroadcast(intent);
                }
            }
        }
    }

    private boolean isAppInBackground(Context context){
        boolean isInBackground =true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos =
                activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos){
            if (processInfo.importance ==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                for (String activeProcess : processInfo.pkgList){
                    if (activeProcess.equals(context.getPackageName())){
                        isInBackground =false;
                    }
                }
            }
        }
        return isInBackground;
    }

    private void sendNotification(Message message){
        Intent intent =new Intent(this, ChatActivity.class);
        intent.putExtra("msg", message);
        intent.putExtra("room_id", Integer.parseInt(message.getRoomId()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bg)
                .setContentTitle("New Message")
                .setContentText(message.getContent())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

}