class AndroidConfig {
  final void Function(BroadcastType broadcastType) onBroadcast;

  const AndroidConfig({
    required this.onBroadcast,
  });
}

enum BroadcastType { timeTick, timeZoneChanged }

class BroadcastData {
  final int handler;
  final String action;

  const BroadcastData({required this.handler, required this.action});

  @override
  String toString() {
    return 'BroadcastData{handler: $handler, action: $action}';
  }

  BroadcastData.fromJson(Map<String, dynamic> json)
      : handler = json['handler'],
        action = json['action'];
}
