package com.sxb.controller.php;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.annotation.Resource;
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

import com.google.gson.reflect.TypeToken;
import com.sxb.model.RespsonData;
import com.sxb.model.RespsonPHP;
import com.sxb.model.Suser;
import com.sxb.service.SuserService;
import com.sxb.util.HtmTools;
import com.sxb.util.JsonUtils;
import com.sxb.util.RegexUtil;

/**
 * @description PHP1.7版本接口----用户信息服务RestController
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@RestController
@Scope(value="request")
public class SuserCore {
	private Log log = LogFactory.getLog(SuserCore.class);
	
	@Resource(name="suserService")
	private SuserService suserService;
	
	/**
	 * 注册新增用户信息 http://localhost/register.php
	 * @param Suser json 对象
	 * 请求示例：data={"userphone":"1589336622", "username":"user_001", "password":"pwe"}
	 * @return RespsonData 成功失败
	 */
	@RequestMapping(method=RequestMethod.POST, value="/register.php")
	public Object register(@RequestBody(required=false) String data) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(data)) {
				rd.setCode(561);
				rd.setData("data is null");
				return rd;
			}
			data = HtmTools.decode(data);
			data = data.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
			Suser u = new Suser();
			u.setUsername(m.get("username"));
			u.setUserphone(m.get("userphone"));
			u.setPassword(m.get("password"));
			suserService.addSuser(u);
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("add user error", e);
		}
		return rd;
	}
	
	/**
	 * 编辑用户信息 http://localhost/user_modifyfields.php
	 * @param Suser json 对象
	 * 请求示例：data={"userphone":"1589336622", "username":"user_001", "address":"address", "constell":"constell", "signature":"signature"}
	 * @return RespsonData 用户信息
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user_modifyfields.php")
	public Object editUser(@RequestBody(required=false) String data) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(data)) {
				rd.setCode(561);
				rd.setData("data is null");
				return rd;
			}
			data = HtmTools.decode(data);
			data = data.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
			Suser u = new Suser();
			u.setUsername(m.get("username"));
			u.setUserphone(m.get("userphone"));
			u.setAddress(m.get("address"));
			u.setConstellation(m.get("constell"));
			u.setPersonal_signature(m.get("signature"));
			suserService.editSuser(u);
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("editUser error", e);
		}
		return rd;
	}
	
	/**
	 * 获取用户信息 http://localhost/user_getinfo.php
	 * @param Suser json 对象
	 * 请求示例：data={"userphone":"1589336622"}
	 * @return RespsonData 用户信息
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user_getinfo.php")
	public Object getUserInfo(@RequestBody(required=false) String data) {
		RespsonPHP rd = new RespsonPHP();
		try {
			// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(data)) {
				rd.setCode(561);
				rd.setData("data is null");
				return rd;
			}
			data = HtmTools.decode(data);
			data = data.split("=")[1];
			Map<String, String> m = JsonUtils.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
			Suser u = new Suser(m.get("userphone"));
			Suser suser = suserService.getSuser(u);
			if(RegexUtil.isEmpty(suser)) {
				rd.setCode(561);
				rd.setData("user is not existed");
				return rd;
			}
			rd.setData(suser);
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("getUserInfo error", e);
		}
		return rd;
	}
	
	/**
	 * 上传用户头像 http://lsxb/user/image_post.php
	 * @param Suser json 对象
	 * 请求示例：
	 * impage=file
	 * imagepostdata={"file":"文件格式内容","imagetype":1,"userphone":"13588885555"}
	 * 文件类型分为分别为：head_image:用户头像,cover_image:封面
	 * @return RespsonData data=1表示成功0失败
	 */
	@RequestMapping(method=RequestMethod.POST, value="/image_post.php")
    public Object imagePost(@RequestParam(required=false) MultipartFile image, 
    						@RequestParam(required=false) String imagepostdata,
    						HttpServletRequest request) {
		RespsonPHP rd = new RespsonPHP();
        try {
        	if(RegexUtil.isEmpty(image)) {
        		rd.setCode(561);
        		rd.setData("file is null");
        		return rd;
        	}
        	// 处理PHP格式数据，苦逼
			if(RegexUtil.isEmpty(imagepostdata)) {
				rd.setCode(561);
				rd.setData("data is null");
				return rd;
			}
			imagepostdata = HtmTools.decode(imagepostdata);
			Map<String, String> m = JsonUtils.fromJson(imagepostdata, new TypeToken<Map<String, String>>() {}.getType());
        	String imagetype = m.get("imagetype");
        	String userphone = m.get("userphone");
			if(RegexUtil.isEmpty(imagetype)) {
				rd.setCode(561);
        		rd.setData("imagetype is null");
        		return rd;
        	} else if(imagetype.equals("1")) {
        		imagetype = "head_image";
        	} else if(imagetype.equals("2")) {
        		imagetype = "cover_image";
        	}
        	if(RegexUtil.isEmpty(userphone)) {
        		rd.setCode(561);
        		rd.setData("userphone is null");
        		return rd;
        	}
        	String path = request.getSession().getServletContext().getRealPath("/") + "/upload/";
        	String images = suserService.imagePost(image, imagetype, userphone, path);
        	rd.setData(images);
		} catch (Exception e) {
			rd.setCode(560);
			rd.setData(e.getMessage());
			log.error("imagePost error", e);
		}
        return rd;
    }
	
	/**
	 * 获取指定图片 http://localhost/image_get.php
	 * 所有本地资源文件都已经转移到七牛云平台，所有后台所有数据都从七牛云取得
	 * @param Suser json 对象
	 * 请求示例：{"imagepath":"/upload/img.jpg", "width":"300", "height":"300"}
	 * @return RespsonData 返回图片资源
	 */
	@RequestMapping(method=RequestMethod.GET, value="/image_get.php")
	public Object getImage(@RequestParam(value="imagepath", required=false) String imagepath, 
			@RequestParam(value="width", required=false) Integer width, 
			@RequestParam(value="height", required=false) Integer height,
			HttpServletRequest request, 
			HttpServletResponse response) {
		ServletOutputStream out = null;
		RespsonData rd = new RespsonData("");
		try {
			response.setContentType("image/gif");
			out = response.getOutputStream();
			String ac = imagepath;
			if(RegexUtil.notEmpty(width) && RegexUtil.notEmpty(height)) {
				ac = imagepath + "?imageView2/2/w/"+width+"/h/"+height+"/interlace/0/q/100";
			}
	        URL url = new URL(ac);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            out.write(btImg);
			out.flush();
			out.close();
		} catch (Exception e) {
			rd.setErrorCode(560);
			rd.setErrorInfo(e.getMessage());
			log.error("getImage error", e);
		}
		if (out != null) {
			try {out.close();} catch (IOException e) {}
		}
		return rd;
	}
	
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    private byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
