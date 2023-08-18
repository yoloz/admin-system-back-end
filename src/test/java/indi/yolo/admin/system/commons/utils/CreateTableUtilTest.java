package indi.yolo.admin.system.commons.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author yoloz
 */
class CreateTableUtilTest {

    @Test
    void createTableSQL() throws IOException {
        String projectName = new File("").getCanonicalPath() + "/src/main/java";
        String packageName = "indi.yolo.admin.system.modules";
        CreateTableUtil createTableUtil = new CreateTableUtil();
        List<String> list = createTableUtil.createTableSQL(projectName, packageName);
        for (String s : list) {
            System.out.println(s);
        }
    }
}