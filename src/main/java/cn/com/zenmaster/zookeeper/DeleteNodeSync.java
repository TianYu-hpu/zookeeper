package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除节点
 * @author TianYu
 *
 */
public class DeleteNodeSync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new DeleteNodeSync());
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
				deleteNode();
			}
		}
	}

	/**
	 * 删除节点
	 */
	private void deleteNode() {
		try {
			zookeeper.delete("/node1", -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
