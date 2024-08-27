package com.example.app_state_checker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class AppStateCheckerPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private lateinit var sharedPreferences: SharedPreferences

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "app_state_checker")
    channel.setMethodCallHandler(this)
    sharedPreferences = flutterPluginBinding.applicationContext.getSharedPreferences("AppStatePrefs", Context.MODE_PRIVATE)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "isAppOpen" -> {
        val isAppOpen = sharedPreferences.getBoolean("isAppOpen", false)
        result.success(isAppOpen)
      }
      // "setAppState" -> {
      //   val isOpen = call.argument<Boolean>("isOpen") ?: false
      //   sharedPreferences.edit().putBoolean("isAppOpen", isOpen).apply()
      //   result.success(null)
      // }
      else -> result.notImplemented()
    }
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    setAppState(true)  // App foi aberto
  }

  override fun onDetachedFromActivityForConfigChanges() {
    setAppState(false)  // App foi fechado
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    setAppState(true)  // App foi reaberto
  }

  override fun onDetachedFromActivity() {
    setAppState(false)  // App foi fechado
  }

  private fun setAppState(isOpen: Boolean) {
    sharedPreferences.edit().putBoolean("isAppOpen", isOpen).apply()
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
