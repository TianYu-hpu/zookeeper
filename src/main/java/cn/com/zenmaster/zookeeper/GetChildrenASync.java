package cn.com.zenmaster.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步 获取节点信息
 * @author TianYu
 *
 */
public class GetChildrenASync implements Watcher {

	private static ZooKeeper zookeeper;
	 
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new GetChildrenASync());
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
	 * 获取节点信息
	 */
	private void getChildren() {
		zookeeper.getChildren("/node_2", true, new MyCallback(), null);
	}
	
	static class MyCallback implements AsyncCallback.Children2Callback {
		public void processResult(int rc, String path, Object ctx,List<String> children, Stat stat) {
			StringBuilder sb = new StringBuilder();
			sb.append("rc="+rc).append("\n");
			sb.append("path="+path).append("\n");
			sb.append("ctx="+ctx).append("\n");
			sb.append("children="+children).append("\n");
			sb.append("stat="+stat).append("\n");
			System.out.println(sb.toString());
		}
	}
	
}
