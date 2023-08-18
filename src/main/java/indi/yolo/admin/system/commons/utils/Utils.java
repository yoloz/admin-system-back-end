package indi.yolo.admin.system.commons.utils;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.HexUtil;

import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yoloz
 */
@Slf4j
public class Utils {

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static String ripeMD160(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("RipeMD160");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] result = md.digest();
            return HexUtil.encodeToString(result, true);
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
        }
        return text;
    }

    public static <T> T updateObject(T oldObj, T newObj, Class<T> clazz) throws Exception {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method getMethod = pd.getReadMethod();
            Object newVal = getMethod.invoke(newObj);
            if (newVal == null) {
                Object oldVal = getMethod.invoke(oldObj);
                Method setMethod = pd.getWriteMethod();
                setMethod.invoke(newObj, oldVal);
            }
        }
        return newObj;
    }

}
