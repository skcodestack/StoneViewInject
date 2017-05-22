package com.example.util;

import com.example.InjectorInfo;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/4/24
 * Version  1.0
 * Description:
 */

public class InjectorInfoUtil {

    public   static InjectorInfo createInjectorInfo(ProcessingEnvironment mProcessingEnv, VariableElement element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String packageName = AnnotationUtil.getPackageName(mProcessingEnv, typeElement);
        String className = typeElement.getSimpleName().toString();
        return new InjectorInfo(packageName, className);
    }
}
