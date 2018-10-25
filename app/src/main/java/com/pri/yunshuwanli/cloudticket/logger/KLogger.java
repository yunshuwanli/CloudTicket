package com.pri.yunshuwanli.cloudticket.logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

public class KLogger implements Thread.UncaughtExceptionHandler {

    private static String TAG = "LogToFile";

    private static String logPath = null;//log日志存放路径
    private static String logZPath = null;//log压缩日志存放路径

    private static SimpleDateFormat dateFormat_date = new SimpleDateFormat("yyyy-MM-dd");//日期格式;
    private static SimpleDateFormat dateFormat_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> infos = new HashMap<>();


    private static KLogger instance = new KLogger();

    private KLogger() {
    }

    public static KLogger getInstance() {
        return instance;
    }

    /**
     * 初始化，须在使用之前设置，最好在Application创建时调用
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        getFilePath(mContext);
        getZipFilePath(mContext);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
//        autoClear(1);
    }

    public static String getFilePath(Context context) {
        logPath = getRootPath(context) + File.separator + "Logs" + File.separator;//获得logs文件储存路径
        return logPath;
    }

    public static String getZipFilePath(Context context) {
        logZPath = getRootPath(context) + File.separator + "Logz" + File.separator;//获得临时压缩文件储存路径
        return logZPath;
    }

    /**
     * 获得文件存储路径
     *
     * @return
     */
    private static String getRootPath(Context context) {
        if (Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            return context.getExternalFilesDir(null).getPath();//获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.../files/Logs/log_2016-03-14_16-15-09.log
        } else {
            return context.getFilesDir().getPath();//直接存在/data/data里，非root手机是看不到的
        }
    }


    private static final char VERBOSE = 'v';

    private static final char DEBUG = 'd';

    private static final char INFO = 'i';

    private static final char WARN = 'w';

    private static final char ERROR = 'e';

    public static void v(String tag, String msg) {
        L.v(tag,msg);
        writeToFile(VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        L.d(tag,msg);
        writeToFile(DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        L.i(tag,msg);
        writeToFile(INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        L.w(tag,msg);
        writeToFile(WARN, tag, msg);
    }

    public static void e(String tag, String msg) {
        L.e(tag,msg);
        writeToFile(ERROR, tag, msg);
    }

    /**
     * 将log信息写入文件中
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void writeToFile(char type, String tag, String msg) {

        if (null == logPath) {
            L.e(TAG, "logPath == null ，未初始化LogToFile");
            return;
        }

        String time = dateFormat_date.format(new Date());
        String fileName = logPath + time + ".log";
        String log = dateFormat_time.format(new Date()) + " " + type + " " + tag + " " + msg + "\n";//log日志内容，可以自行定制

        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdirs();//创建父路径
        }

        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {

            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(log);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public String getCurrNeedWriteFileName() {
        String time = dateFormat_date.format(new Date());
        return time + ".log";
    }

    public String getOtherDay(String time) {
        return time + ".log";
    }

    private String writeFile(String sb) throws Exception {
        String fileName = getCurrNeedWriteFileName();
        if (hasSDCard()) {
            String path =logPath;
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }



    /**
     *
     * @param src 源日志
     * @param needCurrTIme  是否需要当前的日志上传 false表示压缩除今天外的所有日志
     * @return
     */
    public String getZipFile(String src,boolean needCurrTIme) {
        //定义压缩输出流
        ZipOutputStream out = null;
        String destRoot = logZPath;
        File destfile = new File(destRoot);
        if (!destfile.exists()) {
            destfile.mkdirs();
        }

        String dest = null;
        if(needCurrTIme){
            dest = logZPath+dateFormat_date.format(new Date())+".zip"; //今天的日期命名
        }else {
            dest = logZPath + DateUtil.getDate(1, dateFormat_date) + ".zip"; //昨天的日期命名
        }
        try {
            //传入源文件
            File outFile = new File(dest);
            File fileOrDirectory = new File(src);
            //传入压缩输出流
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //判断是否是一个文件或目录
            //如果是文件则压缩
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                File[] entries;
                if(needCurrTIme){
                    entries = fileOrDirectory.listFiles();
                }else {
                    //否则列出目录中的所有文件递归进行压缩   如存活一天，即今天以前的文件压缩
                    entries = fileOrDirectory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            String s = name.split("\\.")[0];
                            String date = dateFormat_date.format(new Date());
                            return s.compareTo(date) < 0;
                        }
                    });
                }

                for (int i = 0; i < entries.length; i++) {
                    zipFileOrDirectory(out, entries[i], "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return dest;
    }

    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath) {
        FileInputStream in = null;
        try {
            //判断目录是否为null
            if (!fileOrDirectory.isDirectory()) {
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                //归档压缩目录
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                //将压缩目录写到输出流中
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                //列出目录中需要上传的的所有文件
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    //递归压缩
                    zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //判断是否有前一天的日志存在，有可以自动上传
    public boolean needAutoUploadFile() {
        String filePath = logPath;
        File file = new File(filePath);
        String[] filePaths = null;
        if (!file.exists()) {
//            ToastUtil.showToast("日志目录不存在");
            return false;
        }
        filePaths = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String s = name.split("\\.")[0];
                String date = dateFormat_date.format(new Date());
                return s.compareTo(date) < 0;
            }
        });
        return filePaths != null && filePaths.length > 0;
    }

    public String[] getNeedUploadFiles(Context context) {
        String filePath = logPath;
        File file = new File(filePath);
        String[] filePaths = null;
        if (!file.exists()) {
            ToastUtil.showToast("日志目录不存在");
            return null;
        }
        filePaths = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String s = name.split("\\.")[0];
                String date = dateFormat_date.format(new Date());
                return s.compareTo(date) < 0;
            }
        });
        return filePaths;

    }

    /**
     * 文件删除 除今天外的所有文件
     *
     */
    public void autoClear() {
        String rootFile = logPath;
        File rootF = new File(rootFile);
        if (!rootF.exists()) {
            rootF.mkdirs();
        }


        File[] files = rootF.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                String s = filename.split("\\.")[0];
//                String date = DateUtil.getDate(autoClearDay, dateFormat_date);
                String date = dateFormat_date.format(new Date());
                return s.compareTo(date) < 0;
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
            } else {
                file.delete();
            }
        }
        //删除压缩文件
        String zippath = logZPath;
        File file = new File(zippath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] zipFiles = file.listFiles();
        for (File zipFile : zipFiles) {
            if (zipFile.isDirectory()) {
            } else {
                zipFile.delete();
            }
        }


    }

    /**
     * 保存错误信息到文件中
     * * @param ex
     * * @return 返回文件名称,便于将文件传送到服务器
     * * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            String date = dateFormat_time.format(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }

        return null;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(3000);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) return false;
        try {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            collectDeviceInfo(mContext);
            saveCrashInfoFile(ex);
            SystemClock.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName ;
                String versionCode = String.valueOf(pi.versionCode);
                String uuid = UserManager.getUID();
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("设备唯一号uuid", uuid);
                if(UserManager.getUser()!=null){
                    String appid = UserManager.getAppId();
                    String clientNo = UserManager.getUser().getClientNo();
                    infos.put("appId", appid);
                    infos.put("开票点代码", clientNo);
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

}

