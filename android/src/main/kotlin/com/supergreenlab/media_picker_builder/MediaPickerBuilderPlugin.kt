package com.supergreenlab.media_picker_builder

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MediaPickerBuilderPlugin() : FlutterPlugin, MethodCallHandler {

    private lateinit var context: Context
    private lateinit var channel : MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "media_picker_builder")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    private val mainHandler by lazy { Handler(context.mainLooper) }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "getAlbums" -> {
                val withImages = call.argument<Boolean>("withImages")
                val withVideos = call.argument<Boolean>("withVideos")
                if (withImages == null || withVideos == null) {
                    result.error("INVALID_ARGUMENTS", "withImages or withVideos must not be null", null)
                    return
                }
                val albums = FileFetcher.getAlbums(context, withImages, withVideos)
                result.success(albums.toString())
            }
            call.method == "getThumbnail" -> {
                val fileId = call.argument<String>("fileId")
                val type = call.argument<Int>("type")
                if (fileId == null || type == null) {
                    result.error("INVALID_ARGUMENTS", "fileId or type must not be null", null)
                    return
                }
                executor.execute {
                    try {
                        val thumbnail = FileFetcher.getThumbnail(
                                context,
                                fileId.toLong(),
                                MediaFile.MediaType.values()[type]
                        )
                        mainHandler.post {
                            if (thumbnail != null)
                                result.success(thumbnail)
                            else
                                result.error("NOT_FOUND", "Unable to get the thumbnail", null)
                        }
                    } catch (e: Exception) {
                        Log.e("MediaPickerBuilder", e.message.toString())
                        mainHandler.post {
                            result.error("GENERATE_THUMBNAIL_FAILED", "Unable to generate thumbnail ${e.message}", null)
                        }
                    }
                }
            }
            call.method == "getMediaFile" -> {
                val fileIdString = call.argument<String>("fileId")
                val type = call.argument<Int>("type")
                val loadThumbnail = call.argument<Boolean>("loadThumbnail")
                if (fileIdString == null || type == null || loadThumbnail == null) {
                    result.error("INVALID_ARGUMENTS", "fileId, type or loadThumbnail must not be null", null)
                    return
                }

                val fileId = fileIdString.toLongOrNull()
                if (fileId == null) {
                    result.error("NOT_FOUND", "Unable to find the file", null)
                    return
                }

                executor.execute {
                    try {
                        val mediaFile = FileFetcher.getMediaFile(
                                context,
                                fileId,
                                MediaFile.MediaType.values()[type],
                                loadThumbnail)
                        mainHandler.post {
                            if (mediaFile != null)
                                result.success(mediaFile.toJSONObject().toString())
                            else
                                result.error("NOT_FOUND", "Unable to find the file", null)
                        }
                    } catch (e: Exception) {
                        Log.e("MediaPickerBuilder", e.message.toString())
                        mainHandler.post {
                            result.error("GENERATE_THUMBNAIL_FAILED", "Unable to generate thumbnail ${e.message}", null)
                        }
                    }
                }
            }
            else -> result.notImplemented()
        }
    }
}
