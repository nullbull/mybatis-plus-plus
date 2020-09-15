### mybatis-plus-plus（mpp）

对mybatis-plus进行增强，支持多表关联查询



### 接入步骤

1. 将代码down到本地，使用maven install

2. 在自己的项目中添加依赖

   ```xml
   <dependency>
       <groupId>com.github.mybatisplus</groupId>
       <artifactId>plus</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

3.  修改 MybatisSqlSessionFactoryBean 

   1. 添加 mpp 自定义的 mybatis 拦截器

      ```java
      MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
      
      sqlSessionFactoryBean.setPlugins(new Interceptor[]{ new MyInterceptor()});
      ```

   2. 添加 mpp 自定义的  默认方法

      ```java
      GlobalConfig globalConfig = GlobalConfigUtils.defaults();
      globalConfig.setSqlInjector(new MultiQueryInjector());
      sqlSessionFactoryBean.setGlobalConfig(globalConfig);
      ```

4. 将mybaits plus 的BaseMapper 替换成mpp自定义的 MultiQueryMapper

### 如何使用

```java
        Query end = new Query(TestVo.class)
                .select(F.f(Meeting::getId), F.f(Room::getId), F.f(Meeting::getTopic))
                .joinOn(F.f(Meeting::getHostId), F.f(Room::getId))
                .in(F.f(Meeting::getId), 1, 2)
                .eq(F.f(Meeting::getTopic), "测试")
                .end();
        List<Object> list = meetingMapper.selectMulti(end);

```

生成的sql

```sql
select meeting.id,room.id,meeting.topic FROM meeting join room on meeting.host_id = room.id where meeting.id in ( ?,? ) and meeting.topic=?
```





