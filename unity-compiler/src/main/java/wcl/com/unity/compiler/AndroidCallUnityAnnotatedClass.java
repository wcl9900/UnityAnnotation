package wcl.com.unity.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.wcl.unity.annotation.AndroidCallUnity;
import com.wcl.unity.annotation.AndroidCallUnityIntercept;
import com.wcl.unity.annotation.BindAndroidCallUnity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import wcl.com.unity.compiler.utils.TextUtils;
import wcl.com.unity.compiler.utils.TypeUtils;

/**
 * Created by wangchunlong on 2018/3/10.
 */

public class AndroidCallUnityAnnotatedClass {
    private Element elementBind;
    private Elements elements;
    private RoundEnvironment roundEnv;

    public AndroidCallUnityAnnotatedClass(Element elementBind, Elements elements, RoundEnvironment roundEnv){
        this.elementBind = elementBind;
        this.elements = elements;
        this.roundEnv = roundEnv;
        checkElementBind();
    }

    public JavaFile generateAndroidCallUnityFile(){
        BindAndroidCallUnity annotationBindAndroidCallUnity = elementBind.getAnnotation(BindAndroidCallUnity.class);
        String gameObject = annotationBindAndroidCallUnity.gameObject();
        if(TextUtils.isEmpty(gameObject)){
            throw new IllegalArgumentException(String.format("%s can not be empty in %s!!!",
                    "gameObject", annotationBindAndroidCallUnity.toString()));
        }

        Set<? extends Element> elementsAnnotatedWithIntercept = roundEnv.getElementsAnnotatedWith(AndroidCallUnityIntercept.class);
        for (Element elementIntercept : elementsAnnotatedWithIntercept){
            checkElementIntercept(elementIntercept);
        }

        List<MethodSpec.Builder> methodBuilderList = new ArrayList<>();
        Set<? extends Element> elementsAnnotatedWithAndroidCallUnity = roundEnv.getElementsAnnotatedWith(AndroidCallUnity.class);
        for (Element element : elementsAnnotatedWithAndroidCallUnity){
            checkElementMethod(element);

            ExecutableElement executableElement = (ExecutableElement) element;
            List<? extends VariableElement> parameters = executableElement.getParameters();

            String methodAndroid = element.getSimpleName().toString();
            String methodUnity = element.getAnnotation(AndroidCallUnity.class).UnityMethod();
            if(TextUtils.isEmpty(methodUnity)){
                methodUnity = methodAndroid;
            }

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodAndroid)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            if(parameters.size() > 1){
                throw new IllegalArgumentException(String.format("%s method parameter count must less than or equal to %s",
                        methodAndroid, "1"));
            }

            // create method start
            if(parameters.size() == 1) {
                VariableElement variableElement = parameters.get(0);
                String argsName = variableElement.getSimpleName().toString();
                methodBuilder.addParameter(TypeName.get(variableElement.asType()), argsName);

                methodBuilder.addStatement("String str = $T.$N($L)",
                        TypeName.get(element.getEnclosingElement().asType()), methodAndroid, argsName);
            }
            else {
                methodBuilder.addStatement("$T.$N()",
                        TypeName.get(element.getEnclosingElement().asType()), methodAndroid);
                methodBuilder.addStatement("String str = \"\"",
                        TypeName.get(element.getEnclosingElement().asType()), methodAndroid);
            }

            for (Element elementIntercept : elementsAnnotatedWithIntercept){
                methodBuilder.addStatement("$T.$N($S,$S,$L)",
                        TypeName.get(elementIntercept.getEnclosingElement().asType()),
                        elementIntercept.getSimpleName(), gameObject, methodAndroid, "str");
            }

            methodBuilder.addStatement("$T.$N($S,$S,$L)",
                    TypeUtils.UNITY_PLAYER, "UnitySendMessage", gameObject, methodUnity, "str");
            //create method end.

            methodBuilderList.add(methodBuilder);
        }

        //create class start
        String className = annotationBindAndroidCallUnity.className();
        if(TextUtils.isEmpty(className)){
            className = elementBind.getSimpleName() + "_";
        }
        TypeSpec.Builder buildClass = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
        for (MethodSpec.Builder methodSpecBuilder : methodBuilderList){
            buildClass.addMethod(methodSpecBuilder.build());
        }
        //create class end

        String packageName = elements.getPackageOf(elementBind).toString();
        return JavaFile.builder(packageName, buildClass.build()).build();
    }

    private void checkElementBind(){
        if(elementBind.getKind() != ElementKind.CLASS){
            throw new IllegalArgumentException(String.format("%s need describe %s",
                    elementBind.getSimpleName(), "class"));
        }
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
        for (VariableElement variableElement : parameters){
            boolean equals = TypeName.get(variableElement.asType()).equals(TypeUtils.STRING);
            if(!equals){
                throw new IllegalArgumentException(String.format(
                        "parameter in method %s must be String",
                        element.getSimpleName().toString()));
            }
        }
    }
    private void checkElementMethod(Element element){
        if(element.getKind() != ElementKind.METHOD){
            throw new IllegalArgumentException(String.format("Only method can be annotated with @%s",
                    AndroidCallUnity.class.getSimpleName()));
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
}
