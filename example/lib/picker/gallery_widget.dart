import 'package:flutter/material.dart';
import 'package:media_picker_builder/data/media_file.dart';

import 'gallery_widget_item.dart';

class GalleryWidget extends StatefulWidget {
  final List<MediaFile> mediaFiles;

  const GalleryWidget({Key? key, required this.mediaFiles}) : super(key: key);

  @override
  State<StatefulWidget> createState() => GalleryWidgetState();
}

class GalleryWidgetState extends State<GalleryWidget> {
  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: widget.mediaFiles.isEmpty
          ? const Center(child: Text("Empty Folder"))
          : GridView.builder(
              padding: const EdgeInsets.all(0),
              itemCount: widget.mediaFiles.length,
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 4,
                mainAxisSpacing: 2,
                crossAxisSpacing: 2,
              ),
              itemBuilder: (BuildContext context, int index) {
                return GalleryWidgetItem(mediaFile: widget.mediaFiles[index]);
              }),
    );
  }
}
