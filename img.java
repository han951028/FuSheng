package com.mossle.activity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.include.common.DateUtils;
import com.include.dto.DTOFamily;
import com.service.FamilyManage;
import com.service.Oauth2Service;
import com.service.commen.Util;
import com.soecode.wxtools.api.WxConfig;
import com.web.zybsl.CommunityActivity;

public class img {
		@RequestMapping("family/save")
		public String saveedream(DTOFamily dto, Model model, HttpServletRequest request)
				throws ServletException, IOException {
			// 从session中取得openId
			String openId = (String) request.getSession().getAttribute("openId");
			// openId不存在的场合调取回调页面 重新获取openId
			if (StringUtils.isEmpty(openId)) {
				String code = request.getParameter("code");
				if (null == code) {
					// 转译回调页面地址
					String reUrlBtn1 = URLEncoder.encode(Util.basePath + "family/save");
					// 取得appid
					WxConfig wxc = new WxConfig();
					String appId = wxc.getAppId();
					model.addAttribute("redirect_uri", reUrlBtn1);
					model.addAttribute("appId", appId);
					return "get_openid";
				}
				openId = Oauth2Service.getOpenId(code);
				if (StringUtils.isEmpty(openId)) {
					return "warn";
				}
				request.getSession().setAttribute("openId", openId);
			}

			if (dto.getId() == null || dto.getId().equals("")) {
				long startTime = System.currentTimeMillis();
				// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
						request.getSession().getServletContext());
				// 检查form中是否有enctype="multipart/form-data"
				String actPic = "";
				String actPicImg = "";
				if (multipartResolver.isMultipart(request)) {
					// 将request变成多部分request
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					// 获取multiRequest 中所有的文件名
					Iterator<String> iter = multiRequest.getFileNames();
					int index = 0;
					while (iter.hasNext()) {
						// 一次遍历所有文件
						List<MultipartFile> list = multiRequest.getFiles(iter.next().toString());
						for (int i = 0; i < list.size(); i++) {
							MultipartFile file = list.get(i);
							if (file != null) {
								String picName = file.getOriginalFilename();
								if (!picName.equals("")) {
									String picType = picName.substring(picName.lastIndexOf("."));
									if (picType.equals(".jpeg")) {
										picType = ".jpg";
									}
									// 传缩略图
									actPic = "images/hdcy/" + Util.getRandomString(4) + System.currentTimeMillis()
											+ picType;
									String path = Util.repDatePath + actPic;
									String path1 = Util.dirPath + "../../" + actPic;

									File tagFile = new File(path);
									File tagDir = new File(tagFile.getParent());
									if (!tagDir.exists()) {
										tagDir.mkdirs();
									}
									try {
										BufferedImage img = ImageIO.read(file.getInputStream()); // 取文件流构造Image对象
										BufferedImage imageR = null ;		//截取后的图像
										int widthR = img.getWidth(null); // 得到源图宽
										int heightR = img.getHeight(null); // 得到源图宽
										int widthW = 100;					//最后生成图片大小
										int heightW = 100;					//最后生成图片大小
										int widthSub=0;						//截取图片方法的位移大小
										int heightSub=0;					//截取图片方法的位移大小
										BufferedImage imageS =new BufferedImage(widthW, heightW,
																BufferedImage.TYPE_INT_RGB);;
										//生成原图画布
										if((widthR>heightR)&&(heightR>100)){
											widthSub=(widthR-heightR)/2;
											imageR=img.getSubimage(widthSub, heightSub, heightR, heightR);
											imageS.getGraphics().drawImage(imageR, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageS, "jpeg", tagFile);
										}else if((widthR<heightR)&&(widthR>100)){
											heightSub=(heightR-widthR)/2;
											imageR=img.getSubimage(widthSub, heightSub, widthR, widthR);
											imageS.getGraphics().drawImage(imageR, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageS, "jpeg", tagFile);
										}else{
											imageR=new BufferedImage(widthW, heightW,
													BufferedImage.TYPE_INT_RGB);
											imageR.getGraphics().drawImage(img, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageR, "jpeg", tagFile);
											
										}
										
										CommunityActivity.fileCopy(path, path1);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// 传原图
									actPicImg = "images/hdcy/" + Util.getRandomString(5) + System.currentTimeMillis()
											+ picType;
									String pathImg = Util.repDatePath + actPicImg;
									String pathImg1 = Util.dirPath + "../../" + actPicImg;
									File tagFileImg = new File(pathImg);
									File tagDirImg = new File(tagFileImg.getParent());
									if (!tagDirImg.exists()) {
										tagDirImg.mkdirs();
									}
									// 上传
									file.transferTo(new File(pathImg));
									CommunityActivity.fileCopy(pathImg, pathImg1);

									if (index == 0) {
										dto.setAct_pic1(actPic);
										dto.setAct_pic1_img(actPicImg);
										index = 1;
									} else if (index == 1) {
										dto.setAct_pic2(actPic);
										dto.setAct_pic2_img(actPicImg);
										index = 2;
									}else if (index == 2) {
										dto.setAct_pic3(actPic);
										dto.setAct_pic3_img(actPicImg);
										index = 3;
									}

								}
							}
						}
					}
				}
				if (dto.getAct_pic1() == null) {
					dto.setAct_pic1("");
					dto.setAct_pic1_img("");
				}
				if (dto.getAct_pic2() == null) {
					dto.setAct_pic2("");
					dto.setAct_pic2_img("");
				}
				if (dto.getAct_pic3() == null) {
					dto.setAct_pic3("");
					dto.setAct_pic3_img("");
				}
				dto.setId(request.getParameter("id"));
				dto.setAct_title(request.getParameter("act_title"));
				dto.setAct_content(request.getParameter("act_content"));
				dto.setCreate_time(DateUtils.getDateWithHMS());
				dto.setType("1");
				dto.setOpen_id(openId);

				FamilyManage.saveFamily(dto);
			} else {
				long startTime = System.currentTimeMillis();
				// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
						request.getSession().getServletContext());
				// 检查form中是否有enctype="multipart/form-data"
				String actPic = "";
				String actPicImg = "";
				if (multipartResolver.isMultipart(request)) {
					// 将request变成多部分request
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					// 获取multiRequest 中所有的文件名
					Iterator<String> iter = multiRequest.getFileNames();
					int index = 0;
					while (iter.hasNext()) {
						// 一次遍历所有文件
						List<MultipartFile> list = multiRequest.getFiles(iter.next().toString());
						for (int i = 0; i < list.size(); i++) {
							MultipartFile file = list.get(i);
							if (file != null) {
								String picName = file.getOriginalFilename();
								if (!picName.equals("")) {
									String picType = picName.substring(picName.lastIndexOf("."));
									if (picType.equals(".jpeg")) {
										picType = ".jpg";
									}
									// 传缩略图
									actPic = "images/hdcy/" + Util.getRandomString(4) + System.currentTimeMillis()
											+ picType;
									String path = Util.repDatePath + actPic;
									String path1 = Util.dirPath + "../../" + actPic;

									File tagFile = new File(path);
									File tagDir = new File(tagFile.getParent());
									if (!tagDir.exists()) {
										tagDir.mkdirs();
									}
									try {
										BufferedImage img = ImageIO.read(file.getInputStream()); // 取文件流构造Image对象
										BufferedImage imageR = null ;		//截取后的图像
										int widthR = img.getWidth(null); // 得到源图宽
										int heightR = img.getHeight(null); // 得到源图宽
										int widthW = 100;					//最后生成图片大小
										int heightW = 100;					//最后生成图片大小
										int widthSub=0;						//截取图片方法的位移大小
										int heightSub=0;					//截取图片方法的位移大小
										BufferedImage imageS =new BufferedImage(widthW, heightW,
																BufferedImage.TYPE_INT_RGB);;
										//生成原图画布
										if((widthR>heightR)&&(heightR>100)){
											widthSub=(widthR-heightR)/2;
											imageR=img.getSubimage(widthSub, heightSub, heightR, heightR);
											imageS.getGraphics().drawImage(imageR, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageS, "jpeg", tagFile);
										}else if((widthR<heightR)&&(widthR>100)){
											heightSub=(heightR-widthR)/2;
											imageR=img.getSubimage(widthSub, heightSub, widthR, widthR);
											imageS.getGraphics().drawImage(imageR, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageS, "jpeg", tagFile);
										}else{
											imageR=new BufferedImage(widthW, heightW,
													BufferedImage.TYPE_INT_RGB);
											imageR.getGraphics().drawImage(img, 0, 0, widthW, heightW, null); // 绘制缩小后的图
											ImageIO.write(imageR, "jpeg", tagFile);
											
										}
										
										CommunityActivity.fileCopy(path, path1);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// 传原图
									actPicImg = "images/hdcy/" + Util.getRandomString(5) + System.currentTimeMillis()
											+ picType;
									String pathImg = Util.repDatePath + actPicImg;
									String pathImg1 = Util.dirPath + "../../" + actPicImg;
									File tagFileImg = new File(pathImg);
									File tagDirImg = new File(tagFileImg.getParent());
									if (!tagDirImg.exists()) {
										tagDirImg.mkdirs();
									}
									// 上传
									file.transferTo(new File(pathImg));
									CommunityActivity.fileCopy(pathImg, pathImg1);

									if (index == 0) {
										dto.setAct_pic1(actPic);
										dto.setAct_pic1_img(actPicImg);
										index = 1;
									} else if (index == 1) {
										dto.setAct_pic2(actPic);
										dto.setAct_pic2_img(actPicImg);
										index = 2;
									}else if (index == 2) {
										dto.setAct_pic3(actPic);
										dto.setAct_pic3_img(actPicImg);
										index = 3;
									}
								}
							}
						}
					}
				}
				if (dto.getAct_pic1() == null) {
					dto.setAct_pic1("");
					dto.setAct_pic1_img("");
				}
				if (dto.getAct_pic2() == null) {
					dto.setAct_pic2("");
					dto.setAct_pic2_img("");
				}
				if (dto.getAct_pic3() == null) {
					dto.setAct_pic3("");
					dto.setAct_pic3_img("");
				}
				dto.setId(request.getParameter("id"));
				dto.setAct_title(request.getParameter("act_title"));
				dto.setAct_content(request.getParameter("act_content"));
				dto.setCreate_time(DateUtils.getDateWithHMS());
				FamilyManage.updateFamily(dto);
			}
			return "redirect:/family/info";
		}
}
