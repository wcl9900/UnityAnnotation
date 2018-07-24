package wcl.com.unity.compiler;

import com.google.auto.service.AutoService;
import com.wcl.unity.annotation.AndroidCallUnity;
import com.wcl.unity.annotation.AndroidCallUnityIntercept;
import com.wcl.unity.annotation.BindAndroidCallUnity;

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

import wcl.com.unity.compiler.utils.CollectionUtils;
import wcl.com.unity.compiler.utils.MessageUtils;

/**
 * Created by wangchunlong on 2018/3/10.
 */

@AutoService(Processor.class)
public class AndroidCallUnityProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    private Map<String, AndroidCallUnityAnnotatedClass> androidCallUnityAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();

        MessageUtils.messager = processingEnv.getMessager();

        typeUtils = processingEnv.getTypeUtils();

        androidCallUnityAnnotatedClassMap = new TreeMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(CollectionUtils.isNotEmpty(annotations)) {
            createAndroidCallUnity(roundEnv);
            return true;
        }
        return false;
    }

    private void createAndroidCallUnity(RoundEnvironment roundEnv) {
        MessageUtils.note("process AndroidCallUnityProcessor>>>>>>>>");
        Set<? extends Element> elementsAnnotatedWithBindAndroidCallUnity = roundEnv.getElementsAnnotatedWith(BindAndroidCallUnity.class);
        for (Element element : elementsAnnotatedWithBindAndroidCallUnity){

            createAndroidCallUnityAnnotatedClass(element, roundEnv);
        }

        for (AndroidCallUnityAnnotatedClass callUnityAnnotatedClass : androidCallUnityAnnotatedClassMap.values()){
            try {
                callUnityAnnotatedClass.generateAndroidCallUnityFile().writeTo(filer);
                MessageUtils.note("generateAndroidCallUnityFile>>>>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createAndroidCallUnityAnnotatedClass(Element element, RoundEnvironment roundEnvironment){
        String simpleName = element.getSimpleName().toString();
        if(!androidCallUnityAnnotatedClassMap.containsKey(simpleName)){
            AndroidCallUnityAnnotatedClass callUnityAnnotatedClass = new AndroidCallUnityAnnotatedClass(element, elementUtils, roundEnvironment);
            androidCallUnityAnnotatedClassMap.put(simpleName, callUnityAnnotatedClass);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AndroidCallUnity.class.getCanonicalName());
        types.add(BindAndroidCallUnity.class.getCanonicalName());
        types.add(AndroidCallUnityIntercept.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
