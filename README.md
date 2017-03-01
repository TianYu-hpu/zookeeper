# ZooKeeper 学习

##  认识 ZooKeeper 

### 概述
   分布式协调服务，是一个高性能的分布式数据一致性解决方案
    
####要点
#####1.源代码开放    
#####2.它解决分布式数据一致性问题        
   A. 顺序一致性    
   B. 原子性    
   C. 单一视图    
   D. 可靠性    
   E: 实时性    
#####3.高性能    
#####4.通过 ZooKeeper 提供的接口来解决分布式应用中的实际问题    
####应用场景
 1.数据发布/订阅    
　　顾名思义就是一方把数据发布出来，另一方通过某种收单得到这些数据    
#####数据订阅有两种方式
推模式：服务器主动向客户端推送消息    
拉模式：客户端主动去服务端获取数据（通常采用轮询的方式）    
　　ZK采用两种方式相结合，发布者将数据发布到 ZK 集群节点上，订阅者通过一定的方法告诉服务器，我对哪个节点的数据感兴趣，那服务器在这些节点的数据发生变化时，就通知客户端，客户端得到通知后可以去服务器获取数据信息
     
#####负载均衡
　　数据库在 ZK 集群中注册成一个节点，这些节点可以是临时的，也可以是永久的，在读取数据的时候，ZK 可以得到所有的数据库连接
#####命名服务
　　提供名称的服务，例如数据库表格 ID,一般用的比较多的有两种ID,一种是自动增长的ID,一种是  UUID,两种ID都有缺陷，自动增长的ID局限在单库单表中使用，不能再分布式中使用，UUID 可以在分布式中使用但是由于 ID 没有规律难于理解，我们可以借用 ZK 来生成一个顺序增长的，可以在集群中使用的，命名易于理解的 ID
#####分布式协调/通知
　　在分布式系统中，我们常常需要某个机器是否可用，在传统开发中，可以通过 Ping 某个主机来实现， Ping 得到说明对方是可用的，相反是不可用的， ZK 中我们让所有的机器注册为临时节点，我们判断一个机器是否可用，我们只需要判断这个节点在 ZK 中是否存在就可以了，不需要直接去链接需要检查的机器，降低系统的复杂度

#####ZK 优势
　　源代码开放  
　　已经被证实是高性能，易用稳定的工业级产品  
　　有着广泛的易用， Hadoop, HBase,Storm Solr  


###ZK 基本概念
#####集群角色
　　Leader/ Follower / Observer    
　　Leader 服务器是整个 ZK 集群工作机制中的核心    
　　Follower 服务器是 ZK 集群状态的跟随者    
　　Observer 服务器充当一个观察者的角色    

　　Leader , Fllower 设计模式    
　　Observer 观察者模式    
#####会话
　　客户端与 ZK 服务器的链接，ZK 中的会话叫做 Session, 客户端与服务器建立一个 TCP 的长连接来维持一个 Session,客户端在启动的时候首先会与服务器建立一个 TCP 连接，通过这个连接，客户端能够通过心跳检测与服务器保持有效的回话，也能向 ZK 服务器发送骑牛并获得响应
    
#####数据节点
　　集群中的一台机器成为一个节点    
　　数据模型中的数据单元 Znode, 分为持久节点和临时节点    
　　ZK 的数据模型是一个树，树的节点就是 Znode, Znode 中可以保存信息    

    /    
	|    
	|-------node_1----------|
	|						|
	|						|
	|-------node_2			|----------node_1_1
	|						|
	|						|
	|-------node_3			|----------node_1_2
	|						|
	|						|
	|-------node_4			|
	|						|----------node_1_3
	|
####版本
	
	|版本类型			|说明						|    
	| ----------------  | -------------------------:|    
	|version			|当前数据节点数据内容的版本号	|    
	|cversion			|当前数据接单子节点的版本号		|    
	|aversion			|当前数据节点ACL变更版本号		|   

      
#####watcher
　　ZK 允许用户在制定节点上注册一些 watcher,当数据节点发生变化的时候， ZK 服务器会把这个变化的通知发送给感兴趣的客户端
                 
#####ACL 权限控制
　　ZK 采用 ACL 策略来进行权限控制，有一些权限
　　Create:创建子节点的权限
　　Read:读取节点数据和子节点列表的权限
　　Write:更新节点数据的权限
　　Delete:删除子节点的权限
　　Admin:设置节点 ACL 的权限
####环境搭建
　　三个虚拟器地址    
　　192.168.1.105    
　　192.168.1.106    
　　192.168.1.107    
　　设置连接模式为桥接模式
  zookeeper.apache.org 下载安装包
  配置 JAVA_HOME
  cd conf，cp zoo_sample.cfg     zook.cfg    zoo_sample.cfg 是 ZK 的样例配置文件
  //ZK 存储快照文件的目录，改成 /var/zookeeper
  dataDir=/var/zookeeper
  //默认服务器向外界提供服务的端口号
  clientPort=2181

