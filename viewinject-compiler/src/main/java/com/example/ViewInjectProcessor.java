package com.example;

import com.example.InjectorInfo;

import com.example.annotation.ViewInjector;
import com.example.util.AnnotationUtil;
import com.example.util.InjectorInfoUtil;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/4/24
 * Version  1.0
 * Description:
 */
@AutoService(Processor.class)
public class ViewInjectProcessor extends AbstractProcessor {


    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(ViewInjector.class.getCanonicalName());
        return supportTypes;
    }

    final Map<String, List<VariableElement>> map = new HashMap<String, List<VariableElement>>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(ViewInjector.class);
        for (Element element:elementSet) {
//            if (element.getKind() != ElementKind.CLASS) {
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "only support class");
//            }
            checkAnnotationValid(element,ViewInjector.class);

            VariableElement varElement= (VariableElement)element;
            String className = getParentClassName(varElement);
            List<VariableElement> cacheElements = map.get(className);
            if(cacheElements==null){
                cacheElements = new LinkedList<VariableElement>();
            }
            cacheElements.add(varElement);
            map.put(className,cacheElements);
        }


        generate();

        return false;
    }

    private void generate() {
        Iterator<Map.Entry<String, List<VariableElement>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<VariableElement>> entry = iterator.next();
            List<VariableElement> cacheElements = entry.getValue();
            if (cacheElements == null || cacheElements.size() == 0) {
                continue;
            }
            InjectorInfo info = InjectorInfoUtil.createInjectorInfo(processingEnv,cacheElements.get(0));
            TypeElement typeElement = (TypeElement) cacheElements.get(0).getEnclosingElement();

            final ClassName  className = ClassName.get(info.packageName,info.classlName);
            final ClassName  InterfaceName=ClassName.get("com.example","InjectAdapter");
            MethodSpec.Builder injectsBuilder = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(void.class)
                    .addParameter(className, "target");
            for (VariableElement element:cacheElements) {
                InjectorInfo temInfo = InjectorInfoUtil.createInjectorInfo(processingEnv,element);
                ViewInjector annotation = element.getAnnotation(ViewInjector.class);
                int value = annotation.value();
                String fieldName = element.getSimpleName().toString();
                String type = element.asType().toString();
//                injectsBuilder.addStatement("if(source instanceof android.app.Activity){\n");
//                (android.app.Activity)

                injectsBuilder.addStatement("target." + fieldName + " = ("+type+")(target).findViewById(" + value + ")");

//                injectsBuilder.addStatement("\n};");


            }
            MethodSpec injects = injectsBuilder.build();

            TypeSpec typeSpec = TypeSpec.classBuilder(info.newClassName)
                    .addSuperinterface(ParameterizedTypeName.get(InterfaceName, className))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(injects)
                    .build();

            JavaFile javaFile = JavaFile.builder(info.packageName, typeSpec).build();


            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();

//                error(typeElement,e.getMessage());
            }


        }

    }

    /**
     * @param element
     * @param message
     * @param args
     */
    protected void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private String getParentClassName(VariableElement varElement) {

        TypeElement typeElement = (TypeElement) varElement.getEnclosingElement();

        String packageName = AnnotationUtil.getPackageName(processingEnv,typeElement);

        return packageName + "." + typeElement.getSimpleName().toString();
    }


//
    private boolean checkAnnotationValid(Element annotatedElement, Class clazz)
    {
        if (annotatedElement.getKind() != ElementKind.FIELD)
        {
            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement))
        {
            error(annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }

        return true;
    }

}
