package VO;

import lombok.Data;

//http请求返回的最外层对象Result Object
/*
* GET /sell/buyer/product/list
```

参数

```
无
```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "name": "热榜",
            "type": 1,
            "foods": [
                {
                    "id": "123456",
                    "name": "皮蛋粥",
                    "price": 1.2,
                    "description": "好吃的皮蛋粥",
                    "icon": "http://xxx.com",
                }
            ]
        },
        {
            "name": "好吃的",
            "type": 2,
            "foods": [
                {
                    "id": "123457",
                    "name": "慕斯蛋糕",
                    "price": 10.9,
                    "description": "美味爽口",
                    "icon": "http://xxx.com",
                }
            ]
        }
    ]
}*/
@Data
public class ResultVO<T> {
    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //返回的具体内容，就是一个对象，可以定义为一个泛型
    private T data;
}
