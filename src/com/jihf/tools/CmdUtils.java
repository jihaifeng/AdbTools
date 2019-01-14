package com.jihf.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CmdUtils {

    /**
     * 執行
     *
     * @param cmd
     * @return
     */
    public String exeCmd(String cmd) {
        StreamCaptureThread errorStream = null;
        StreamCaptureThread outputStream = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);

            errorStream = new StreamCaptureThread(p.getErrorStream());
            outputStream = new StreamCaptureThread(p.getInputStream());

            new Thread(errorStream).start();
            new Thread(outputStream).start();
            p.getOutputStream().close();
            p.waitFor();

            return outputStream.output.toString() + errorStream.output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream.output.toString();
        }
    }

    /**
     * 版本号
     */
    public void version() {
        Log.i(exeCmd("adb version"));
        Log.i(exeCmd("java -version"));
        Log.i("Adb Tools version 1.0.3");
    }

    /**
     * 检测adb端口号
     */
    public String detectAdbPort() {
        // error: could not install *smartsocket* listener: cannot bind to
        // 127.0.0.1:30123: 通常每个套接字地址(协议/网络地址/端口) 只允许使用一次。 (10048)
        Log.i("执行ADB 端口检测...");
        String[] input = exeCmd("adb nodaemon server").split(" ");
        String port = (input[9].split(":"))[1];
        Log.i("Port " + port);
        return port;
    }

    /**
     * 检测adb端口号占用
     */
    public void detectAdbPortOccupy() {
        String port = detectAdbPort();
        Log.i("执行ADB 端口占用检测...");
        Log.i(exeCmd("cmd.exe /k netstat -ano | findstr " + port));

    }

    /**
     * 启动adb服务
     */
    public void startAdbServer() {
        Log.i("执行ADB 服务启动!");
        String outPut = exeCmd("adb start-server");
        if ("".equals(outPut) || outPut == null) {
            Log.i("检测到 ADB 服务 已启动，无需重复操作!");
        } else {
            Log.i("ADB 服务启动成功!");
        }
    }

    /**
     * 停止adb服务
     */
    public void stopAdbServer() {
        Log.i("执行ADB 服务停止...");
        String outPut = exeCmd("adb kill-server");
        if ("".equals(outPut) || outPut == null) {
            Log.i("ADB 服务已停止!");
        } else {
            Log.i("检测到 ADB 服务 已停止，无需重复操作!");
        }
    }

    private int installNum = 0;
    private String[] install = {" -r", " -t", " -d", " -g"};

    /**
     * 安裝APK
     */
    public void installApk(String apkPath, boolean auto) {

        if (auto) {
            installNum++;
        } else {
            installNum = 0;
        }

        Log.i("执行APK 安装...");
        String str = "adb install";
        for (int i = 0; i < 4 - installNum; i++) {
            str += install[i];
        }

        Log.i(str);
        Log.i(apkPath);
        str += (" " + apkPath);
        String outPut = exeCmd(str);

        if (outPut.indexOf("adb: failed to install") >= 0 && installNum != 2) {
            Log.i("APK 安装失败，尝试重新安装 ");
            installApk(apkPath, true);
            return;
        }
        Log.i("APK 安装 " + outPut);
    }

    /**
     * 卸載APK
     *
     * @param packname
     */
    public void unInstallApk(String packname) {
        Log.i("执行APK 卸载...");
        Log.i(packname);
        String outPut = exeCmd("adb uninstall " + packname);
        Log.i("APK 卸载 " + outPut);
    }

    /**
     * 導出APK
     */
    public void pullApk(String packname, String outPath) {
        Log.i("执行APK 导出...");
        Log.i("检测" + packname + "路径");
        String outPut = exeCmd("adb shell pm path " + packname);
        // 获取apk路径 package:/data/app/com.huawei.himovie-1/base.apk
        outPut = outPut.replace("package:", "");
        Log.i(outPut);
        outPut = exeCmd("adb pull " + outPut + " " + outPath + "out_put/apk");
        Log.i(outPut);
    }

    /**
     * 清除应用缓存
     *
     * @param packname
     */
    public void shellClearApk(String packname) {
        Log.i("执行清除应用缓存...");
        String outPut = exeCmd("adb shell pm clear " + packname);
        Log.i("清除应用缓存 " + outPut);
    }

    /**
     * 已连接设备检测
     */
    public void devicesList() {
        Log.i("执行已连接设备检测...");
        Log.i(exeCmd("adb devices"));
    }

    /**
     * himovie 环境切换
     *
     * @param path
     * @param eSwitch 0 测试环境 1 正式环境
     */
    public void himovie_environmentSwitch(String path, int eSwitch) {

        if (eSwitch == 0) {
            Log.i("执行国内测试环境切换...");
            unInstallApk("com.huawei.hwid");
            Log.i(exeCmd("adb shell am force-stop com.huawei.himovie"));
            Log.i(exeCmd("adb shell pm clear com.huawei.himovie"));
            installApk(path + "HMS\\HMS_test_release.apk", false);
            Log.i(exeCmd("adb push " + path
                    + "Himovie_TestSwitch\\himovie.properties /sdcard/Android/data/com.huawei.himovie/files/himovie.properties"));

            Log.i(exeCmd("adb shell am force-stop com.huawei.hwid"));
            Log.i(exeCmd("adb shell pm clear com.huawei.hwid"));
            Log.i(exeCmd("adb push " + path
                    + "Himovie_TestSwitch\\hwid.properties /sdcard/Android/data/com.huawei.himovie/files/hwid.properties"));
        } else {
            Log.i("执行国内现网环境切换...");
            unInstallApk("com.huawei.hwid");
            Log.i(exeCmd("adb shell am force-stop com.huawei.himovie"));
            Log.i(exeCmd("adb shell pm clear com.huawei.himovie"));

            Log.i(exeCmd("adb shell am force-stop com.huawei.hwid"));
            Log.i(exeCmd("adb shell pm clear com.huawei.hwid"));
            installApk(path + "HMS\\HMS.apk", false);

        }
    }

    /**
     * hwoversea 环境切换
     *
     * @param path
     * @param eSwitch 0 测试环境 1 正式环境
     */
    public void hwoversea_environmentSwitch(String path, int eSwitch) {

        if (eSwitch == 0) {
            Log.i("执行海外测试环境切换...");
            unInstallApk("com.huawei.hwid");
            Log.i(exeCmd("adb shell am force-stop com.huawei.himovie.overseas"));
            Log.i(exeCmd("adb shell pm clear com.huawei.himovie.overseas"));
            installApk(path + "HMS\\HMS_test_release.apk", false);
            Log.i(exeCmd("adb push " + path
                    + "Hwoversea_TestSwitch\\env_mirror.properties /sdcard/Android/data/com.huawei.himovie.overseas/files/himovie.overseas.properties"));

            Log.i(exeCmd("adb shell am force-stop com.huawei.hwid"));
            Log.i(exeCmd("adb shell pm clear com.huawei.hwid"));
        } else {
            Log.i("执行海外现网环境切换...");
            unInstallApk("com.huawei.hwid");
            installApk(path + "HMS\\HMS.apk", false);
            Log.i(exeCmd("adb shell am force-stop com.huawei.himovie.overseas"));
            Log.i(exeCmd("adb shell pm clear com.huawei.himovie.overseas"));
            Log.i(exeCmd("adb push " + path
                    + "Hwoversea_TestSwitch\\env_de.properties /sdcard/Android/data/com.huawei.himovie.overseas/files/himovie.overseas.properties"));

            Log.i(exeCmd("adb shell am force-stop com.huawei.hwid"));
            Log.i(exeCmd("adb shell pm clear com.huawei.hwid"));

        }
    }

    private int signNum = 0;

    /**
     * himovie 签名
     *
     * @param eSwitch 0 不自动安装 1自动安装
     */
    public void signApk(String path, String apkPath, int eSwitch, boolean auto) {
        if (auto) {
            if (signNum == 1) {
                return;
            }
            Log.i("尝试再次签名...");
            signNum++;
        } else {
            signNum = 0;
        }

        Log.i("执行签名...");
        String outPut = exeCmd("signclient -a \"172.100.22.64:8090; 10.21.152.156:8090\" -f " + apkPath);
        Log.i(outPut);

        if (outPut.indexOf("signed 1 files succeed!") >= 0) {
            if (eSwitch == 1) {
                Log.i("签名完成！");
                apkPath = apkPath.replace(".apk", "-signed.apk");
                installApk(apkPath, false);
            }
        } else {
            Log.i("签名失败!");
            signApk(path, apkPath, eSwitch, true);
        }
    }

    /**
     * 模拟分辨率
     *
     * @param Resolution
     */
    public void analogResolution(String Resolution) {
        Log.i("执行模拟分辨率...");
        Log.i(Resolution);
        if ("".endsWith(Resolution) || Resolution == null) {
            Log.i("NULL");
            return;
        }
        Resolution = Resolution.replace("*", "x");
        Log.i(exeCmd("adb shell wm size " + Resolution));
    }

    /**
     * 恢复分辨率
     */
    public void resetResolution() {
        Log.i("执行恢复分辨率...");
        Log.i(exeCmd("adb shell wm size reset"));
    }

    /**
     * 截图
     */
    public void screenshot(String path) {
        Log.i("执行截图...");
        String time = System.currentTimeMillis() + "";
        Log.i(exeCmd("adb shell /system/bin/screencap -p /sdcard/" + time + ".png"));
        Log.i("导出截图...");
        Log.i(exeCmd("adb pull /sdcard/" + time + ".png " + path + "outputs\\png\\" + time + ".png"));
        Log.i("删除设备临时文件...");
        Log.i(exeCmd("adb shell rm /sdcard/" + time + ".png"));
        Log.i("截图输出路径：" + path + "/out_put/png/" + time + ".png");
    }

    /**
     * 全量日志
     *
     * @param path
     */
    public void logcatAll(String path) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = df.format(new Date());
        File file = new File(path + "outputs\\log\\" + time);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.i("导出ANR LOG...");
        Log.i("adb pull /data/anr " + path + "outputs\\log\\" + time + "\\anr\\");
        Log.i(exeCmd("adb pull /data/anr " + path + "outputs\\log\\" + time + "\\anr\\"));
        Log.i("导出Android LOG...");
        Log.i(exeCmd("adb pull /data/log/android_logs " + path + "outputs\\log\\" + time + "\\android_logs\\"));
    }

    /**
     * 实时日志
     *
     * @param path
     */
    public void logcatNow(String path) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = df.format(new Date());
        File file = new File(path + "outputs\\log\\" + time);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.i("adb logcat -v time > " + path + "outputs\\log\\" + time + "\\logcat.log");
        Log.i(exeCmd("adb logcat -v time > " + path + "outputs\\log\\" + time + "\\logcat.log"));
    }

    /**
     * 清空全量
     *
     * @param path
     */
    public void logcatClear() {
        Log.i("清空全量日志...");
        Log.i(exeCmd("adb shell rm /data/log/android_logs"));
    }

    /**
     * 导出华为视频日志
     *
     * @param path
     * @param type
     */
    public void logcatHuawei(String path, int type) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = df.format(new Date());
        File file = new File(path + "outputs\\log\\" + time);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (type == 0) {
            Log.i("导出com.huawei.himovie LOG...");
            Log.i(exeCmd("adb pull /data/data/com.huawei.himovie/files/log " + path + "outputs\\log\\" + time));
        } else {
            Log.i("导出com.huawei.himovie.overseas LOG...");
            Log.i(exeCmd("adb pull /data/data//com.huawei.himovie.overseas/files/log " + path
                    + "outputs\\log\\" + time));
        }
    }

    /**
     * 清空华为视频日志
     *
     * @param path
     * @param type
     */
    public void logcatClearHuawei(int type) {
        if (type == 0) {
            Log.i("清空com.huawei.himovie LOG...");
            Log.i(exeCmd("adb shell rm /data/data/com.huawei.himovie/files/log"));
        } else {
            Log.i("清空com.huawei.himovie.overseas  LOG...");
            Log.i(exeCmd("adb shell rm /data/data/com.huawei.himovie.overseas/files/log"));
        }
    }
}
