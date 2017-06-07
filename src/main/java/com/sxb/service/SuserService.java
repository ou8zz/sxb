package com.sxb.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sxb.model.Qupload;
import com.sxb.model.Suser;

/**
 * @description 随心播用户信息服务接口
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
public interface SuserService {
	Object addSuser(Suser u);
	Object editSuser(Suser u);
	void delSuser(Integer id);
	List<Suser> getSusers(Suser u);
	Suser getSuser(Suser u);
	String imagePost(MultipartFile file, String imagetype, String userphone, String path) throws IllegalStateException, IOException;
	Qupload getUploadToken(Qupload qu);
	Object uploadCallback(Qupload qu);
}
