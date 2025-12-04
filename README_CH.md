# **智能产品追踪系统 - Spring Boot 应用**

**语言:**
- [English](README.md)
- [中文 (Chinese)](README_CH.md)

一个全面的产品追踪和管理系统，使用 Spring Boot 构建，具有用户认证、产品管理和分析功能。

## **🚀 快速开始**

### **先决条件**

- **Java 17 或 21** (LTS 版本)
- **Maven 3.8+** 或 **Gradle 7.x+**
- **PostgreSQL 14+**
- **Git**
- **IDE** (IntelliJ IDEA, VS Code 或 Eclipse)

### **1. 克隆仓库**

```bash
git clone https://github.com/noumanahmad448/smart-product-tracker.git
cd smart-product-tracker
```

### **2. 数据库设置**

#### **选项 A: 使用 Docker (推荐)**
```bash
# 使用 Docker 启动 PostgreSQL
docker run --name smart-tracker-db \
  -e POSTGRES_DB=smart_product_tracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15-alpine

# 或者使用 docker-compose (如果存在 docker-compose.yml)
docker-compose up -d
```

#### **选项 B: 手动 PostgreSQL 设置**
```sql
-- 连接到 PostgreSQL
psql -U postgres

-- 创建数据库
CREATE DATABASE smart_product_tracker;

-- 创建用户 (可选)
CREATE USER smart_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE smart_product_tracker TO smart_user;

-- 验证
\l  -- 列出数据库
```

### **3. 配置应用**

复制示例配置文件并更新您的设置：

```bash
# 复制配置模板
cp src/main/resources/application-example.yml src/main/resources/application.yml

# 编辑配置
nano src/main/resources/application.yml
```

**更新这些关键设置：**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smart_product_tracker
    username: postgres      # 如果使用不同用户请修改
    password: password      # 修改为您的密码
    
  jpa:
    hibernate:
      ddl-auto: update     # 生产环境使用 'validate'
```

### **4. 构建应用**

#### **使用 Maven:**
```bash
# 清理并构建
./mvnw clean compile

# 运行测试
./mvnw test

# 构建 JAR 文件
./mvnw clean package

# JAR 文件位置: target/smart-product-tracker-0.0.1-SNAPSHOT.jar
```

#### **使用 Gradle:**
```bash
# 构建和测试
./gradlew build

# 运行测试
./gradlew test

# 构建 JAR
./gradlew bootJar
```

### **5. 运行应用**

#### **选项 A: 使用 Maven/Gradle 运行**
```bash
# Maven
./mvnw spring-boot:run

# Gradle
./gradlew bootRun
```

#### **选项 B: 直接运行 JAR**
```bash
# 构建 JAR 后
java -jar target/smart-product-tracker-0.0.1-SNAPSHOT.jar

# 使用自定义配置文件
java -jar target/smart-product-tracker-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=dev
```

#### **选项 C: 开发模式运行 (自动重载)**
```bash
# 安装 Spring Boot DevTools 实现热重载
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### **6. 验证安装**

1. **检查健康端点：**
   ```bash
   curl http://localhost:8080/api/health
   # 预期: "Smart Product Tracker is running!"
   ```

2. **访问 Swagger UI：**
   打开浏览器: http://localhost:8080/swagger-ui/index.html

3. **检查 Actuator 端点：**
   - 健康: http://localhost:8080/actuator/health
   - 信息: http://localhost:8080/actuator/info
   - 指标: http://localhost:8080/actuator/metrics

## **📁 项目结构**

```
smart-product-tracker/
├── src/main/java/com/smarttracker/product/
│   ├── config/              # 配置类
│   │   ├── SecurityConfig.java
│   │   ├── DatabaseConfig.java
│   │   └── OpenApiConfig.java
│   ├── controller/          # REST 控制器
│   │   ├── AuthController.java
│   │   ├── ProductController.java
│   │   └── HealthController.java
│   ├── service/            # 业务逻辑
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   └── impl/           # 实现类
│   ├── repository/         # 数据访问层
│   ├── model/             # JPA 实体
│   ├── dto/               # 数据传输对象
│   ├── exception/         # 自定义异常
│   └── validation/        # 自定义验证器
├── src/main/resources/
│   ├── application.yml     # 主配置
│   ├── application-dev.yml # 开发配置
│   ├── application-prod.yml# 生产配置
│   └── db/
│       └── migration/      # 数据库迁移
├── src/test/              # 测试
└── pom.xml / build.gradle # 构建配置
```

## **🔧 配置配置文件**

### **开发配置文件**
```bash
# 使用 dev 配置文件运行
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 启用的功能：
# - H2 内存数据库 (可选)
# - 详细日志
# - 禁用安全以方便测试
# - 自动数据库创建
```

### **生产配置文件**
```bash
# 使用生产设置运行
java -jar app.jar --spring.profiles.active=prod

# 功能：
# - PostgreSQL 数据库
# - 增强安全
# - 性能优化
# - 外部化配置
```

### **配置文件**
- `application.yml` - 基础配置
- `application-dev.yml` - 开发覆盖配置
- `application-prod.yml` - 生产覆盖配置
- `application-test.yml` - 测试配置

## **🧪 测试**

### **运行所有测试**
```bash
# Maven
./mvnw test

# Gradle
./gradlew test
```

### **运行特定测试**
```bash
# 运行单个测试类
./mvnw test -Dtest=AuthControllerTest

# 运行特定名称模式的测试
./mvnw test -Dtest="*ServiceTest"

# 运行并生成覆盖率报告
./mvnw test jacoco:report
# 报告位置: target/site/jacoco/index.html
```

### **集成测试**
```bash
# 使用测试配置文件运行（使用测试数据库）
./mvnw test -Dspring.profiles.active=test

# 使用 Testcontainers 运行
./mvnw verify
```

