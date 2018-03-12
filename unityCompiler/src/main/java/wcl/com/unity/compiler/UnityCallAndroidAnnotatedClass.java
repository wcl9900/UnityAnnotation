package wcl.com.unity.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.wcl.unity.annotation.AndroidCallUnityIntercept;
import com.wcl.unity.annotation.BindUnityCallAndroid;
import com.wcl.unity.annotation.UnityCallAndroid;
import com.wcl.unity.annotation.UnityCallAndroidIntercept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import wcl.com.unity.compiler.utils.TypeUtils;

/**
 * Created by wangchunlong on 2018/3/10.
 */

public class UnityCallAndroidAnnotatedClass {

    private Element elementBind;
    private Elements elements;
    private RoundEnvironment roundEnv;

    private Map<String, ClassName> methodMap;

    private String bindUnityCallAndroid = "BindUnityCallAndroid";
    private String bindUnityCallAndroidInject = "BindUnityCallAndroidInject";

    public UnityCallAndroidAnnotatedClass(Element elementBind, Elements elements, RoundEnvironment roundEnv) {
        this.elementBind = elementBind;
        this.elements = elements;
        this.roundEnv = roundEnv;

        methodMap = new HashMap<>();
        init();
    }

    private void init(){
        checkElementBind();

        Set<? extends Element> elementsAnnotatedWithCallBack = roundEnv.getElementsAnnotatedWith(UnityCallAndroid.class);
        for (Element element : elementsAnnotatedWithCallBack){

            checkElementMethod(element);

            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            Name packageName = elements.getPackageOf(typeElement).getQualifiedName();

            String method = element.getSimpleName().toString();

            if(!methodMap.containsKey(method)){
                methodMap.put(method, ClassName.get(packageName.toString(), typeElement.getSimpleName().toString()));
            }
        }
    }

    private void checkElementBind() {
        if(elementBind.getKind() != ElementKind.FIELD){
            throw new IllegalArgumentException(String.format("Only filed can be annotated with @%s",
                    BindUnityCallAndroid.class.getSimpleName()));
        }
    }

    private void checkElementMethod(Element element){
        if(element.getKind() != ElementKind.METHOD){
            throw new IllegalArgumentException(String.format("Only method can be annotated with @%s",
                    UnityCallAndroid.class.getSimpleName()));
        }
        checkModifier(element, Modifier.PUBLIC);
        checkModifier(element, Modifier.STATIC);
    }

    private void checkModifier(Element element, Modifier compare){
        Iterator<Modifier> iterator = element.getModifiers().iterator();
        while (iterator.hasNext()){
            boolean isModifier = iterator.next() == compare;
            if(isModifier){
                return;
            }
        }
        throw new IllegalArgumentException(String.format("method %s need %s",
                element.getSimpleName(), compare.toString()));
    }

    private void checkElementIntercept(Element element){
        if(element.getKind() != ElementKind.METHOD){
            throw new IllegalArgumentException(String.format("Only method can be annotated with @%s",
                    AndroidCallUnityIntercept.class.getSimpleName()));
        }
        checkModifier(element, Modifier.PUBLIC);
        checkModifier(element, Modifier.STATIC);

        ExecutableElement executableElement = (ExecutableElement) element;
        List<? extends VariableElement> parameters = executableElement.getParameters();
        if(parameters.size() != 3){
            throw new IllegalArgumentException(String.format(
                    "parameter count must be %s in method %s",
                    "3",element.getSimpleName().toString()));
        }
        for (int i = 0; i < parameters.size(); i++){
            VariableElement variableElement = parameters.get(i);
            if(i == 0){
                if(!TypeName.get(variableElement.asType()).equals(TypeUtils.CONTEXT)){
                    throw new IllegalArgumentException(String.format(
                            "parameters in method %s must be (Context, String, String)",
                            element.getSimpleName().toString()));
                }
                continue;
            }
            boolean equals = TypeName.get(variableElement.asType()).equals(TypeUtils.STRING);
            if(!equals){
                throw new IllegalArgumentException(String.format(
                        "parameters in method %s must be (Context, String, String)",
                        element.getSimpleName().toString()));
            }
        }
    }

