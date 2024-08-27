import 'package:flutter/services.dart';

class AppStateChecker {
  static const MethodChannel _channel = MethodChannel('app_state_checker');

  static Future<bool> isAppOpen() async {
    final bool isOpen = await _channel.invokeMethod('isAppOpen');
    return isOpen;
  }

  // static Future<void> setAppState(bool isOpen) async {
  //   await _channel.invokeMethod('setAppState', {'isOpen': isOpen});
  // }
}
