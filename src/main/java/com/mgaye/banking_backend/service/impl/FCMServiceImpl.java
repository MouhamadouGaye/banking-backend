// package com.mgaye.banking_backend.service.impl;

// @Service
// @RequiredArgsConstructor
// public class FCMServiceImpl implements FCMService {
// private final FirebaseMessaging firebaseMessaging;

// @Override
// public void sendPushNotification(PushNotificationRequest request) {
// try {
// Message message = Message.builder()
// .setToken(request.getDeviceToken())
// .setNotification(Notification.builder()
// .setTitle(request.getTitle())
// .setBody(request.getMessage())
// .build())
// .build();

// firebaseMessaging.send(message);
// } catch (FirebaseMessagingException e) {
// throw new PushNotificationException("Failed to send FCM notification", e);
// }
// }
// }