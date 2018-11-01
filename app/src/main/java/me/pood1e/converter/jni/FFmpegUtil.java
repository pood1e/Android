package me.pood1e.converter.jni;

public class FFmpegUtil {

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("avcodec");
        System.loadLibrary("swresample");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("avfilter");
        System.loadLibrary("avdevice");
        System.loadLibrary("ffmpeg-invoke");

    }

    private static native int run(String[] cmd);

    public static int execute(String[] commands) {
        return run(commands);
    }
}
