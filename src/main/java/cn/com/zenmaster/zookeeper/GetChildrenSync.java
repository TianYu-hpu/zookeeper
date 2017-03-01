package cn.com.zenmaster.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 同步 获取子节点信息
 * @author TianYu
 *
 */
public class GetChildrenSync implements Watcher {

	private static ZooKeeper zookeeper;
	private static Stat stat = new Stat();
	 
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new GetChildrenSync());
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
				getChildren();
			}
		}
	}

	/**
	 * 获取子节点信息
	 */
	private void getChildren() {
		try {
			List<String> list = zookeeper.getChildren("/node_2", false, stat);
			for(String str : list) {
				System.out.println(str);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
