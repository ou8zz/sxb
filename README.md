#SXB_JAVA 接口文档
### 上线版本
* 2016-07-25 上线版本1.0
* 2016-07-28 更新线上版本表结构统一为首字母小写下划线的命名规则，部分没有使用的表已经在表备注中说明。新增用户反馈记录表。 

### 后台结构说明
#####系统接口层级
* com.sxb.controller 1.8接口RESTController
* com.sxb.controller.php 1.7接口RESTCore
* com.sxb.service 服务逻辑处理层
* com.sxb.constant 枚举类
* com.sxb.model 数据库VO实体类
* com.task 自动任务调度处理
* com.util 通用工具类，包括腾讯使用的部分包
* com.tls  TLS校验签名Filter 拦截所有请求做sig校验
* com.qcloud 腾讯云获取视频接口通用接口包

##### 新增了sig签名校验，以下除了/register, /reqjssdk, /user_getinfo, /comment_listget, /replay_detail, /feedback, /*.php之外的所有action都需要进行签名校验后才可访问。
##### 对应的白名单可以在web.xml中whiteList做配置,该配置修改后需要重启服务,签名过滤器com.tls.TlsFilter.java

### 0 RespsonData公共字段说明
| 字段|类型 |选项|说明 |
|---------|---------|---------|---------|
| errorCode | Integer | 必需| 错误码。正常返回0 异常返回560 错误提示561对应errorInfo |
| errorInfo | String | 必需 | 错误信息。正常返回空"" 异常返回错误信息文本 |
| data | Object | 必需 | 返回数据内容。 如果有返回数据，可以是字符串或者数组JSON等等 |
| totalItems | String | 可选 | 返回有data集合的总分页数量。分页显示才会有此结果 |
##### 注：如果接口本身没有数据需要返回，则对象为空，默认errorCode,errorInfo,data是必须会在JSON对象中存在的。

### 1 开始直播
##### 1.1 POST请求URL  http://180.76.147.91:8080/sxb/live/start
##### 1.2 请求包示例
```
{
    "title": "测试直播",
    "cover": "http://aa.com",
    "chat_room_id": "1",
    "av_room_id": 1,
    "host_uid": "86-18516576316",
    "host_avatar": "http://r.plures.net/lg/images/star/live/topbar-logo-large.png",
    "host_username": "用户名",
    "longitude": 1.2,
    "latitude": 2.1,
    "address": "上海"
}·
```
##### 1.3 请求包字段说明:
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| cover | String(128) | 选填 | 封面图片URL地址。  |
| title | String(50) | 选填 | 直播主题。  |
| chat_room_id | String | 必填 | 地理信息。  |
| av_room_id | Integer | 必填 | 地理信息。  |
| host_uid | String | 必填 | 主播uid。  |
| host_avatar | String(128) | 选填 | 头像URL。  |
| host_username | String(50) | 选填 | 用户名。  |
| longtitude | Double | 选填 | 经度。  |
| latitude | Double | 选填 | 纬度。  |
| address | String(100) | 选填 | 地址。  |
##### 1.4 返回包示例
```
{
    "errorInfo": "",
    "errorCode": 0
}
```
## 2 直播列表
##### 2.1 功能说明 获取当前直播列表。
##### 2.2 POST请求URL  http://180.76.147.91:8080/sxb/live/list
##### 2.3 请求包示例
```
{
    "pageIndex": 1,
    "pageSize": 10
}
```
##### 2.4 请求包字段说明
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| pageIndex | Integer | 必填 | 第几页，从1开始。 |
| pageSize | Integer | 必填 | 每页大小，最多不能超过50。  |
##### 2.5 返回示例
```
{"data": [{
      "id": 100016,
      "title": "直播01",
      "cover": "cover",
      "host_uid": "user1003",
      "host_avatar": "1",
      "host_username": "王大锤",
      "longitude": 0,
      "latitude": 0,
      "address": "浦东南路109号",
      "av_room_id": 0,
      "chat_room_id": "1006",
      "admire_count": 0,
      "watch_count": 0,
      "time_span": 0,
      "create_time": "2016-06-21 13:47:34",
      "modify_time": "2016-06-21 13:47:34"
    }
  ],
  "totalItems": "5",
  "errorCode": "0",
  "errorInfo": ""
}
```
##### 2.6 返回包专有字段说明
###### data中字段：
| 字段|类型 |说明 |
|---------|---------|---------|
| data | Array | 直播记录列表，列表的每条记录是一个LiveRecord对象。|
| totalItem | String | 总数。 |
###### data对象说明：
| 字段|类型 |说明 |
|---------|---------|---------|
| host_uid | String | user1003 |
| host_avatar | String | 1 |
| host_username | String | 王大锤 |
| create_time | String | 创建时间。 |
| cover | String(128) | 封面图片URL地址。  |
| title | String(50) | 直播主题。  |
| admire_count | Integer | 点赞人数。 |
| chat_room_id | String | 聊天室ID。 |
| av_room_id | Integer | AV Room ID。 |
| time_span | Integer | 直播时长，单位秒。 |
| watch_count | Integer | 观看人数。 |

