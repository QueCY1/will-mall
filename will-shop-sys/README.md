# 工具
+ `IpHelper` 用来获取 ip，通过 `HttpServletRequest request = HttpContextUtils.getHttpServletRequest();` 方式
+ `SpringContextUtils` 用来获取 Spring 上下文，注意需要实现 ApplicationContextAware，当一个类实现了 ApplicationContextAware 接口并被 Spring 容器管理时，在容器初始化过程中，会自动调用该类的 setApplicationContext 方法，并将应用上下文对象作为参数传递进来。

# TokenStore
+ 有三部分：`uid_to_access`、`token access`、`token refresh` 
+ `uid_to_access`: 是一个集合，里面每个元素存储 1:1 的 accessToken 和 refreshToken，该 key 的设置有过期时间，代表一次登录获取新令牌后可以操作的最大时间。该集合中的元素可能与 redis 中对应的另外两个 token 不一致，因为另外两个 token 过期之后，redis 中就不会存在这个键，这里保证数据一致性是通过每次登录获取新令牌时，同步该集合中的数据
+ `token access`: 
+ `token refresh`: 
+ 使用 `redisTemplate.executePipelined` 管道技术来同时在 redis 中设置三个键，并设置跟系统相关的过期时间

# 系统模块
## 系统用户
+ 系统用户根据店铺 `shop_id` 进行区分，每个店铺都可以拥有管理员权限，相当于店铺店长的权限
+ 系统中并非所有权限都归店长所有，店长的角色次于平台提供者的权限
+ 单用户对应多个角色
+ 系统用户登录前端会经历 AES 对称加密，后端会将密码解密，该工具是 hutool 提供的，解密后得到明文再与数据库中的不可逆加密密码（哈希）进行匹配 `match`，使用 Spring Security 中的 `passwordEncoder`

## 