package com.jihf.tools;

public class Main {

    private static CmdUtils cmd = new CmdUtils();

    /**
     * 版本号
     */
    public final static int VERSION = 1;

    /**
     * Adb 端口检测
     */
    public final static int ADB_PORT = 2;

    /**
     * Adb 端口占用检测
     */
    public final static int DETECT_ADB_PORT_OCCUPY = 3;

    /**
     * 启动ADB服务
     */
    public final static int START_ADB_SERVER = 4;

    /**
     * 停止ADB服务
     */
    public final static int STOP_ADB_SERVER = 5;

    /**
     * Adb 端口占用检测
     */
    public final static int UPDATE_ADB_PORT = 6;

    /**
     * 安装apk
     */
    public final static int INSTALL_APK = 7;

    /**
     * 卸载apk
     */
    public final static int UN_INSTALL_APK = 8;

    /**
     * 导出apk
     */
    public final static int PULL_APK = 9;

    /**
     * 清除应用缓存
     */
    public final static int SHELL_CLEAR_APK = 10;

    /**
     * 已连接设备检测
     */
    public final static int DEVICES_LIST =11;

    /**
     * 环境切换 - 测试环境
     */
    public final static int HIMOVIE_ENVIRONMENT_SWITCH_TEST =12;

    /**
     * 环境切换 - 正式环境
     */
    public final static int HIMOVIE_ENVIRONMENT_SWITCH =13;

    /**
     * 签名不安装
     */
    public final static int HIMOVIE_SIGN =14;

    /**
     * 签名直接安装
     */
    public final static int HIMOVIE_SIGN_INSTALL_APK =15;

    /**
     * 模拟分辨率
     */
    public final static int ANALOG_RESOLUTION =16;

    /**
     * 恢复分辨率
     */
    public final static int RESET_RESOLUTION =17;

    /**
     * 模拟分辨率
     */
    public final static int SCREENSHOT =18;

    /**
     * 环境切换 - 测试环境
     */
    public final static int HWOVERSEA_ENVIRONMENT_SWITCH_TEST =19;

    /**
     * 环境切换 - 正式环境
     */
    public final static int HWOVERSEA_ENVIRONMENT_SWITCH =20;

    /**
     * 全量日志
     */
    public final static int LOG_CAT_ALL =21;

    /**
     * 实时日志
     */
    public final static int LOG_CAT_NOW =22;

    /**
     * 清空日志
     */
    public final static int LOG_CAT_CLEAR =23;

    /**
     * 国内日志
     */
    public final static int HIMOVIE_LOG_CAT =24;

    /**
     * 海外日志
     */
    public final static int HWOVERSEA_LOG_CAT =25;

    /**
     * 清空国内日志
     */
    public final static int HIMOVIE_LOG_CAT_CLEAR =26;

    /**
     * 清空海外日志
     */
    public final static int HWOVERSEA_LOG_CAT_CLEAR =27;

    public static void main(String[] arg) {
        if (arg.length > 0) {
            switch (Integer.valueOf(arg[0])) {
                case VERSION:
                    cmd.version();
                    break;
                case ADB_PORT:
                    cmd.detectAdbPort();
                    break;
                case DETECT_ADB_PORT_OCCUPY:
                    cmd.detectAdbPortOccupy();
                    break;
                case START_ADB_SERVER:
                    cmd.startAdbServer();
                    break;
                case STOP_ADB_SERVER:
                    cmd.stopAdbServer();
                    break;
                case INSTALL_APK:
                    cmd.installApk(arg[1],false);
                    break;
                case UN_INSTALL_APK:
                    cmd.unInstallApk(arg[1]);;
                    break;
                case PULL_APK:
                    cmd.pullApk(arg[1],arg[2]);
                    break;
                case SHELL_CLEAR_APK:
                    cmd.shellClearApk(arg[1]);
                    break;
                case DEVICES_LIST:
                    cmd.devicesList();
                    break;
                case HIMOVIE_ENVIRONMENT_SWITCH_TEST:
                    cmd.himovie_environmentSwitch(arg[1],0);
                    break;
                case HIMOVIE_ENVIRONMENT_SWITCH:
                    cmd.himovie_environmentSwitch(arg[1],1);
                    break;
                case HIMOVIE_SIGN:
                    cmd.signApk(arg[1],arg[2],0,false);
                    break;
                case HIMOVIE_SIGN_INSTALL_APK:
                    cmd.signApk(arg[1],arg[2],1,false);
                    break;
                case ANALOG_RESOLUTION:
                    cmd.analogResolution(arg[1]);
                    break;
                case RESET_RESOLUTION:
                    cmd.resetResolution();
                    break;
                case SCREENSHOT:
                    cmd.screenshot(arg[1]);
                    break;
                case HWOVERSEA_ENVIRONMENT_SWITCH_TEST:
                    cmd.hwoversea_environmentSwitch(arg[1],0);
                    break;
                case HWOVERSEA_ENVIRONMENT_SWITCH:
                    cmd.hwoversea_environmentSwitch(arg[1],1);
                    break;
                case LOG_CAT_ALL:
                    cmd.logcatAll(arg[1]);
                    break;
                case LOG_CAT_NOW:
                    cmd.logcatNow(arg[1]);
                    break;
                case LOG_CAT_CLEAR:
                    cmd.logcatClear();
                    break;
                case HIMOVIE_LOG_CAT:
                    cmd.logcatHuawei(arg[1],0);
                    break;
                case HWOVERSEA_LOG_CAT:
                    cmd.logcatHuawei(arg[1],1);
                    break;
                case HIMOVIE_LOG_CAT_CLEAR:
                    cmd.logcatClearHuawei(0);
                    break;
                case HWOVERSEA_LOG_CAT_CLEAR:
                    cmd.logcatClearHuawei(1);
                    break;
                default:
                    break;
            }
        }
    }
}