### 3 主播心跳包
主播定期心跳上报数据。
##### 3.1 POST请求URL  http://180.76.147.91:8080/sxb/live/host_heartbeat
##### 3.2 请求包示例
```
{
    "host_uid": "user1002",
    "watch_count": 100,
    "admire_count": 10,
	"time_span": 100
}
```
##### 3.3 请求包字段说明
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| host_uid | String | 必填 | 主播uid。  |
| watch_count | Integer | 必填 | 观看人数。 |
| admire_count | Integer | 必填 | 点赞数。  |
| time_span | Integer | 必填 | 直播时长，单位秒。  |

##### 3.4 返回包示例
data返回0表示没有找到需要执行的数据,否则返回在线观看人数
```
{
  "data": 151,	
  "errorCode": "0",
  "errorInfo": ""
}
```

### 4 直播结束
直播结束的时候，修改直播列表记录，并返回本次直播详情。
##### 4.1 POST请求URL  http://180.76.147.91:8080/sxb/live/end
##### 4.2 请求包示例
```
{
    "host_uid": "user1002"
}
```
##### 4.3 请求包字段说明
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| host_uid | String | 必填 | 主播uid。  |

##### 4.4 返回示例
data直播信息
```
{
  "data": {
    "id": 100258,
    "title": "测试直播",
    "cover": "http://aa.com",
    "host_uid": "user1000",
    "host_avatar": "http://r.plures.net/lg/images/star/live/topbar-logo-large.png",
    "host_username": "用户名",
    "longitude": 1.2,
    "latitude": 2.1,
    "address": "上海",
    "av_room_id": 1,
    "chat_room_id": "1",
    "admire_count": 0,
    "watch_count": 0,
    "time_span": 0,
    "create_time": "2016-07-04 10:55:49",
    "modify_time": "2016-07-04 10:55:49"
  },
  "url": "end",
  "errorCode": 0,
  "errorInfo": ""
}
```
##### 4.5 返回包专有字段说明
| 字段|类型 |说明 |
|---------|---------|---------|
|record|Object|直播记录，是一个LiveRecord对象。|


### 5 获取av room ID
获取用户对应的聊天室ID。
##### 5.1 POST请求URL  http://180.76.147.91:8080/sxb/live/get_user_room
##### 5.2 请求包示例
```
{
    "uid": "user1000"
}
```
##### 5.3 请求包字段说明
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| uid | String | 必填 | uid。  |
##### 5.4 返回包示例
```
{
    "data": 20003,
    "errorCode": 0, 
    "errorInfo": ""
}
```
##### 5.5 返回包专有字段说明
| 字段|类型 |说明 |
|---------|---------|---------|
| avRoomId | Integer | AV Room ID。 |


### 6 获取COS签名 
###（目前没有使用该工具包）
##### 获取COS签名，以便上传文件到COS
##### 6.2 POST请求URL  http://180.76.147.91:8080/sxb/live/get_sign
##### 6.3 请求包示例
```
{}
```
##### 6.6 返回包示例
```
{
  "data":"nOTYwODE1NCZ0PTE0NjcwOTcwMTYmcj05NTc0MTcyMTcmdT0mZj0",
  "url": "get_sign",
  "errorCode": 0,
  "errorInfo": ""
}
```
##### 6.6 返回包专有字段说明
| 字段|类型 |说明 |
|---------|---------|---------|
| sign | String | 签名。 |


