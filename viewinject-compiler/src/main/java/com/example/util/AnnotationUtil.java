package com.example.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/4/24
 * Version  1.0
 * Description:
 */

public class AnnotationUtil {
    public static String getPackageName(ProcessingEnvironment mProcessingEnv, Element varElement){
        return mProcessingEnv.getElementUtils().getPackageOf(varElement).getQualifiedName().toString();
    }
}
