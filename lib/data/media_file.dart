class MediaFile {
  /// Unique identifier for the file
  String id;

  /// Date added in seconds (unix timestamp)
  int dateAdded;

  /// Original file path
  String path;

  /// Thumbnails from android (NOT iOS) need to have their orientation fixed
  /// based on the returned [orientation]
  /// usage: RotatedBox(
  ///                  quarterTurns: Platform.isIOS
  ///                      ? 0
  ///                      : orientationToQuarterTurns(mediaFile.orientation),
  ///                  child: Image.file(
  ///                    File(mediaFile.thumbnailPath),
  ///                    fit: BoxFit.cover,
  ///                    )
  /// Note: If thumbnail returned is null you will have to call [MediaPickerBuilder.getThumbnail]
  String? thumbnailPath;

  /// Orientation in degrees (i.e. 0, 90, 180, 270)
  int orientation;

  /// Video duration in milliseconds
  int? duration;

  /// Supported on Android only
  String? mimeType;

  /// On iOS, original file name is retrieved only if [loadIOSPath] is true
  String? fileName;

  MediaType type;

  MediaFile(
      {required this.id,
      required this.dateAdded,
      required this.path,
      this.thumbnailPath,
      required this.orientation,
      required this.type});

  MediaFile.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        dateAdded = json['dateAdded'],
        path = json['path'],
        thumbnailPath = json['thumbnailPath'],
        orientation = json['orientation'],
        duration = json['duration'],
        mimeType = json['mimeType'],
        fileName = json['fileName'],
        type = MediaType.values[json['type']];

  @override
  bool operator ==(Object other) =>
      identical(this, other) || other is MediaFile && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}

enum MediaType { image, video }
