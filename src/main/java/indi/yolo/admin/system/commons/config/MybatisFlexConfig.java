package indi.yolo.admin.system.commons.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import indi.yolo.admin.system.commons.entity.base.BasePO;
import indi.yolo.admin.system.commons.intrcepter.BasePOListener;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author yoloz
 */
@Configuration
public class MybatisFlexConfig implements ConfigurationCustomizer, SqlSessionFactoryBeanCustomizer, MyBatisFlexCustomizer {

    @Autowired
    UserContextManager userContextManager;

    //mybatis configuration 配置
    @Override
    public void customize(FlexConfiguration flexConfiguration) {
        // SQL日志输出到控制台,生产环境需要注释掉
        flexConfiguration.setLogImpl(StdOutImpl.class);
    }

    //SqlSessionFactoryBean 配置
    @Override
    public void customize(SqlSessionFactoryBean sqlSessionFactoryBean) {
    }

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        BasePOListener baseEntityListener = new BasePOListener(userContextManager);
        flexGlobalConfig.registerInsertListener(baseEntityListener, BasePO.class);
        flexGlobalConfig.registerUpdateListener(baseEntityListener, BasePO.class);
    }
}
