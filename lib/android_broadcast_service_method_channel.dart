import 'dart:convert';
import 'dart:ui';

import 'package:android_broadcast_service/utils/configs.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'android_broadcast_service_platform_interface.dart';

/// An implementation of [AndroidBroadcastServicePlatform] that uses method channels.
class MethodChannelAndroidBroadcastService
    extends AndroidBroadcastServicePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel =
      const MethodChannel('android_broadcast_service', JSONMethodCodec());

  @visibleForTesting
  final eventChannel =
      const EventChannel('android_broadcast_service_event', JSONMethodCodec());

  @override
  Future<void> listenBroadcastStream() async {
    eventChannel.receiveBroadcastStream().listen((event) {
      print('event: $event');
      final data = BroadcastData.fromJson(jsonDecode(event));
      final handlerLong = data.handler;
      final broadcastHandle = CallbackHandle.fromRawHandle(handlerLong);
      final onBroadcast =
          PluginUtilities.getCallbackFromHandle(broadcastHandle);
      print('onBroadcast: $onBroadcast');
      if (onBroadcast != null) {
        final broadcastType = BroadcastType.values
            .firstWhere((element) => element.name == data.action);
        onBroadcast(broadcastType);
      }
    });
  }

  @override
  Future<bool> configure(AndroidConfig config) async {
    final onBroadcastHandle =
        PluginUtilities.getCallbackHandle(config.onBroadcast);
    if (onBroadcastHandle == null) {
      throw 'Unable to get callback handle';
    }
    final result = await methodChannel.invokeMethod<bool>(
      'configure',
      {
        'on_broadcast': onBroadcastHandle.toRawHandle(),
      },
    );
    return result ?? false;
  }

  @override
  Future<bool> isServiceRunning() async {
    final result = await methodChannel.invokeMethod<bool>('isRunning', {});
    return result ?? false;
  }

  @override
  Future<bool> start() async {
    final result = await methodChannel.invokeMethod<bool>('start', {});
    listenBroadcastStream();
    return result ?? false;
  }

  @override
  Future<bool> stop() async {
    final result = await methodChannel.invokeMethod<bool>('stop', {});
    return result ?? false;
  }
}