## **🌐 API 文档**

### **Swagger UI**
- **URL:** http://localhost:8080/swagger-ui/index.html
- **功能：**
  - 交互式 API 文档
  - 直接测试端点
  - 模型架构
  - 认证测试

### **OpenAPI 规范**
- **JSON:** http://localhost:8080/v3/api-docs
- **YAML:** http://localhost:8080/v3/api-docs.yaml

### **Postman 集合**
```bash
# 生成 OpenAPI 规范
./mvnw springdoc:generate

# 导入到 Postman：
# 1. 打开 Postman
# 2. 导入 → 链接
# 3. 粘贴: http://localhost:8080/v3/api-docs
```

## **📦 数据库迁移**

### **初始设置**
```bash
# 数据库将在首次运行时自动创建
# 在 application.yml 中设置：
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 用于开发
      
# 生产环境使用 Flyway 或 Liquibase
```

### **重置数据库**
```bash
# 删除并重新创建（仅限开发）
DROP DATABASE smart_product_tracker;
CREATE DATABASE smart_product_tracker;

# 或使用 JPA 重新创建
spring.jpa.hibernate.ddl-auto=create-drop
```

## **🔐 默认用户和认证**

### **初始管理员用户**
首次运行时，系统创建默认管理员用户：
- **用户名:** admin
- **密码:** admin123
- **邮箱:** admin@smarttracker.local

**首次登录后请立即修改！**

### **注册端点**
```bash
# 注册新用户
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "youruser",
    "email": "your@email.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "firstName": "Your",
    "lastName": "Name"
  }'
```

## **🐳 Docker 部署**

### **构建 Docker 镜像**
```bash
# 构建镜像
docker build -t smart-product-tracker:latest .

# 或使用 Maven
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=smart-product-tracker

# 运行容器
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/smart_product_tracker \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  smart-product-tracker:latest
```

### **Docker Compose (一体化)**
```bash
# 启动整个堆栈
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止所有服务
docker-compose down

# 停止并删除卷
docker-compose down -v
```

## **🚨 故障排除**

### **常见问题**

#### **1. 端口 8080 已被占用**
```bash
# 检查占用端口 8080 的进程
sudo lsof -i :8080

# 终止进程
sudo kill -9 <PID>

# 或在不同端口运行
./mvnw spring-boot:run -Dserver.port=8081
```

#### **2. 数据库连接失败**
```bash
# 检查 PostgreSQL 是否运行
sudo systemctl status postgresql

# 启动 PostgreSQL
sudo systemctl start postgresql

# 测试连接
psql -h localhost -U postgres -d smart_product_tracker
```

#### **3. Java 版本问题**
```bash
# 检查 Java 版本
java -version

# 应显示 Java 17 或 21
# 如果版本错误，设置 JAVA_HOME
export JAVA_HOME=/path/to/java17
```

#### **4. 构建失败**
```bash
# 清理并重新构建
./mvnw clean compile

# 跳过测试
./mvnw clean install -DskipTests

# 更新依赖
./mvnw versions:display-dependency-updates
```

### **日志和调试**
```bash
# 查看应用日志
tail -f logs/application.log

# 启用调试日志
# 添加到 application.yml：
logging:
  level:
    com.smarttracker: DEBUG
    org.springframework: DEBUG

# 访问 Actuator 日志端点
curl http://localhost:8080/actuator/loggers/com.smarttracker
```

## **📈 监控**

### **Spring Boot Actuator 端点**
- **健康:** `GET /actuator/health`
- **信息:** `GET /actuator/info`
- **指标:** `GET /actuator/metrics`
- **环境:** `GET /actuator/env`
- **Beans:** `GET /actuator/beans`
- **映射:** `GET /actuator/mappings`

### **Prometheus 指标**
```bash
# 在 application.yml 中启用
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  metrics:
    export:
      prometheus:
        enabled: true

# 访问指标
curl http://localhost:8080/actuator/prometheus
```

## **🔧 开发提示**

### **IDE 设置**
#### **IntelliJ IDEA:**
1. 作为 Maven/Gradle 项目打开
2. 启用注解处理 (Lombok)
3. 安装 Spring Boot 插件
4. 配置运行配置：
   - 主类: `com.smarttracker.product.SmartProductTrackerApplication`
   - 活动配置文件: `dev`

#### **VS Code:**
1. 安装扩展：
   - Java Extension Pack
   - Spring Boot Extension Pack
   - Lombok Annotations Support
2. 打开文件夹并信任项目
3. 使用 Spring Boot Dashboard

### **代码质量**
```bash
# 检查代码风格
./mvnw checkstyle:check

# 运行 PMD 进行静态分析
./mvnw pmd:check

# SpotBugs 检查错误模式
./mvnw spotbugs:check
```

## **🤝 贡献**

1. Fork 仓库
2. 创建功能分支：`git checkout -b feature/amazing-feature`
3. 提交更改：`git commit -m 'Add amazing feature'`
4. 推送到分支：`git push origin feature/amazing-feature`
5. 打开 Pull Request

### **提交消息约定**
```
feat: 添加用户注册端点
fix: 解决密码验证问题
docs: 更新 API 文档
style: 根据指南格式化代码
refactor: 改进服务层结构
test: 添加认证服务单元测试
chore: 更新依赖
```

## **📄 许可证**

本项目基于 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## **📞 支持**

- **问题:** [GitHub Issues](https://github.com/noumanahmad448/smart-product-tracker/issues)
- **文档:** [Wiki](https://github.com/noumanahmad448/smart-product-tracker/wiki)
- **邮箱:** support@smarttracker.com

---

**编码愉快！ 🚀**

*如果遇到任何问题，请查看故障排除部分或在 GitHub 上提交问题。*