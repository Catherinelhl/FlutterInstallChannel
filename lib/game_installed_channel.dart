
import 'dart:io';

import 'package:flutter/services.dart';

///Flutter 与Android 交互游戏APP安装channel

class InstallGameStateChannel{
  static const MethodChannel _channel =
  const MethodChannel("gameInstalledChannel");

  /// Check if an app is available with the given [uri] scheme.
  ///
  /// Returns a [Map<String, String>] containing info about the App or throws a [PlatformException]
  /// if the app isn't found.
  ///
  /// The returned [Map] has a form like this:
  /// ```dart
  /// {
  ///   "app_name": "",
  ///   "package_name": "",
  ///   "versionCode": "",
  ///   "version_name": ""
  /// }
  static Future<Map<String, String>> checkAvailability(String uri) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent('uri', () => uri);

    if (Platform.isAndroid) {
      Map<dynamic, dynamic> app = await _channel.invokeMethod("1", args);
      return {
        "app_name": app["app_name"],
        "package_name": app["package_name"],
        "versionCode": app["versionCode"],
        "version_name": app["version_name"]
      };
    }
    else if (Platform.isIOS) {
      bool appAvailable = await _channel.invokeMethod("1", args);
      if (!appAvailable) {
        throw PlatformException(code: "", message: "App not found $uri");
      }
      return {
        "app_name": "",
        "package_name": uri,
        "versionCode": "",
        "version_name": ""
      };
    }

    return null;
  }

  /// Only for **Android**.
  ///
  /// Get the list of all installed apps, where
  /// each app has a form like [checkAvailability()].
  static Future<List<Map<String, String>>> getInstalledApps() async {
    List<dynamic> apps = await _channel.invokeMethod("2");
    if (apps != null && apps is List) {
      List<Map<String, String>> list = new List();
      for (var app in apps) {
        if (app is Map) {
          list.add({
            "app_name": app["app_name"],
            "package_name": app["package_name"],
            "versionCode": app["versionCode"],
            "version_name": app["version_name"]
          });
        }
      }

      return list;
    }
    return new List(0);
  }


}