package me.pood1e.converter;

import me.pood1e.converter.jni.FFmpegUtil;

public class TransferUtil {
    public static void convert(String src, String dst, ShowInMain s) {
        new Thread(() -> {
            String cmd = "ffmpeg -i %s -vn -acodec libmp3lame -ab 64000 -ac 1 -ar 22050 -f mp3 -y %s";
            cmd = String.format(cmd, src, dst);
            FFmpegUtil.execute(cmd.split(" "));
            if (s != null) {
                s.show(dst);
            }
        }).start();
    }
}
