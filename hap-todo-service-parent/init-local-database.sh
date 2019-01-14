#!/usr/bin/env bash

# 获取初始化数据库的jar包
mkdir -p target

if [ ! -f target/hap-liquibase-tools.jar ]
then
    curl http://nexus.saas.hand-china.com/content/repositories/rdc/com/hand/hap/cloud/hap-liquibase-tools/1.0/hap-liquibase-tools-1.0.jar -o target/hap-liquibase-tools.jar
fi

# 初始化项目数据库
java -Dspring.datasource.url="jdbc:mysql://localhost:3306/hap_demo_service_todo?useUnicode=true&characterEncoding=utf-8&useSSL=false" \
    -Dspring.datasource.username=hapdemo \
    -Dspring.datasource.password=handhand \
    -Ddata.init=true -Ddata.drop=true \
    -Ddata.dir=hap-todo-service/src/main/resources \
    -jar target/hap-liquibase-tools.jar

# 追加项目数据信息到hap-user-service数据库中
java -Dspring.datasource.url="jdbc:mysql://localhost:3306/hap_user_service?useUnicode=true&characterEncoding=utf-8&useSSL=false" \
    -Dspring.datasource.username=hapcloud \
    -Dspring.datasource.password=handhand \
    -Ddata.init=true \
    -Ddata.dir=hap-user-service-db \
    -jar target/hap-liquibase-tools.jar