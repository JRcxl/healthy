# 基于传智健康进行的拓展

#### 介绍
智能医疗体检系统

#### 软件架构
技术栈：SSM+MySQL+Redis+SpringSecurity+Dubbo+RocketMQ+ECharts+Apache POI+Vue+Flask API


#### 项目描述
针对传统医疗体检系统人工效率低、规则引擎灵活性差等痛点，通过调用大模型API构建智能化引擎，实现后台资源调度算法化与前台服务个性化的双向优化。系统通过分布式架构与混合推荐算法，支持AI健康评估、智能对话、健康推荐、地理位置敏感服务等核心功能，覆盖用户从预约到报告获取的全流程
1.项目采用Maven分模块设计，方便项目的管理和部署。项目分为前台和后台，基于Dubbo实现分布式服务治理。将公共的代码提取作为服务的提供方，前台和后台项目依赖与该公共的项目并作为服务的消费方。\n
2. 前台健康评估模块通过调用百度千帆大模型和Prompt模板对体检数据进行精准分析。采用流式数据传输与实时渲染，降低首屏响应时间。\n
3.使用Redis List缓存用户最近5轮对话历史，结合SimHash算法生成热点问题指纹，通过Hash结构实现相似问题快速响应。采用LRU淘汰策略管理高频问答缓存\n
5. 前台健康推荐模块采用规则引擎与协同过滤混合推荐的方法根据用户各生理指标数据生成个性化食谱以及建议。\n
6.基于RocketMQ异步处理预约请求，利用延迟队列实现错峰资源分配，支持网络异常自动重试。\n
8.协同使用Redis GEO存储用户区域健康画像，实现地理位置敏感推荐。并采用异步更新策略实时更新Redis中的缓存。\n
9.前台体检套餐部分模块采用页面静态化Freemarker，为数据库减压并提高系统运行性能。\n
10.基于Spring Security构建鉴权体系，通过RBAC模型动态拦截未授权请求。\n



#### 部分扩展模块：
![h1](https://github.com/user-attachments/assets/ae6531e0-c603-40a7-9613-2412c7f47d2e)
![h2](https://github.com/user-attachments/assets/0114615e-b62e-45c8-89ba-37ea588ba0e5)
![h3](https://github.com/user-attachments/assets/72ed53bd-ca3a-460a-a166-7d90796b29f9)
![h4](https://github.com/user-attachments/assets/8d6e86c7-df86-4c3d-8eb7-c06998769d04)
![h5](https://github.com/user-attachments/assets/5cb7b2d6-204a-4b21-9849-3d7a365017d8)
