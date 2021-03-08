### 参考链接
https://projects.spring.io/spring-security-oauth/docs/oauth2.html

### grant_type = "password" 模式
post请求 /oauth/token 获取token
<br />
参数形式：
* Specify the client_id and client_secret in the header using base64 encoding.
* Next specify username,password,scope, and the grant type as Password Grant in body and send the request.