####配置服务器
server.id=host:port:port    server.1=192.168.1.105:leader 服务器端口：follower服务器端口    
server.1=192.168.1.105:2888:3888  
server.2=192.168.1.106:2888:3888  
server.3=192.168.1.107:2888:3888  
保存退出  
scp 文件到其他集群中的服务器  
cd /var/zookeeper  
vim myid 创建一个这样的文件，在这里面写id,这个id,就是 server.id后面的id  
同样在其他集群中的服务器中也要做这样的操作  
启动zkserver.sh来启动服务器  
测试是否启动 telnet 主机地址 端口号          端口号就是clientPort  
启动服务器集群中的其他服务器，链接之后输入 stat 命令，当集群中有一半的机器启动成功，集群就能使用  

伪集群：  
server.1=192.168.1.105:2888:3888  
server.2=192.168.1.105 :12888:13888  
server.3=192.168.1.105 :22888:23888  
单机：  
server.1=192.168.1.105:2888:3888  

##ZooKeeper 实战
  ./zkcli.sh -timeout ms -r -server ip:port  
 timeout 选项表示设置 zookeeper集群的超时时间，当超过这个时间后，zookeeper还没有收到心跳包，则认为服务不可用，-r选项表示当服务不可用的时候仍可以以只读模式访问服务， server表示服务器的ip和端口号  
 连接之后输入 h 查看帮助信息  
 在进行这些之前先回顾一下 ZooKeeper的数据结构  
 
 根节点下面是子节点，子节点下面又可以又很多子节点，比如 node_1这个节点的完整路径就是    /node_1  
 stat /node_1  

	 cZxid						创建时的事务 id  
	 ctime						创建时间  
	 mZxid						修改时的事务id  
	 mtime						修改时间  
	 pZxid						表示该节点的子节点列表最后一次修改的事务id（什么时候回改变子节点列表呢，从当前节点中添加子节点，从当前节点中删除子节点）  
	 cversion       			数据节点中子节点版本号  
	 dataversion  				数据版本号  
	 aclversion     			访问控制版本号  
	 ephemeralOwner 			创建临时节点的事务id,如果是永久节点，那么这个事务id固定  
	 datalength    				当前节点数据的长度  
	 numChildren    			当前节点子节点的个数  
 获取节点数据内容  
 get 节点  
 ls2 显示节点列表  
 创建子节点 crate  
 create -e /节点/子节点  
 临时节点创建的节点在客户端临时会话结束的时候都会删除  
 修改节点  
 set path data  
 每次修改数据，节点的数据版本号会改变  
 修改数据的时候要么设置版本号时，要么设置的版本号为上一次查询的版本号  
 删除   
 delete path [version] 不能删除含有子节点的节点  
 rmr 删除含有子节点的节点  
 配额  
 setquot -n|-b var path  
 
 Java 客户端API操作 ZooKeeper  
 通过 API 链接 ZK  
　创建一个测试类CreateSession,生成 main 方法，创建 ZooKeeper，对象，在构造方法中传入服务器 IP地址以及端口号，以及设置的超时时间，在创建ZooKeeper实例的时候需要传入一个 Watcher 实例，这个类是一个接口，因此我们需要创建一个类实现这个接口，这里我们让 CreateSession来实现该接口，获取 zookeeper状态，将watcher 接收到的时间输出，由于是异步的，因此返回的状态是 CONNECTING,调用ZK 的函数不应该在main方法中，应该在 Watcher 的process 方法中做一些操作，操作方式有异步和同步两种方式，
 同步创建节点的时候有，将服务端返回的节点路径输出。
 异步创建节点，需要传入一个StringCallBack接口的实例，另外需要传入一个参数，作为异步调用的上下文，在回调中返回结果信息
  
  	获取子节点
  	getChildren();
	
	节点数据
	getData();

	删除
	delete();

	创建节点
	create();

	判断节点是否存在
	exists()

	修改数据
	setData();

###权限
####权限模式(scheme)
	1.ip    
	2.digest  
####权限对象（ID）
ip权限模式: 具体ip地址  
digest权限模式:username:Base64(SHA-1(username:password))  
####权限
create(C)  
delete(D)  
READ(R)  
WRITE(W)  
ADMIN(A)  
#####单个权限、完全权限、复合权限

#####权限组合：shceme + ID + permission
  
	
	ACL aclIp = new ACL(Perms.READ,new Id("ip","192.168.0.155"));

	ACL aclDigest = new ACL(Perms.READ,new Id("digest",DigestAuthenticationProvider.generateDigest(username:password)));

	ArrayList<ACL> acls = new ArrayList<ACL>();

	ZooKeeper预设的权限列表 Ids.xxxxx

	通过客户端为用户创建权限
	create /nodexxx ip:192.168.xxx.xxx:crwda

	DigestAuthenticationProvider.generateDigest("xxxx:xxxx");

	create /nodexxx digest:使用zookeeper密文生成函数生成的结果
