package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 异步 创建节点
 * @author TianYu
 *
 */
public class CreateNodeASync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new CreateNodeASync());
			System.out.println(zookeeper.getState());
			
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
			if(event.getType() == EventType.None && null == event.getPath()) {
				createNode();
			}
		}
	}

	/**
	 * 创建节点
	 */
	private void createNode() {
		zookeeper.create("/node2", "node2——1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new MyStringCallback(), "上下文");
	}
	
	static class MyStringCallback implements StringCallback {

		public void processResult(int rc, String path, Object crx, String name) {
			StringBuffer sb = new StringBuffer();
			sb.append("rc:" + rc);
			sb.append("\npath:" + path);
			sb.append("\ncrx:" + crx);
			sb.append("\nname:" + name);
			System.out.println(sb.toString());
		}
		
	}

}
