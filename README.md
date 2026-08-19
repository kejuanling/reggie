# 瑞吉外卖（Reggie Takeout）

基于 Spring Boot + MyBatis-Plus 的经典外卖系统课程项目，采用前后端分离架构，包含管理端（Vue2 + Element UI）和移动端（Vant）两套前端。

## 功能模块

### 管理端（backend）
- 员工登录与权限管理
- 分类管理（菜品分类 / 套餐分类）
- 菜品管理（含口味、图片上传）
- 套餐管理（菜品组合）
- 订单管理

### 移动端（front）
- 手机验证码登录
- 菜品浏览与分类筛选
- 购物车
- 下单与订单管理
- 地址簿管理

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7.6、MyBatis-Plus、Druid 连接池 |
| 数据库 | MySQL（`db_reggie.sql`）/ PostgreSQL（`db_reggie_pg.sql`） |
| 管理端 | Vue2 + Element UI + Axios |
| 移动端 | Vant + 原生 JS |
| 部署 | Railway（`railway.json`）/ Render（`render.yaml`）/ Procfile |

## 项目结构

```
reggie/
├── src/main/java/com/itheima/reggie/
│   ├── common/       # 通用类（统一返回 R、全局异常、字段填充、上下文）
│   ├── config/       # MyBatis-Plus / WebMvc 配置
│   ├── controller/   # 控制层（11 个 Controller）
│   ├── dto/          # 数据传输对象（DishDto、SetmealDto）
│   ├── entity/       # 实体类（11 个）
│   ├── filter/       # 登录检查过滤器
│   ├── mapper/       # MyBatis-Plus Mapper 接口
│   ├── service/      # 业务层（接口 + 实现）
│   └── utils/        # 工具类（验证码等）
├── src/main/resources/
│   ├── backend/      # 管理端前端（Vue2 + Element UI）
│   ├── front/        # 移动端前端（Vant）
│   └── application.yml
├── db_reggie.sql     # MySQL 建库脚本
├── db_reggie_pg.sql  # PostgreSQL 建库脚本
└── pom.xml
```

## 快速开始

### 1. 环境要求
- JDK 11+
- MySQL 8.0+（或 PostgreSQL）
- Maven 3.6+

### 2. 初始化数据库
```bash
mysql -u root -p < db_reggie.sql
```

### 3. 修改数据库配置
编辑 `src/main/resources/application.yml`，修改数据库连接信息（支持环境变量注入：`DATABASE_URL`、`DATABASE_USERNAME`、`DATABASE_PASSWORD`）。

### 4. 启动
```bash
mvn spring-boot:run
```

启动后访问：
- 管理端：`http://localhost:8080/backend/index.html`
- 移动端：`http://localhost:8080/front/index.html`

## 核心设计要点

- **统一返回结构**：`R<T>` 封装所有接口响应，配合 `GlobalExceptionHandler` 统一异常处理
- **登录鉴权**：`LoginCheckFilter` 拦截未登录请求，移动端使用手机验证码登录
- **公共字段自动填充**：`MyMetaObjectHandler` 自动填充创建/更新时间与操作人
- **线程上下文**：`BaseContext` 基于 ThreadLocal 保存当前登录用户
- **DTO 组装**：`DishDto` / `SetmealDto` 组装多表查询结果，避免前端多次请求
