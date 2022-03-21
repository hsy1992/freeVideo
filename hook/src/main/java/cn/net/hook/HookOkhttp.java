package cn.net.hook;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @ClassName HookOkhttp
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/2/6 20:39
 * @Version 1.0
 */
public class HookOkhttp implements IXposedHookLoadPackage {

    private ClassLoader mClassLoader;
    private Context mOtherContext;
    private Class<?> okHttpClient = null;
    private Class<?> okHttpBuilder = null;
    //存放 全部类名字的 集合
    private final List<String> AllClassNameList = new ArrayList<>();
    //存放 全部类的 集合
    public static ArrayList<Class> mClassList = new ArrayList<>();
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("HookOkhttp >> current package:" + lpparam.packageName);

        if ("com.shixin.html".equals(lpparam.packageName)) {
            try {
                hookApplication();
            } catch (Throwable t) {
                XposedBridge.log(t);
            }
        }
    }

    private void hookApplication() {
        XposedHelpers.findAndHookMethod(Application.class,
                "attach",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        mOtherContext = (Context) param.args[0];
                        mClassLoader = mOtherContext.getClassLoader();
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        HookOKClient();
                    }
                });
    }

    /**
     * 判断 OkHttp是否存在 混淆 这步骤
     */
    private synchronized void HookOKClient() {
        try {
            if (okHttpClient == null) {
                okHttpClient = Class.forName("okhttp3.OkHttpClient", false, mClassLoader);
            }
        } catch (ClassNotFoundException e) {
            XposedBridge.log("出现异常 发现对方没有使用OkHttp或者被混淆,开始尝试自动获取路径 ");
        }
        try {
            if (okHttpBuilder == null) {
                okHttpBuilder = Class.forName("okhttp3.OkHttpClient$Builder", false, mClassLoader);
            }
        } catch (ClassNotFoundException e) {
            XposedBridge.log("出现异常 发现对方没有使用OkHttp或者被混淆,开始尝试自动获取路径 ");
            XposedBridge.log("HookAndInitProguardClass");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HookAndInitProguardClass();
                }
            }).start();
        }
        if (isExactness()) {
            XposedBridge.log("okHttp本身未混淆");
//            HookClientAndBuilderConstructor();
        } else {
            XposedBridge.log("okHttp本身混淆");
//            HookAndInitProguardClass();
        }
    }

    /**
     * @return 这个App是否是被混淆的okHttp
     */
    private boolean isExactness() {
        return okHttpClient.getName().equals("okhttp3.OkHttpClient")
                && okHttpBuilder.getName().equals("okhttp3.OkHttpClient$Builder")
                //拦截器里面常用的类不等于Null 才可以保证插件正常加载
                && getClass("okio.Buffer") != null
                && getClass("okio.BufferedSource") != null
                && getClass("okio.GzipSource") != null
                && getClass("okhttp3.Request") != null
                && getClass("okhttp3.Response") != null
                && getClass("okio.Okio") != null
                && getClass("okio.Base64") != null
                ;
    }

    /**
     * 遍历当前进程的Classloader 尝试进行获取指定类
     *
     * @param className
     * @return
     */
    private Class getClass(String className) {
        Class<?> aClass = null;
        try {
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException classNotFoundE) {

                try {
                    aClass = Class.forName(className, false, mClassLoader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return aClass;
        } catch (Throwable e) {

        }
        return null;
    }

    private synchronized void HookAndInitProguardClass() {
        //放在子线程去执行，防止卡死
        //先拿到 app里面全部的

        getAllClassName();

        initAllClass();


        //第一步 先开始 拿到 OkHttp 里面的 类  如Client 和 Builder
        getClientClass();
//        getBuilder();
//
//        if (OkHttpBuilder != null && OkHttpClient != null) {
//            XposedBridge.log("使用了okHttp 开始添加拦截器");
//            HookClientAndBuilderConstructor();
//        } else {
//            XposedBridge.log("对方App可能没有使用okHttp 开始Hook底层方法");
//            //可能对方没有使用OkHttp
//            HookGetOutPushStream();
//        }

    }

    /**
     * 获取 ClientCLass的方法
     */
    private void getClientClass() {
        if (mClassList.size() == 0) {
            XposedBridge.log("全部的 集合 mClassList  的个数 为 0  ");
            return;
        }
        XposedBridge.log("开始 查找   ClientClass");
        try {
            for (Class mClient : mClassList) {
                //判断 集合 个数 先拿到 四个集合 可以 拿到 Client
                if (isClient(mClient)) {
                    okHttpClient = mClient;
                    return;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        XposedBridge.log("没找到 client ");
    }

    private boolean isClient(@NonNull Class<?> mClass) {
        try {
            int typeCount = 0;
            int StaticCount = 0;

            //getDeclaredFields 是个 获取 全部的
            Field[] fields = mClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String type = field.getType().getName();

                //XposedBridge.log(" 复合 规则 该 Field是      " + type  +"是否包含      "+ type.contains(Key.ListType) +"   是否是 final类型  "+Modifier.isFinal(field.getModifiers()));

                //四个 集合 四个final 特征
                if (type.contains(List.class.getName()) && Modifier.isFinal(field.getModifiers())) {
                    //XposedBridge.log(" 复合 规则 该 Field是      " + field.getName() + " ");
                    typeCount++;
                }
                if (type.contains(List.class.getName()) && Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                    //XposedBridge.log(" 复合 规则 该 Field是      " + field.getName() + " ");
                    StaticCount++;
                }
            }
            //XposedBridge.log("field 符合标准   个数 是    " + typeCount);

            if (StaticCount >= 2 && typeCount == 6 && mClass.getInterfaces().length >= 1) {
                XposedBridge.log("找到OkHttpClient  该类的名字是  " + mClass.getName());
                return true;
            }
        } catch (Throwable e) {
            XposedBridge.log("isClient error " + e.toString());
            e.printStackTrace();
        }
        // mFieldArrayList.clear();
        return false;
    }

    private synchronized void getAllClassName() {

        //保证每次初始化 之前 保证干净
        AllClassNameList.clear();
        XposedBridge.log("开始 获取全部的类名  ");
        try {
            //系统的 classloader是 Pathclassloader需要 拿到他的 父类 BaseClassloader才有 pathList
            if (mClassLoader == null) {
                return;
            }
            Field pathListField = mClassLoader.getClass().getSuperclass().getDeclaredField("pathList");
            if (pathListField != null) {
                pathListField.setAccessible(true);
                Object dexPathList = pathListField.get(mClassLoader);
                Field dexElementsField = dexPathList.getClass().getDeclaredField("dexElements");
                if (dexElementsField != null) {
                    dexElementsField.setAccessible(true);
                    Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);
                    for (Object dexElement : dexElements) {
                        Field dexFileField = dexElement.getClass().getDeclaredField("dexFile");
                        if (dexFileField != null) {
                            dexFileField.setAccessible(true);
                            DexFile dexFile = (DexFile) dexFileField.get(dexElement);
                            getDexFileClassName(dexFile);
                        } else {
                            XposedBridge.log("获取 dexFileField Null ");
                        }
                    }
                } else {
                    XposedBridge.log("获取 dexElements Null ");
                }
            } else {
                XposedBridge.log("获取 pathListField Null ");
            }
            //获取 app包里的
            //DexFile df = new DexFile(mOtherContext.getPackageCodePath());
        } catch (Throwable e) {
            XposedBridge.log("getAllClassName   Throwable   " + e.toString());
            e.printStackTrace();
        }
    }

    private void getDexFileClassName(DexFile dexFile) {
        if (dexFile == null) {
            return;
        }

        //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
        Enumeration<String> enumeration = dexFile.entries();
        while (enumeration.hasMoreElements()) {//遍历
            String className = enumeration.nextElement();
            //添加过滤信息
            if (className.contains("okhttp") || className.contains("okio")
            ) {
                AllClassNameList.add(className);
            }
        }
    }

    /**
     * 初始化 需要的 class的 方法
     */
    private void initAllClass() {
        mClassList.clear();

        try {
            XposedBridge.log("需要初始化Class的个数是  " + AllClassNameList.size());

            Class<?> MClass = null;

            for (int i = 0; i < AllClassNameList.size(); i++) {

                MClass = getClass(AllClassNameList.get(i));
                if (MClass != null) {
                    //XposedBridge.log("添加成功 "+MClass.getName());
                    mClassList.add(MClass);
                }

            }

            XposedBridge.log("初始化全部类的个数   " + mClassList.size());
        } catch (Throwable e) {
            XposedBridge.log("initAllClass error " + e.toString());
        }
    }

}
