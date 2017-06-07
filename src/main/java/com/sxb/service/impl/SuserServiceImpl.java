package com.sxb.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.util.Auth;
import com.sxb.model.LiveRecord;
import com.sxb.model.Qupload;
import com.sxb.model.RespsonData;
import com.sxb.model.Suser;
import com.sxb.service.SuserService;
import com.sxb.util.RegexUtil;
import com.sxb.util.ResourceUtil;
import com.sxb.util.UploadUtil;

/**
 * @description 随心播用户信息服务接口实现
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date 2016/06/14
 * @version 1.0
 */
@Repository(value="suserService")
@Scope(value="prototype")
@Transactional(propagation=Propagation.REQUIRED)
public class SuserServiceImpl implements SuserService {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public Object addSuser(Suser u) {
		RespsonData rd = new RespsonData();
		
		// 记录登录日志
		sqlSession.insert("suser.addLoginLog", u);
		
		// 校验用户名是否已存在(目前修改为用户可以重复)
//		Suser byUserName = new Suser();
//		byUserName.setUsername(u.getUsername());
//		byUserName = sqlSession.selectOne("suser.getSusers", byUserName);
//		if(RegexUtil.notEmpty(byUserName)) {
//			rd.setErrorCode(562);
//			rd.setErrorInfo("username '" + byUserName.getUsername() + "' is existed");
//			return rd;
//		}
		
		// 校验手机是否已存在
		Suser byUserPhone = new Suser();
		byUserPhone.setUserphone(u.getUserphone());
		byUserPhone = sqlSession.selectOne("suser.getSusers", byUserPhone);
		if(RegexUtil.notEmpty(byUserPhone)) {
			rd.setErrorCode(561);
			rd.setErrorInfo("userphone '" + byUserPhone.getUserphone() + "' is existed");
			rd.setData(byUserPhone);
			return rd;
		}
		
		// 执行添加操作
		if(RegexUtil.isEmpty(u.getHeadimagepath())) {
			u.setHeadimagepath(ResourceUtil.getConf("qn.prefix") + "chaoji_logo.png");	// 如果用户没有传头像，设置默认头像路径
		}
		sqlSession.insert("suser.addSuser", u);
		rd.setData(u);
		return rd;
	}

	@Override
	public void delSuser(Integer id) {
//		sqlSession.delete("suser.delSuser", id);
	}

	@Override
	public Object editSuser(Suser u) {
		RespsonData rd = new RespsonData();
		
		// 校验用户名是否已存在(目前修改为用户可以重复)
//		if(RegexUtil.notEmpty(u.getUsername())) {
//			Suser byUserName = new Suser();
//			byUserName.setUsername(u.getUsername());
//			byUserName = sqlSession.selectOne("suser.getSusers", byUserName);
//			if(RegexUtil.notEmpty(byUserName)) {
//				rd.setErrorCode(562);
//				rd.setErrorInfo("username '" + byUserName.getUsername() + "' is existed");
//				return rd;
//			}
//		}
		
		// 执行update
		int update = sqlSession.update("suser.editSuser", u);
		rd.setData(update);
		return rd;
	}

	@Override
	public List<Suser> getSusers(Suser u) {
		List<Suser> p = sqlSession.selectList("suser.getSusers", u);
		return p;
	}
	
	@Override
	public Suser getSuser(Suser u) {
		Suser queryForList = sqlSession.selectOne("suser.getSusers", u);
		return queryForList;
	}

	/**
	 *         	// imagetype
        	// 1:HEAD_IMAGE,2:COVER_IMAGE,3:FORCAST_IMAGE
//        	if($imageType == head_image) {
//        		$insertSql = "UPDATE User SET headimagepath='$imageName' WHERE userphone='$userPhone'";
//        	} else if ($imageType == cover_image) {
//        		$insertSql = "UPDATE Liveinfo SET coverimagepath='$imageName' WHERE programid=$roomNum";
//        	} else if($imageType == forcast_image) {
//        		$insertSql = "UPDATE Liveforcast SET coverimagepath='$imageName' WHERE roomnum=$roomNum";
//        	}
	 */
	@Override
	public String imagePost(MultipartFile file, String imagetype, String userphone, String path) throws IllegalStateException, IOException {
		// 将文件资源保存到数据库
		String images = imagetype+"-"+System.currentTimeMillis()+"-"+file.getOriginalFilename();
		String prefix = ResourceUtil.getConf("qn.prefix");
		if("head_image".equals(imagetype)) {
			Suser u = new Suser();
			u.setUserphone(userphone);
			u.setHeadimagepath(prefix+images);
			sqlSession.update("suser.editSuser", u);
		}
		else if("cover_image".equals(imagetype)) {
			LiveRecord lr = new LiveRecord();
			lr.setHost_uid(userphone);
			lr.setCover(prefix+images);
			sqlSession.update("live.updateLiveRecord", lr);
		}
		
		// 写入文件
//		File uploadPath = new File(path);
//		if(!uploadPath.exists()) {
//			uploadPath.mkdir();
//        }
//		String pathFile=path+images;
//		File dest = new File(pathFile);
//		file.transferTo(dest);
		
		// 上传到七牛云
		UploadUtil.upload(file.getBytes(), images);
		return prefix+images;
	}
	
	/**
	 * 七牛云上传图标必须接口
	 * @param name		上传文件的名称
	 * @param imagetype		上传文件类型，如用户头像，直播头像等
	 * @param userphone		上传用户ID
	 * @return Qupload 对象	包含七牛云uploadToken 并且将图片名称返回给客户端，好让客户端根据约定的名字进行保存
	 */
	@Override
	public Qupload getUploadToken(Qupload qu) {
		// 将文件资源保存到数据库
		String imagetype = qu.getImagetype();
		String images = imagetype+"-"+System.currentTimeMillis()+"-"+qu.getName();
		String prefix = ResourceUtil.getConf("qn.prefix");
		
		// 密钥配置 简单上传，使用默认策略，只需要设置上传的空间名就可以了
		Auth auth = Auth.create(ResourceUtil.getConf("qn.AK"), ResourceUtil.getConf("qn.SK"));
		String uploadToken = auth.uploadToken(ResourceUtil.getConf("qn.bucketName"));
		
		Qupload q = new Qupload();
		q.setPrefix(prefix);
		q.setName(images);
		q.setUploadToken(uploadToken);
		return q;
	}
	
	/**
	 * 七牛云上传图标必须接口
	 * @param name		上传文件的名称
	 * @param imagetype		上传文件类型，如用户头像，直播头像等
	 * @param userphone		上传用户ID
	 * @return Qupload 对象	包含七牛云uploadToken 并且将图片名称返回给客户端，好让客户端根据约定的名字进行保存
	 */
	@Override
	public Object uploadCallback(Qupload qu) {
		// 将文件资源保存到数据库
		String imagetype = qu.getImagetype();
		String userphone = qu.getUserphone();
		String prefix = ResourceUtil.getConf("qn.prefix");
		
		// 密钥配置 简单上传，使用默认策略，只需要设置上传的空间名就可以了
		if("head_image".equals(imagetype)) {
			Suser u = new Suser();
			u.setUserphone(userphone);
			u.setHeadimagepath(prefix+qu.getName());
			sqlSession.update("suser.editSuser", u);
		}
		else if("cover_image".equals(imagetype)) {
			LiveRecord lr = new LiveRecord();
			lr.setHost_uid(userphone);
			lr.setCover(prefix+qu.getName());
			sqlSession.update("live.updateLiveRecord", lr);
		}
		return null;
	}
}
