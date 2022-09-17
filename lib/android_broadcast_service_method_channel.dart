import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'android_broadcast_service_platform_interface.dart';

/// An implementation of [AndroidBroadcastServicePlatform] that uses method channels.
class MethodChannelAndroidBroadcastService
    extends AndroidBroadcastServicePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('android_broadcast_service');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
