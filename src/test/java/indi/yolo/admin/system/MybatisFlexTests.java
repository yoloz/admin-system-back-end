package indi.yolo.admin.system;

import com.mybatisflex.core.query.QueryWrapper;
import indi.yolo.admin.system.modules.user.entity.table.UserTableDef;
import org.junit.jupiter.api.Test;

public class MybatisFlexTests {

    @Test
    void testSelectAllSQL() {
        QueryWrapper queryWrapper = QueryWrapper.create().select().from(UserTableDef.USER);
        System.out.println(queryWrapper.toSQL());
    }

    //Column: isLarge 需要手动select指定列
    @Test
    void testSelectDefaultColumnSQL() {
        QueryWrapper queryWrapper = QueryWrapper.create().select(UserTableDef.USER.DEFAULT_COLUMNS).from(UserTableDef.USER);
        System.out.println(queryWrapper.toSQL());
    }
}
