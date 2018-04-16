package com.jenphy.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

    public enum ClassType {
        ENUM,
        INTERFACE,
        ANNOTATION,
    }

    public static List<Class<?>> getClassesByAnnotation(ClassLoader classLoader, String packageName, Class<?> annotationType, ClassType... classTypes) {
        List<Class<?>> allClasses = getClasses(classLoader, packageName, classTypes);
        List<Class<?>> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(allClasses)) {
            return result;
        }
        for (Class clazz : allClasses) {
            if(clazz.getAnnotation(annotationType) != null) {
                result.add(clazz);
            }
//            Annotation annotation = (Annotation) getAnnotation(clazz, annotationType);
//            if (annotation != null) {
//                result.add(clazz);
//            }
        }
        return result;
    }

    /**
     * 从包package中获取所有的Class
     *
     */
    private static List<Class<?>> getClasses(ClassLoader classLoader, String packageName, ClassType... classTypes) {

        Set<ClassType> classTypeSet = new HashSet<>(Arrays.asList(classTypes));
        // 第一个class类的集合
        List<Class<?>> classList = new ArrayList<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
//            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//            ClassLoader classLoader = Object.class.getClassLoader();
//            while (classLoader.getParent() != null) {
//                classLoader = classLoader.getParent();
//            }
            dirs = classLoader.getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(classLoader, packageName, filePath, recursive, classList, classTypeSet);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes

                                            Class clazz = Class.forName(packageName + '.' + className, true, classLoader);
                                            addClassListByClassType(classLoader, classList, clazz, classTypeSet);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classList;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     */
    private static void findAndAddClassesInPackageByFile(ClassLoader classLoader, String packageName, String packagePath, final boolean recursive, List<Class<?>> classList, Set<ClassType> classTypeSet) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(classLoader,packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classList, classTypeSet);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    Class clazz = Class.forName(packageName + '.' + className, true, classLoader);
//                    Class clazz = Class.forName(packageName + '.' + className);
                    addClassListByClassType(classLoader, classList, clazz, classTypeSet);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void addClassListByClassType(ClassLoader classLoader, List<Class<?>> classList, Class clazz, Set<ClassType> classTypeSet) {
        if (CollectionUtils.isNotEmpty(classTypeSet)) {
            if (classTypeSet.contains(ClassType.ENUM) && clazz.isEnum()) {
                classList.add(clazz);
            } else if (classTypeSet.contains(ClassType.INTERFACE) && clazz.isInterface()) {
                classList.add(clazz);
            } else if (classTypeSet.contains(ClassType.ANNOTATION) && clazz.isAnnotation()) {
                classList.add(clazz);
            }
        } else {
            classList.add(clazz);
        }
    }

}
