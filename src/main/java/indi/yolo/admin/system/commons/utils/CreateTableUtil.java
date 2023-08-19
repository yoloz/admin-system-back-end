package indi.yolo.admin.system.commons.utils;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.processor.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;

/**
 * just for bean table sql(mysql)
 *
 * @author yoloz
 */
@Slf4j
public class CreateTableUtil {

    private final Map<String, String> types = new HashMap<>();

    public CreateTableUtil() {
        types.put("int", "INT UNSIGNED");
        types.put("java.lang.Integer", "INTEGER UNSIGNED");
        types.put("long", "BIGINT UNSIGNED");
        types.put("java.lang.Long", "BIGINT UNSIGNED");
        types.put("float", "FLOAT");
        types.put("java.lang.Float", "FLOAT");
        types.put("double", "DOUBLE");
        types.put("java.lang.Double", "DOUBLE");
        types.put("char", "CHAR");
        types.put("java.lang.Character", "CHAR");
        types.put("java.util.Date", "DATETIME");
        types.put("java.sql.Timestamp", "TIMESTAMP");
        types.put("java.math.BigDecimal", "DECIMAL");
        types.put("boolean", "TINYINT UNSIGNED");
        types.put("java.lang.Boolean", "TINYINT UNSIGNED");
    }

    private record ColumnDef(String name, String type, boolean isPrimary) {
    }

    private record TableDef(String name, List<ColumnDef> columns) {
    }

    /**
     * @param projectPath 源码父路径
     * @param packageName 扫描的包
     */
    public List<String> createTableSQL(String projectPath, String packageName) {
        String directory = Paths.get(projectPath, packageName.replace(".", "/")).toString();
        List<String> allFiles = getAllClassFile(directory);
        List<String> classFiles = pathToPackage(projectPath, allFiles);
        List<Class<?>> tableClasses = getAllTableClass(classFiles);
        List<TableDef> tableDefList = tableClasses.stream().map(this::getTableDef).toList();
        List<String> sqlList = new ArrayList<>(tableDefList.size());
        for (TableDef tableDef : tableDefList) {
            sqlList.add(doSql(tableDef));
        }
        return sqlList;
    }


    // 获取当前包下的所有class对象的名字
    private List<String> getAllClassFile(String directory) {
        Stack<String> stack = new Stack<>();
        stack.push(directory);
        List<String> allClassFiles = new ArrayList<>();
        while (!stack.empty()) {
            File file = new File(stack.pop());
            if (!file.exists()) {
                log.warn("path[" + file.getPath() + "],exit...");
                return Collections.emptyList();
            }
            File[] files = file.listFiles((pathname) -> (file.isDirectory()) || (file.getName().endsWith(".java")));
            if (files == null) {
                continue;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    stack.push(f.getPath());
                } else {
                    allClassFiles.add(f.getPath());
                }
            }
        }
        return allClassFiles;
    }

    //将绝对路径转为全类名格式方便反射
    private List<String> pathToPackage(String prefix, List<String> allClassFiles) {
        int len = prefix.length();
        List<String> list = new ArrayList<>(allClassFiles.size());
        for (String s : allClassFiles) {
            if (s.indexOf("/") == 0) {
                s = s.substring(1);
            }
            s = s.replace("/", ".");
            list.add(s.substring(len, s.length() - 5)); //.java
        }
        return list;
    }

    // 拿到所有com.mybatisflex.annotation.Table注解的类
    private List<Class<?>> getAllTableClass(List<String> classFiles) {
        List<Class<?>> list = new ArrayList<>();
        try {
            for (String classFile : classFiles) {
                Class<?> aClass = Class.forName(classFile);
                if (aClass.getAnnotation(Table.class) != null) {
                    list.add(aClass);
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("get table class fail...", e);
        }
        return list;
    }

    private boolean checkPrimaryKey(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            return annotation.annotationType().getName().equals(Id.class.getName());
        }
        return false;
    }

    // 当前class文件的定义信息,未处理JdbcType
    private TableDef getTableDef(Class<?> clazz) {
        Table tanno = clazz.getAnnotation(Table.class);
        String tableName = tanno.value();
        if (StringUtils.isEmpty(tableName)) {
            throw new RuntimeException("请在注解里加上表名...");
//            String name = clazz.getName();
//            tableName = name.substring(name.lastIndexOf('.') + 1);
        }
        List<Field> fields = new ArrayList<>();
        // getDeclaredFields 可以获取private，但是不能获取父类
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        List<ColumnDef> columnDefList = new ArrayList<>(fields.size());
        for (Field field : fields) {
            String colName = StrUtil.camelToUnderline(field.getName());
            Column colAnno = field.getAnnotation(Column.class);
            if (colAnno != null) {
                if (colAnno.ignore()) {
                    continue;
                }
                if (!StringUtils.isEmpty(colAnno.value())) {
                    colName = colAnno.value();
                }
            }
            columnDefList.add(new ColumnDef(colName, field.getType().getName(), checkPrimaryKey(field)));
        }
        return new TableDef(tableName, columnDefList);
    }

    private String doSql(TableDef tableDef) {
        String tableName = tableDef.name;
        log.debug("begin: " + tableName + "...");
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists `").append(tableName).append("` (");
        sb.append("\n");
        List<ColumnDef> columns = tableDef.columns;
        for (ColumnDef column : columns) {
            sb.append("`").append(column.name).append("`");
            sb.append('\t');
            sb.append(types.getOrDefault(column.type, "VARCHAR(255)"));
            if (column.isPrimary) {
                sb.append("\t");
                sb.append("primary key");
            }
            sb.append(',');
            sb.append('\n');
        }
        int i = sb.lastIndexOf(",");
        sb.deleteCharAt(i);
        sb.append(")");
//        log.debug("end: " + tableName + "...");
        return sb.toString();
    }

}