### 7 注册用户信息
##### POST请求URL  http://180.76.147.91:8080/sxb/user/register
##### 请求包字段说明
| 字段 | 类型  | 选项 | 说明  |
|---------|---------|---------|---------|
| userphone | String | 必填 | 登录ID，不一定是手机号码  |
| username | String | 必填 | 用户昵称，用户可以自己修改，现在可以重名  |
| password | String | 必填 | 登录密码  |
| atype | String | 必填 | 登录账号类型，目前枚举： PHONE("手机"),QQ("腾讯QQ"),WX("微信") |
| headimagepath | String | 非必填 | 用户头像路径  |
| phoneno | String | 非必填 | 手机号码  |
| unionno | String | 非必填 | 联合账号  |
| deviceinfo | String | 非必填 | 设备信息  |
| ipaddress | String | 非必填 | 登录IP  |
| logininfo | String | 非必填 | 登录成功或异常信息    |

##### 请求包示例
```
{
	"userphone":"1589336622", 
	"username":"user_001", 
	"password":"pwe",
	"atype":"PHONE",
	"phoneno":"1589336622",
	"headimagepath":"http://www.qq.com/test.jpg",
	"unionno":"weixinaccount",
	"deviceinfo":"iphone6plus",
	"ipaddress":"180.15.222.85",
	"logininfo":"登录成功"
}
```
##### 返回包示例
data返回用户详细信息
errorCode返回0表示成功，560为后台异常，561为手机已存在
```
{
  "data": {
    "userphone": "15893366221",
    "username": "user_001",
    "password": "pwe",
    "headimagepath": "http://www.qq.com/test.jpg",
    "praisenum": 0,
    "personal_signature": "",
    "getattentions": 0,
    "payattentions": 0,
    "sex": "0",
    "address": "",
    "constellation": "",
    "online": 0,
    "atype": "PHONE",
    "phoneno": "1589336622",
    "unionno": "weixinaccount",
    "remark": null,
    "deviceinfo": null,
    "ipaddress": null,
    "logininfo": null,
    "signature": null
  },
  "url": "register",
  "errorCode": 561,
  "errorInfo": "userphone '158933662211' is existed"
}
```

### 8 编辑用户信息
##### POST请求URL  http://180.76.147.91:8080/sxb/user/user_modifyfields
##### 请求包字段说明
| 字段|类型 | 选项 | 说明  |
|---------|---------|---------|---------|
| userphone | String | 必填 | 登录ID，不一定是手机号码  |
| username | String | 必填 | 用户昵称，用户可以自己修改，但是后台会逻辑判断唯一性，不能重名  |
| atype | String | 必填 | 登录账号类型，目前枚举： PHONE("手机"),QQ("腾讯QQ"),WX("微信");  |
| phoneno | String | 必填 | 手机号码  |
##### 请求包示例
```
{
	"userphone":"1589336622", 
	"username":"username_001", 
	"atype":"PHONE",
	"phoneno":"1589336622"
}
```
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据   
errorCode返回0表示成功，560为后台异常，561为手机已存在，562为用户名已存在
```
{
    "data": 1,
    "errorCode": 0, 
    "errorInfo": ""
}
```

### 9 获取用户信息
##### POST请求URL  http://180.76.147.91:8080/sxb/user/user_getinfo
##### 请求包示例
```
{
	"userphone":"1589336622"
}
```
##### 返回包示例
errorCode返回0表示成功，560为后台异常，561为找不到该信息
```
{
  "data": {
    "userphone": "18621577368",
    "username": "18621577368",
    "password": "123456",
    "headimagepath": "http://o9idyqp20.bkt.clouddn.com/head_image-1469090570782-18621577368_head.jpg",
    "praisenum": 0,
    "personal_signature": "",
    "getattentions": 0,
    "payattentions": 0,
    "sex": "0",
    "address": "",
    "constellation": "",
    "online": 0,
    "atype": "PHONE",
    "phoneno": "18621577368",
    "unionno": null,
    "remark": null,
    "signature": null
  },
  "url": "user_getinfo",
  "errorCode": 0,
  "errorInfo": ""
}
```

