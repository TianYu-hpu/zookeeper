package cn.com.zenmaster.zookeeper;

import java.security.NoSuchAlgorithmException;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * 创建授权信息
 * @author TianYu
 *
 */
public class CreateAuthString {

	
	public static void main(String[] args) {
		try {
			System.out.println(DigestAuthenticationProvider.generateDigest("tianyu:123456"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
}
