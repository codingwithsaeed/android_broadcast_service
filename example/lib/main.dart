import 'package:android_broadcast_service/android_broadcast_service.dart';
import 'package:android_broadcast_service/utils/configs.dart';
import 'package:flutter/material.dart';

final plugin = AndroidBroadcastService();

void main() {
  runApp(const MyApp());
}

void onBroadcast(BroadcastType broadcastType) {
  print('onBroadcast in app: $broadcastType');
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              const Text('Plugin Example'),
              const SizedBox(height: 20),
              ElevatedButton(
                  onPressed: () {
                    plugin.configure(
                        const AndroidConfig(onBroadcast: onBroadcast));
                    plugin.start();
                  },
                  child: const Text('Start')),
            ],
          ),
        ),
      ),
    );
  }
}
