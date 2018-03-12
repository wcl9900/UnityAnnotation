package wcl.com.unity.compiler;

import com.google.auto.service.AutoService;
import com.wcl.unity.annotation.BindUnityCallAndroid;
import com.wcl.unity.annotation.UnityCallAndroid;
import com.wcl.unity.annotation.UnityCallAndroidIntercept;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import wcl.com.unity.compiler.utils.MessageUtils;

/**
 * Created by wangchunlong on 2018/3/10.
 */

@AutoService(Processor.class)
public class UnityCallAndroidProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    private Map<String, UnityCallAndroidAnnotatedClass> unityCallAndroidBindAnnotatedClassMap;

    private Map<String, AndroidCallUnityAnnotatedClass> androidCallUnityAnnotatedClassMap;

    boolean created = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        created = false;

        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();

        MessageUtils.messager = processingEnv.getMessager();

        typeUtils = processingEnv.getTypeUtils();

        unityCallAndroidBindAnnotatedClassMap = new TreeMap<>();
        androidCallUnityAnnotatedClassMap = new TreeMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(created) return true;
        MessageUtils.note("process UnityCallAndroidProcessor>>>>>>>>");
        createUnityCallAndroid(roundEnv);
        created = true;
        return true;
    }

    private void createUnityCallAndroid(RoundEnvironment roundEnv) {
        unityCallAndroidBindAnnotatedClassMap.clear();

        Set<? extends Element> elementsAnnotatedWithBindUnityCallBack = roundEnv.getElementsAnnotatedWith(BindUnityCallAndroid.class);
        for (Element element : elementsAnnotatedWithBindUnityCallBack){
            createBindAnnotatedClass(element, roundEnv);
        }

        Collection<UnityCallAndroidAnnotatedClass> unityCallAndroidAnnotatedClasses = unityCallAndroidBindAnnotatedClassMap.values();
        for (UnityCallAndroidAnnotatedClass bindAnnotatedClass : unityCallAndroidAnnotatedClasses){
            try {
                bindAnnotatedClass.generateUnityCallAndroidFile().writeTo(filer);
                MessageUtils.note("generateUnityCallAndroidFile>>>");
                bindAnnotatedClass.generateUnityCallAndroidInjectFile().writeTo(filer);
                MessageUtils.note("generateUnityCallAndroidInjectFile>>>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createBindAnnotatedClass(Element element, RoundEnvironment roundEnv){
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        if(!unityCallAndroidBindAnnotatedClassMap.containsKey(fullName)){
            UnityCallAndroidAnnotatedClass bindAnnotatedClass = new UnityCallAndroidAnnotatedClass(element, elementUtils,roundEnv);
            unityCallAndroidBindAnnotatedClassMap.put(fullName, bindAnnotatedClass);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(UnityCallAndroid.class.getCanonicalName());
        types.add(BindUnityCallAndroid.class.getCanonicalName());
        types.add(UnityCallAndroidIntercept.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