    public JavaFile generateUnityCallAndroidFile(){
        Set<String> methodsCallAndroid = methodMap.keySet();

        Set<? extends Element> elementsAnnotatedWithIntercept = roundEnv.getElementsAnnotatedWith(UnityCallAndroidIntercept.class);
        for (Element elementIntercept : elementsAnnotatedWithIntercept){
            checkElementIntercept(elementIntercept);
        }

        FieldSpec.Builder builderFiledHost = FieldSpec
                .builder(TypeName.get(elementBind.getEnclosingElement().asType()), "mHost", Modifier.PRIVATE);

        List<MethodSpec.Builder> methodBuilderList = new ArrayList<>();
        for (String method : methodsCallAndroid){
            ClassName methodClassType = methodMap.get(method);

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeUtils.STRING, "str",Modifier.FINAL);

            methodBuilder.addCode("(($T)mHost).runOnUiThread(new Runnable() {\n" +
                    "      @Override\n" +
                    "      public void run() {\n", TypeUtils.ACTIVITY);

            for (Element elementIntercept : elementsAnnotatedWithIntercept){
                methodBuilder.addStatement(
                    "          $T.$N($L,$S,$S)",
                        TypeName.get(elementIntercept.getEnclosingElement().asType()),
                        elementIntercept.getSimpleName().toString(), "mHost", method, "str");
            }

            methodBuilder.addStatement(
                    "          $T.$N($L,$L);\n"+
                    "    }\n" +
                    "})",
                    methodClassType, method, "mHost", "str");
            methodBuilderList.add(methodBuilder);
        }

        MethodSpec.Builder builderInject = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(elementBind.getEnclosingElement().asType()), "host")
                .addStatement("mHost = host");

        MethodSpec.Builder builderUnInject = MethodSpec.methodBuilder("unInject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(elementBind.getEnclosingElement().asType()), "host")
                .addStatement("mHost = null");

        //generaClass
        TypeElement classElement = (TypeElement) elementBind.getEnclosingElement();
        TypeSpec.Builder classBuilder = TypeSpec.
                classBuilder(classElement.getSimpleName().toString() + "$$" + bindUnityCallAndroid)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtils.BIND_INJECT, TypeName.get(classElement.asType())))
                .addModifiers(Modifier.PUBLIC);

        classBuilder.addField(builderFiledHost.build());
//        classBuilder.addMethod(builderConstruct.build());
        classBuilder.addMethod(builderInject.build());
        classBuilder.addMethod(builderUnInject.build());

        for (MethodSpec.Builder methodBuilder : methodBuilderList){
            classBuilder.addMethod(methodBuilder.build());
        }
        TypeSpec build = classBuilder.build();

        String packageName = elements.getPackageOf(classElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, build).build();
    }

    public JavaFile generateUnityCallAndroidInjectFile(){
        TypeElement classElement = (TypeElement) elementBind.getEnclosingElement();
        String methodClassName = classElement.getSimpleName().toString() + "$$" + bindUnityCallAndroid;

        String packageName = elements.getPackageOf(classElement).getQualifiedName().toString();
        ClassName UNITY_CALL_BACK_CLASS = ClassName.get(packageName, methodClassName);

        MethodSpec.Builder builderInject = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(classElement.asType()), "host")
                .addStatement("host.$N = new $T()",
                        elementBind.getSimpleName().toString(), UNITY_CALL_BACK_CLASS)
                .addStatement("(($T)host.$N).inject($L)", TypeUtils.BIND_INJECT, elementBind.getSimpleName(), "host");

        MethodSpec.Builder builderUnInject = MethodSpec.methodBuilder("unInject")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(classElement.asType()), "host")
                .addAnnotation(Override.class)
                .addStatement("(($T)host.$L).unInject($L)", TypeUtils.BIND_INJECT, elementBind.getSimpleName(), "host");

        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(classElement.getSimpleName() + "$$"+ bindUnityCallAndroidInject)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtils.BIND_INJECT, TypeName.get(classElement.asType())))
                .addMethod(builderInject.build())
                .addMethod(builderUnInject.build())
                .build();

        return JavaFile.builder(packageName, injectClass).build();
    }
}
