package com.ding.dingitems.util.mysql;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {


    /**
     * 生成自动代码
     * @param moduleName
     * @param parent
     * @param tableName 可以逗号分割多表
     * @param isOverride false 不覆盖原有文件 ，true 覆盖原有文件
     */
    public static void startCode(String moduleName,String parent,String tableName,boolean isOverride) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("hjw");
        gc.setOpen(false);
        // 第二次会覆盖老的
        gc.setFileOverride(isOverride);
        // 设置生成service名字
        gc.setServiceName("%sService");
        gc.setBaseResultMap(true);
        //mapper.xml 是否生成 ColumnList，默认 false 不生成
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/dmeeting?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent(parent);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");

        strategy.setInclude(tableName.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //生成的字段 是否添加注解，默认false
        strategy.setEntityTableFieldAnnotationEnable(true);
        //是否启用 builder 模式 例：new DevDevice().setDealerId("").setDeviceCode("");
        strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    public static void main(String[] args) {

        startCode("meeting","com.ding.dingitems","dd_schedule",false);
    }
}
