import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'android_broadcast_service_method_channel.dart';

abstract class AndroidBroadcastServicePlatform extends PlatformInterface {
  /// Constructs a AndroidBroadcastServicePlatform.
  AndroidBroadcastServicePlatform() : super(token: _token);

  static final Object _token = Object();

  static AndroidBroadcastServicePlatform _instance =
      MethodChannelAndroidBroadcastService();

  /// The default instance of [AndroidBroadcastServicePlatform] to use.
  ///
  /// Defaults to [MethodChannelAndroidBroadcastService].
  static AndroidBroadcastServicePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AndroidBroadcastServicePlatform] when
  /// they register themselves.
  static set instance(AndroidBroadcastServicePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
