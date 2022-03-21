package com.endless.video.core.bean.mirror;

/**
 * @ClassName MirrorSerializeVideoSize
 * @Description 视频信息
 * @Author haosiyuan
 * @Date 2022/2/18 13:53
 * @Version 1.0
 */
public class MirrorSerializeVideoSize {

    /// 宽
    private double x;

    /// 高
    private double y;

    /// 视频长度
    private double duration;

    /// 视频大小
    /// TODO 视频大小应该在 [MirrorSerializeVideoInfo] 中包含
    private double size;

    /// 格式化视频大小
    private void humanSize() {}

    /// 格式化视频时间
    private void humanDuration() {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}
