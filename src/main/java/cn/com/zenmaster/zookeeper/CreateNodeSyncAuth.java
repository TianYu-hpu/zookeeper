package cn.com.zenmaster.zookeeper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * 创建节点的时候设置权限 ACL
 * @author TianYu
 *
 */
public class CreateNodeSyncAuth implements Watcher {

	private static ZooKeeper zookeeper;
	/**
	 * 当网络环境不好的时候，会导致zookeeper客户端断线重连，这样的话就会造成 createNode 执行多次，
	 * 加入这个变量，当执行完成一次的时候就不会造成多次执行的情况
	 */
	private static boolean isDone = false;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new CreateNodeSyncAuth());
			
			Thread.sleep(Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	public void process(WatchedEvent event) {
		System.out.println("事件输出"+ event);
		if(event.getState()==KeeperState.SyncConnected) {
			if(!isDone && event.getType() == EventType.None && null == event.getPath()) {
				createNode();
			}
		}
	}

	/*
	 * 权限模式(scheme): ip, digest
	 * 授权对象(ID)
	 * 		ip权限模式:  具体的ip地址
	 * 		digest权限模式: username:Base64(SHA-1(username:password))
	 * 权限(permission): create(C), DELETE(D),READ(R), WRITE(W), ADMIN(A) 
	 * 		注：单个权限，完全权限，复合权限
	 * 
	 * 权限组合: scheme + ID + permission
	 * 
	 * */
	private void createNode() {
		try {
			ACL aclIP = new ACL(Perms.READ, new Id("ip", "192.168.30.86"));
			ACL aclDigest = new ACL(Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("tianyu:123456")));
			//在创建节点的时候增加用户 digest授权模式 权限 ACL 也可以在客户端中通过 addauth digest tianyu:123456 的方式来添加授权
			//zookeeper.addAuthInfo("digest", "tianyu:123456".getBytes())
			ArrayList<ACL> aclList = new ArrayList<ACL>();
			aclList.add(aclIP);
			aclList.add(aclDigest);
			String path = zookeeper.create("/node_3", "node1".getBytes(), aclList, CreateMode.PERSISTENT);
			System.out.println("return path: " + path);
			
			//已经执行完了
			isDone = true;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}

}