### 10 上传用户头像
##### POST请求URL  http://180.76.147.91:8080/sxb/user/image_post
##### 请求包示例
文件类型分为分别为：head_image:用户头像,cover_image:封面
```
{
	"file":"文件格式内容",
	"imagetype":"head_image",
	"userphone":"13588885555"
}
```
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据
```
{
  "data": "http://o9idyqp20.bkt.clouddn.com/head_image-1467190635317-Chrysanthemum.jpg",
  "url": "image_post",
  "errorCode": 0,
  "errorInfo": ""
}
```

### 11 获取用户头像
##### GET请求URL  http://180.76.147.91:8080/sxb/user/image_get?imagepath=img.jpg&width=300&height=300
##### 【原本请求的地址修改为七牛云平台存储，所以直接通过访问下面的地址进行访问：】
##### GET请求七牛云URL http://o9idyqp20.bkt.clouddn.com/head_image-1467178670840-1111.png?imageView2/2/w/300/h/300/interlace/0/q/100
##### 返回示例
```
{图片资源}
```


### 12 获取直播视频列表
##### POST请求URL  http://180.76.147.91:8080/sxb/replay/replay_getbytime
##### 请求包示例
```
{
	"begin_time":"2016-06-29"
}
```
##### 返回包示例
```
{"data": [{
      "programid": 16214,
      "userphone": "15651780625",
      "subject": "潮极app录播测试",
      "praisenum": 0,
      "coverimagepath": "",
      "state": 1,
      "groupid": "@TGS#3KDUQFBED",
      "begin_time": 1466667667000,
      "url": "",
      "addr": "",
      "duration": 27668000,
      "replayid": "200012747_6b07139d9c5c40c6990747b2fa686024\r\n"
    }
  ],
  "url": "replay_getbytime",
  "errorCode": "0",
  "errorInfo": ""
}
```

### 13 保存·直播视频
##### POST请求URL  http://180.76.147.91:8080/sxb/replay/replay_create
##### 请求包示例
```
{
	"subject":"title", 
	"programid":12220, 
	"userphone":"1523655566", 
	"groupid":"SE14PE", 
	"replayid":1005, 
	"duration":"2016-06-30 00:00:00", 
	"coverimagepath":"http://o9idyqp20.bkt.clouddn.com/head_image-1467178670840-1111.png?imageView2/2/w/300/h/300/interlace/0/q/100"
}
```
##### 请求包字段说明:
| 字段 | 类型 | 选项 | 说明 |
|---------|---------|---------|---------|
| subject | String | 必填 | 直播名称。  |
| programid | Integer | 必填 | 房间号。  |
| userphone | String | 必填 | 用户ID。  |
| groupid | String | 必填 | 用户组。  |
| replayid | String | 必填 | 视频唯一ID。  |
| duration | String | 必填 | 视频时长。  |
| coverimagepath | String | 必填 | 封面图片。  |
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据
```
{
  "data": 1,
  "url": "replay_create",
  "errorCode": "0",
  "errorInfo": ""
}
```

### 14 创建评论
##### POST请求URL  http://180.76.147.91:8080/sxb/comment/comment_create
##### 请求包示例
```
 {
 	"userphone":"1555223554", 
 	"article_uuid":"auid100", 
 	"content":"neirong"
 }
 ```
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据
```
{
    "data": 1,
    "errorCode": 0, 
    "errorInfo": ""
}
```

### 15 获取评论或者评论列表
##### POST请求URL  http://180.76.147.91:8080/sxb/comment/comment_listget
##### 请求包示例
```
{
	"article_uuid":100, 
	"userphone":"15966636322",
	"limit":10, 
	"offset":1
}
```
##### 返回包示例
```
{
  "data": {
    "total": 0,
    "favorTotal": 0,
    "comments": []
  },
  "url": "comment_listget",
  "errorCode": "0",
  "errorInfo": ""
}
```

