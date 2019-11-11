# AndroidLearning
Question:除了SD卡文件共享的方式，App2如何实现读取和修改App1的数据？

Answer：实际上这是android中Process通信的问题，主要的方式有：隐式Intent、Content Provider、AIDL、Socket通信。自己实现了下，基本上都可以。

具体来说，新建了ShareApp1和ShareApp2，后者需要读取和修改前者的数据，具体实现：

1.隐式Intent：
实验了两种方式.第一种，ShareApp2通过隐式Intent（不放Bundle）打开ShareApp1的Activity，ShareApp1根据传来的Intent的Category判断是读取还是修改并执行相应逻辑，
通过setResult返回结果，ShareApp1onActivityResult中读取结果。第二种，ShareApp2通过隐式Intent（放入Bundle），发送BroadCast，ShareApp1在onReceive中读取Intent，
执行相应逻辑后同样通过隐式Intent将结果发给ShareApp2.

2.ContentProvider: 在ShareApp1中实现Content Provider，ShareApp2通过ContentResolver和Uri进行CRUD.

3.AIDL: 在ShareApp1中新建AIDL IReadWrite，并写要暴露的接口，然后在ShareApp1中新建Service，在Service中继承IReadWrite内的Stub，并重写其中的方法，
实现具体的读写逻辑.将ShareApp1中的接口复制到ShareApp2中(包名要相同),并bind ShareApp1中的Service.通过Service的onBind获取返回的Binder，
并通过AIDL的Stub的asInterface转换为IReadWrite接口类型.最后直接调用相应函数即可.

4.Socket通信：在ShareApp1中开启线程运行Socket Server，绑定指定端口，并等待ShareApp2连接.当和ShareApp2建立连接后，就可以开始通信，根据客户端发来的消息执行相应逻辑，并将结果通过Socket发回ShareApp2.

优缺点比较：

1.隐式Intent（Bundle）：
优点：简单易用
缺点：只能传输简单的数据类型
场景：一般适用于四大组件之间的进程通信

2.ContentProvider:
优点：易用，在数据源访问方面功能强，支持一对多并发数据共享
缺点：底层由AIDL实现，功能上有一些限制
场景：一对多的进程数据共享

3.AIDL：
优点：功能强大，支持一对多并发通信
缺点：使用稍微复杂，需要处理线程同步
场景：一对多通信且有RPC需求

4.Socket：
优点：功能强大，可跨网络通信
缺点：实现细节稍微复杂
场景：跨网络通信
