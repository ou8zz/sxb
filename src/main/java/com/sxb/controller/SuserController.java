package com.sxb.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sxb.model.Qupload;
import com.sxb.model.RespsonData;
import com.sxb.model.Suser;
import com.sxb.service.SuserService;
import com.sxb.util.RegexUtil;

/**
 * @description 用户信息服务RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Scope(value="request")
public class SuserController {
	private Log log = LogFactory.getLog(SuserController.class);
	
	@Resource(name="suserService")
	private SuserService suserService;
	
	/**
	 * 注册新增用户信息 http://localhost/sxb/user/register
	 * @param Suser json 对象
	 * 请求示例：{"userphone":"1589336622", "username":"user_001", "headimagepath":"http//:www.weixin.qq/head.jpg", "password":"pwe", "atype":"PHONE", "phoneno":"1589336622", "unionno":"weixinaccount"}
	 * @return RespsonData 成功失败
	 */
	@RequestMapping(method=RequestMethod.POST, value="/register")
	public Object register(@RequestBody Suser u) {
		RespsonData rd = new RespsonData("register");
		try {
			rd = (RespsonData) suserService.addSuser(u);
			rd.setUrl("register");
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("add user error", e);
		}
		return rd;
	}
	
	/**
	 * 编辑用户信息 http://localhost/sxb/user/user_modifyfields
	 * @param Suser json 对象
	 * 请求示例：{"userphone":"1589336622", "username":"user_001", "atype":"PHONE", "phoneno":"1589336622", "unionno":"weixinaccount"}
	 * @return RespsonData 用户信息
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user_modifyfields")
	public Object editUser(@RequestBody Suser u) {
		RespsonData rd = new RespsonData("user_modifyfields");
		try {
			rd = (RespsonData) suserService.editSuser(u);
			rd.setUrl("user_modifyfields");
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("editUser error", e);
		}
		return rd;
	}
	
	/**
	 * 获取用户信息 http://localhost/sxb/user/user_getinfo
	 * @param Suser json 对象
	 * 请求示例：{"userphone":"1589336622"}
	 * @return RespsonData 用户信息
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user_getinfo")
	public Object getUserInfo(@RequestBody Suser u) {
		RespsonData rd = new RespsonData("user_getinfo");
		try {
			Suser suser = suserService.getSuser(u);
			rd.setData(suser);
			if(suser==null) {
				rd.setErrorCode(561);
				rd.setErrorInfo("user is not existed");
				rd.setData("");
			}
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getUserInfo error", e);
		}
		return rd;
	}
	
	/**
	 * 上传用户头像 http://localhost/sxb/user/image_post
	 * @param Suser json 对象
	 * 请求示例：
	 * {"file":"文件格式内容","imagetype":"head_image","userphone":"13588885555"}
	 * 文件类型分为分别为：head_image:用户头像,cover_image:封面
	 * @return RespsonData data=1表示成功0失败
	 */
	@RequestMapping(method=RequestMethod.POST, value="/image_post", consumes="multipart/form-data")
    public Object imagePost(@RequestParam(required=false) MultipartFile file, 
    						@RequestParam(required=false) String imagetype, 
    						@RequestParam(required=false) String userphone, 
    						HttpServletRequest request) {
		RespsonData rd = new RespsonData("image_post");
        try {
        	if(RegexUtil.isEmpty(file)) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("file is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(imagetype)) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("imagetype is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(userphone)) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("userphone is null");
        		return rd;
        	}
        	String path = request.getSession().getServletContext().getRealPath("/") + "/upload/";
        	String images = suserService.imagePost(file, imagetype, userphone, path);
        	rd.setData(images);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("imagePost error", e);
		}
        return rd;
    }
	
	/**
	 * 七牛云获取上传文件的key and secret http://localhost/sxb/user/get_uploadinfo
	 * 请求示例：
	 * {"name":"demo.jpg","imagetype":"head_image","userphone":"13588885555"}
	 * 文件类型分为分别为：head_image:用户头像,cover_image:封面
	 * @return RespsonData 七牛云上传文件对象Qupload
	 */
	@RequestMapping(method=RequestMethod.POST, value="/get_uploadinfo")
    public Object getQnUploadInfo(@RequestBody Qupload qu) {
		RespsonData rd = new RespsonData("get_uploadinfo");
        try {
        	if(RegexUtil.isEmpty(qu)) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("Qupload is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getName())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("name is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getImagetype())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("imagetype is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getUserphone())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("userphone is null");
        		return rd;
        	}
        	Qupload images = suserService.getUploadToken(qu);
        	rd.setData(images);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("get_uploadinfo error", e);
		}
        return rd;
    }
	
	/**
	 * 七牛云上传文件Callback http://localhost/sxb/user/upload_callback
	 * 请求示例：
	 * {"name":"head_image-1467685660698-demo.jpg","imagetype":"head_image","userphone":"13588885555"}
	 * 文件类型分为分别为：head_image:用户头像,cover_image:封面
	 * @return RespsonData 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/upload_callback")
    public Object uploadCallback(@RequestBody Qupload qu) {
		RespsonData rd = new RespsonData("upload_callback");
        try {
        	if(RegexUtil.isEmpty(qu)) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("Qupload is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getName())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("name is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getImagetype())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("imagetype is null");
        		return rd;
        	}
        	if(RegexUtil.isEmpty(qu.getUserphone())) {
        		rd.setErrorCode(561);
        		rd.setErrorInfo("userphone is null");
        		return rd;
        	}
        	Object ck = suserService.uploadCallback(qu);
        	rd.setData(ck);
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("upload_callback error", e);
		}
        return rd;
    }

	/**
	 * 获取指定图片 http://localhost/sxb/user/image_get
	 * @param Suser json 对象
	 * 请求示例：{"imagepath":"/upload/img.jpg", "width":"300", "height":"300"}
	 * @return RespsonData 返回图片资源
	 */
	@RequestMapping(method=RequestMethod.GET, value="/image_get")
	public Object getImage(@RequestParam("imagepath") String imagepath, 
			@RequestParam("width") int width, 
			@RequestParam("height") int height, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		RespsonData rd = new RespsonData("image_get");
		FileInputStream fis = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/") + "/upload/";
			response.setContentType("image/gif");
			ServletOutputStream out = response.getOutputStream();
			File file = new File(path+imagepath);
			fis = new FileInputStream(file);
			BufferedImage prevImage = ImageIO.read(fis);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			Graphics graphics = image.createGraphics();
			graphics.drawImage(prevImage, 0, 0, width, height, null);
			ImageIO.write(image, "gif", out);
			out.flush();
			out.close();
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getImage error", e);
		}
		if (fis != null) {
			try {fis.close();} catch (IOException e) {}
		}
		return rd;
	}
}
