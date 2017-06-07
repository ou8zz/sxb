package com.sxb.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * @description 七牛云图片上传
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/29
 * @version 1.0
 */
public class UploadUtil {

	// 密钥配置
	private static Auth auth = Auth.create(ResourceUtil.getConf("qn.AK"), ResourceUtil.getConf("qn.SK"));
	// 创建上传对象
	private static UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	private static String getUpToken() {
		return auth.uploadToken(ResourceUtil.getConf("qn.bucketName"));
	}

	public static void upload(byte[] path, String name) {
		try {
			// 调用put方法上传
			Response res = uploadManager.put(path, name, getUpToken());
			// 打印返回的信息
			System.out.println(res.bodyString());
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
	}
	
}
