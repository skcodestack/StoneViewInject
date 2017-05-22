package com.example;

import java.io.File;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/4/24
 * Version  1.0
 * Description:
 */

public class InjectorInfo {
    public static final String SUFFIX = "InjectAdapter";
    public String packageName;

    public String classlName;

    public String newClassName;

    public InjectorInfo(String packageName, String classlName) {
        this.packageName = packageName;
        newClassName = classlName +"$$"+SUFFIX;
        this.classlName = classlName;
    }

    public String getClassFullPath() {
        return packageName + File.separator + newClassName;
    }
}
