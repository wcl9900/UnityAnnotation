package wcl.com.unity.compiler.utils;

import java.util.Collection;

/**
 * Created by wangchunlong on 2018/7/24.
 */

public class CollectionUtils {
    public static boolean isNotEmpty(Collection collection){
        return collection != null && collection.size() != 0;
    }
}
