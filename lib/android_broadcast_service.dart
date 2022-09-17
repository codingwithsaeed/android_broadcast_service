import 'android_broadcast_service_platform_interface.dart';

class AndroidBroadcastService {
  Future<String?> getPlatformVersion() {
    return AndroidBroadcastServicePlatform.instance.getPlatformVersion();
  }
}
