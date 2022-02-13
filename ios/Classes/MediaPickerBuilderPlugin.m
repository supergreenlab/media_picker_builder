#import "MediaPickerBuilderPlugin.h"
#if __has_include(<media_picker_builder/media_picker_builder-Swift.h>)
#import <media_picker_builder/media_picker_builder-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "media_picker_builder-Swift.h"
#endif

@implementation MediaPickerBuilderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMediaPickerBuilderPlugin registerWithRegistrar:registrar];
}
@end
