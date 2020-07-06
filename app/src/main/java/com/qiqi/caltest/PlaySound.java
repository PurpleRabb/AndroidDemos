package com.qiqi.caltest;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class PlaySound {
    private static SoundPool sp;
    private static Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    // 声音池初始化方法
    public static SoundPool initSoundPool(Context context) {
        if (sp == null) {
            sp= new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
            soundMap.put(1, sp.load(context, R.raw.win, 1));
            soundMap.put(2, sp.load(context, R.raw.lose, 1));
        }
        return sp;
    }

    // 播放声音方法
    public static int playSound(Context context, int sound, int number) {
        if (sp == null) {
            initSoundPool(context);
        }
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if(am.getRingerMode()!=AudioManager.RINGER_MODE_NORMAL){
            return -1;
        }
        // 返回当前AlarmManager最大音量
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 返回当前AudioManager对象的音量值
        float audioCurrentVolume = am
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolume / audioMaxVolume;
        int streamId = sp.play(soundMap.get(sound), // 播放的音乐Id
                volumnRatio, // 左声道音量
                volumnRatio, // 右声道音量
                1, // 优先级，0为最低
                number, // 循环次数，0无不循环，-1无永远循环
                1);// 回放速度，值在0.5-2.0之间，1为正常速度
        return streamId;
    }

    /**
     * 停止声音
     *
     * @param streamID
     */
    public static void pause(int streamID) {
        if (sp != null) {
            sp.pause(streamID);
        }
    }

}