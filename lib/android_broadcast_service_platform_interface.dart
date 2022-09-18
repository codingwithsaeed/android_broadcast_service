import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'android_broadcast_service_method_channel.dart';
import 'utils/configs.dart';

abstract class AndroidBroadcastServicePlatform extends PlatformInterface {
  AndroidBroadcastServicePlatform() : super(token: _token);

  static final Object _token = Object();

  static AndroidBroadcastServicePlatform _instance =
      MethodChannelAndroidBroadcastService();

  static AndroidBroadcastServicePlatform get instance => _instance;

  static set instance(AndroidBroadcastServicePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<bool> configure(AndroidConfig config);

  Future<bool> start();

  Future<bool> stop();

  Future<bool> isServiceRunning();

  Future<void> listenBroadcastStream();
}
