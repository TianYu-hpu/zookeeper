package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步 节点是否存在
 * @author TianYu
 *
 */
public class NodeExistsASync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new NodeExistsASync());
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
				exists();
			} else {
				try {
					if(event.getType() == EventType.NodeCreated) {
						System.out.println(event.getPath()+" created");
						zookeeper.exists(event.getPath(), true, new MyCallback(), null);
					} else if (event.getType() == EventType.NodeDataChanged) {
						System.out.println(event.getPath()+" updated");
						zookeeper.exists(event.getPath(), true, new MyCallback(), null);
					} else if (event.getType() == EventType.NodeDeleted) {
						System.out.println(event.getPath()+" deleted");
						zookeeper.exists(event.getPath(), true, new MyCallback(), null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 节点是否存在
	 */
	private void exists() {
		zookeeper.exists("/node_2/node_2_2", true, new MyCallback(), null);
	}
	
	static class MyCallback implements AsyncCallback.StatCallback {

		public void processResult(int rc, String path, Object ctx, Stat stat) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc="+rc).append("\n");
			sb.append("path="+path).append("\n");
			sb.append("ctx="+ctx).append("\n");
			sb.append("stat="+stat).append("\n");
			System.out.println(sb.toString());
			
		}
		
	}

}
