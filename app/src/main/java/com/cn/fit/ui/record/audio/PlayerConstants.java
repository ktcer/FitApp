package com.cn.fit.ui.record.audio;

public class PlayerConstants {
    public static final int STATUS_INVALID = 100;

    public static final int STATUS_IDLE = STATUS_INVALID + 1;

    public static final int STATUS_INITIALIZED = STATUS_IDLE + 1;

    public static final int STATUS_PREPARED = STATUS_INITIALIZED + 1;

    public static final int STATUS_STARTED = STATUS_PREPARED + 1;

    public static final int STATUS_PAUSED = STATUS_STARTED + 1;

    public static final int STATUS_PLACKBACK_COMPLETED = STATUS_PAUSED + 1;

    public static final int STATUS_STOPPED = STATUS_PLACKBACK_COMPLETED + 1;

    public static final int STATUS_ERROR = STATUS_STOPPED + 1;
}
