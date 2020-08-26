package com.xxc.android10.utils;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Create By xxc
 * Date: 2020/8/26 14:10
 * Desc:
 */
public class PermissionUtil {
    private static RxPermissions sRxPermissions;

    private PermissionUtil() {
    }

    public static RxPermissions getRxPermissions(FragmentActivity fragmentActivity) {
        if (sRxPermissions == null) {
            synchronized (PermissionUtil.class) {
                if (sRxPermissions == null) {
                    sRxPermissions = new RxPermissions(fragmentActivity);
                }
            }
        }

        return sRxPermissions;
    }
}

/**
 * 懒汉式单例
 */
//public class Singleton {
//    private static Singleton instance;
//
//    /**
//     * 不允许使用new创建实例
//     */
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        if (instance == null) {
//            instance = new Singleton();
//        }
//        return instance;
//    }
//}

/**
 * 线程安全的懒汉式
 */
//public class Singleton {
//    private static Singleton instance;
//
//    private Singleton() {
//    }
//
//    public synchronized static Singleton getInstance() {
//        if (instance == null) {
//            instance = new Singleton();
//        }
//
//        return instance;
//    }
//}

/**
 * 双重校验锁实现线程安全的懒汉式单例
 */
//public class Singleton {
//    private static Singleton instance;
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        if (instance == null) {
//            synchronized (Singleton.class) {
//                if (instance == null) {
//                    instance = new Singleton();
//                }
//            }
//        }
//
//        return instance;
//    }
//}

/**
 * 饿汉式单例
 * 典型的空间换时间，只要类加载了就会创建实例，不管你是否使用；
 * 在服务器内存资源有限的情况下不可取。
 */
//public class Singleton {
//    private final static Singleton instance = new Singleton();
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        return instance;
//    }
//}
