package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 同步 节点是否存在
 * @author TianYu
 *
 */
public class NodeExistsSync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new NodeExistsSync());
			System.out.println(zookeeper.getState().toString());
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
				nodeExists();
			}
		}
	}

	/**
	 * 节点是否存在
	 */
	private void nodeExists() {
		try {
			System.out.println("stat:" + zookeeper.exists("/node_2/node_2_44", new NodeExistsSync()));
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