### 16 设置用户点赞
##### POST请求URL  http://180.76.147.91:8080/sxb/comment/comment_favor_set
##### 请求包示例
```
{
	"userphone":"18625155246", 
	"comment_uuid":15500, 
	"favor":true
}
```
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据
```
{
  "data": 1,
  "url": "comment_favor_set",
  "errorCode": "0",
  "errorInfo": ""
}
```

### 17 JAVA获取微信签名
##### GET请求URL  http://180.76.147.91:8080/sxb/tools/reqjssdk?callback=callback&url=http://demo.com
##### 请求包示例
get请求
##### 返回包示例
跨域返回格式：
```
callback(
	{
		"timestamp":"1466587027",
		"appId":"wx163dcc33797eed26",
		"nonceStr":"e03b6763-d2d9-4f8e-b9c7-d8a3d1566ecb",
		"jsapi_ticket":"sM4AOVdWfPE4DxkXGEs8VIAdV_7jD5L8A7HCNkZjq1FOlchH55fQlugfk7AKznUVWTM8sALuInH3yu2adyU2ew",
		"signature":"66dc119db4af91bcb0935885eb6954e153e90c6c",
		"url":"http://180.76.147.91:8080/tools/reqjssdk.php"
	}
)
```

### 18 获取七牛云上传文件的key and secret
实现文件资源上传直接到七牛云不经过服务器
实现文件上传API：http://developer.qiniu.com/resource/official.html
##### POST请求URL  http://180.76.147.91:8080/sxb/user/get_uploadinfo
##### 请求包示例
文件类型分为分别为：head_image:用户头像,cover_image:封面
```
{
	"name":"demo.jpg",
	"imagetype":"head_image",
	"userphone":"13588885555"
}
```
##### 返回包示例
```
{
  "data": {
    "name": "head_image-1467685660698-demo.jpg",
    "prefix": "http://o9idyqp20.bkt.clouddn.com/",
    "uploadToken": "nnS_nK-KJODc_LEyBgNBT4FaN8M5GyJn-xpd-Gce:AYPA-h7lvYYDwjGClKSR4T5nLqY=:eyJzY29wZSI6InN4Yi1yZXNvdXJjZSIsImRlYWRsaW5lIjoxNDY3Njg5MjYwfQ=="
  },
  "url": "get_uploadinfo",
  "errorCode": 0,
  "errorInfo": ""
}
```

### 19 七牛云上传文件的Callback处理
##### POST请求URL  http://180.76.147.91:8080/sxb/user/upload_callback
##### 请求包示例
文件类型分为分别为：head_image:用户头像,cover_image:封面
```
{
	"name":"head_image-1467685660698-demo.jpg",
	"imagetype":"head_image",
	"userphone":"13588885555"
}
```
##### 返回包示例
```
{
  "url": "upload_callback",
  "errorCode": 0,
  "errorInfo": ""
}
```
		
### 20 根据VID获取视频的详细信息
##### POST请求URL  http://180.76.147.91:8080/sxb/replay/replay_detail
##### 后续因为后台需要上传视频文件而VID取不到上传的视频文件，新增该方法VID传参规则为@file文件为上传文件操作方式，原VID获取逻辑不变。
##### 请求包示例
```
{ "vid":"200012747_fdb0ed7f3a3c4547a5c3b63435e2f110" }
```
##### 上传文件请求示例
```
{ "vid":"20160722@file" }
```
##### 返回包示例
```
{
  "data": {
    "image_url": "http://p.qpic.cn/videoyun/0/200012747_fdb0ed7f3a3c4547a5c3b63435e2f110_1/640",
    "url": "http://200012747.vod.myqcloud.com/200012747_fdb0ed7f3a3c4547a5c3b63435e2f110.f0.mp4"
  },
  "url": "replay_detail",
  "errorCode": 0,
  "errorInfo": ""
}
```

### 21 收集用户提交的反馈
##### POST请求URL  http://180.76.147.91:8080/sxb/comment/feedback
##### 请求包示例
如果用户在未登录的状态下进行提交userphone可以为空
```
{
	"userphone":"18625155246", 
	"content":"这个APP还有XXX需要改进的地方"
}
```
##### 返回包示例
data返回1表示成功，0表示没有找到需要执行的数据
```
{
  "data": 1,
  "url": "feedback",
  "errorCode": 0,
  "errorInfo": ""
}
```

