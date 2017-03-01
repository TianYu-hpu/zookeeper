package cn.com.zenmaster.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步 获取节点数据
 * @author TianYu
 *
 */
public class GetDataASync implements Watcher {

	private static ZooKeeper zookeeper;
	private static Stat stat = new Stat();
	 
	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.0.35", 5000, new GetDataASync());
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
				getData();
			}
		}
	}

	/**
	 * 获取节点数据
	 */
	private void getData() {
		try {
			System.out.println(new String(zookeeper.getData("/node_2", false, stat)));
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static class MyStringCallback implements DataCallback {
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			StringBuffer sb = new StringBuffer();
			sb.append("rc:" + rc);
			sb.append("\npath:" + path);
			sb.append("\ncrx:" + ctx);
			sb.append("\nbyte:" + new String(data));
			sb.append("\nstat:" + stat);
			System.out.println(sb.toString());
		}
		
	}

}
