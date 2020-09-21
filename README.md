### mybatis-plus-plus（mpp）

对mybatis-plus进行增强，支持多表关联查询

使用示例 demo https://github.com/nullbull/mybatis-plus-plus-demo

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

3. 配置方式

    - SpringBoot 配置

      ```java
      @Configuration
      public class MybatisPlusConfig {
      
          @Bean
          public Interceptor myInterceptor() {
              return new MyInterceptor();
          }
      
          @Bean
          public ISqlInjector multiQueryInjector() {
              return new MultiQueryInjector();
          }
      
      }
      ```

      

    - Spring 配置方式

      1. 在Spring初始化中修改MybatisSqlSessionFactoryBean 
      2. 添加 mpp 自定义的 mybatis 拦截器
          ```java
          MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();

          sqlSessionFactoryBean.setPlugins(new Interceptor[]{ new MyInterceptor()});
          ```

      3. 添加 mpp 自定义的  默认方法
          ```java
          GlobalConfig globalConfig = GlobalConfigUtils.defaults();
          globalConfig.setSqlInjector(new MultiQueryInjector());
          sqlSessionFactoryBean.setGlobalConfig(globalConfig);
          ```

4. 将mybaits plus 的BaseMapper 替换成mpp自定义的 MultiQueryMapper

### 如何使用

示例一：

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

示例二：

```java
List<Object> meetingList = meetingMapper.selectMulti(new Query(Meeting.class)
           			.joinOn(F.f(Meeting::getHostId), F.f(Room::getId))
                    .eq(F.f(Room::getName), "测试会议室")
                    .end());
```



生成的SQL

```sql
select meeting.id,meeting.meeting_id,meeting.host_id,meeting.topi,meeting.type,meeting.created_time from meeting join room on meeting.host_id = room.id and room.name=?
```



