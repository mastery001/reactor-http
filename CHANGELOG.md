引言
===
1.0开头的版本都属于测试升级版本,使用http4以上API

CHANGELOG
===

- v1.0.0 

1. HttpRequest实现HttpUriRequest，HttpMessage接口，删除0.*版本的一些不必要的方法  
2. HttpResponseMessage实现HttpResponse 
3. HttpClientFactory为获取org.apache.http.client.HttpClient 
4. 添加HttpExecutor执行器

- v1.0.1

1. HttpRequest添加是否开启日志的方法
2. 修复HttpPostRequest实际发送的是get请求的bug
3. 删除BaseHttpRequest中的ParamBuilder类
4. 修复使用过去线程不安全的HttpClient，导致大量线程出现WAITING，改为使用CloseableHttpClient

- v1.0.2

1. 修复Attempted read from closed stream异常

- v1.0.3

1. 将连接执行完成后释放
2. request自动添加Header：Connection -> close

- v1.0.4

1. 添加Releaseable接口，用于释放request资源 
2. 添加请求参数时当值为空的时候也可以添加
3. 重试策略以接口形式HttpRetryAware
4. 将request和response的关闭开放


	
