import 'package:android_broadcast_service/utils/configs.dart';

import 'android_broadcast_service_platform_interface.dart';

class AndroidBroadcastService {
  AndroidBroadcastServicePlatform get _instance =>
      AndroidBroadcastServicePlatform.instance;

  Future<bool> configure(AndroidConfig config) async =>
      await _instance.configure(config);

  Future<bool> start() async {
    if (await isServiceRunning()) return false;
    return await _instance.start();
  }

  Future<bool> stop() async {
    if (!await isServiceRunning()) return false;
    return await _instance.stop();
  }

  Future<bool> isServiceRunning() async => await _instance.isServiceRunning();
}
