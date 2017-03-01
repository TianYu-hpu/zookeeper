package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 异步 删除节点
 * @author TianYu
 *
 */
public class DeleteNodeASync implements Watcher {

	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new DeleteNodeASync());
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
		zookeeper.delete("/node_2/node_2_1", -1, new MyCallback(), null);
	}
	
	static class MyCallback implements AsyncCallback.VoidCallback {

		public void processResult(int rc, String path, Object ctx) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc="+rc).append("\n");
			sb.append("path"+path).append("\n");
			sb.append("ctx="+ctx).append("\n");
			System.out.println(sb.toString());
		}
		
	}

}
