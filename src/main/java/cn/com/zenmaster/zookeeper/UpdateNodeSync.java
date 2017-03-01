package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 同步更新节点数据
 * @author TianYu
 *
 */
public class UpdateNodeSync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new UpdateNodeSync());
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
				updateNode();
			}
		}
	}

	/**
	 * 更新节点数据
	 */
	private void updateNode() {
		try {
			Stat stat = zookeeper.setData("/node1", "node1".getBytes(), -1);
			System.out.println("return path: " + stat);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
