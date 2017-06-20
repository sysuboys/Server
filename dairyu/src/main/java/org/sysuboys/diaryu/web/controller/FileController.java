package org.sysuboys.diaryu.web.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.sysuboys.diaryu.business.service.IDiaryFileService;

@Controller
public class FileController {

	@Autowired
	IDiaryFileService diaryFileService;

	@RequestMapping("/try_upload")
	public String hello() {
		return "upload";
	}

	@RequestMapping(value = "/try_upload", method = RequestMethod.POST)
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,
			ModelMap model) {

		// MultipartHttpServletRequest mreq=(MultipartHttpServletRequest)req;
		// MultipartFile file = mreq.getFile("file");
		// String fileName = file.getOriginalFilename();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// FileOutputStream fos = new
		// FileOutputStream(req.getSession().getServletContext().getRealPath("/")+
		// "upload/"+sdf.format(new
		// Date())+fileName.substring(fileName.lastIndexOf(".")));
		// fos.write(file.getBytes());
		// fos.flush();
		// fos.close();
		// return "helloworld";

		// System.out.println("开始");
		// String path = "./";
		// String fileName = file.getOriginalFilename();
		// // String fileName = new Date().getTime()+".jpg";
		// System.out.println(path);
		// File targetFile = new File(path, fileName);
		// if (!targetFile.exists()) {
		// targetFile.mkdirs();
		// }
		//
		// // 保存
		// try {
		// file.transferTo(targetFile);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// model.addAttribute("fileUrl", request.getContextPath() + "/upload/" +
		// fileName);
		
		try {
			// 这里将上传得到的文件保存至 d:\\temp\\file 目录
			diaryFileService.create("2.txt", file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "login";
	}

	@RequestMapping("/try_download")
	public @ResponseBody ResponseEntity<byte[]> getFile(HttpServletRequest request) throws IOException {
		File file = diaryFileService.get("2.txt");
		// String dfileName = new String(fileName.getBytes("gb2312"),
		// "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		// headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}

	@RequestMapping("/try_download_FORBIDDEN")
	public @ResponseBody ResponseEntity<byte[]> getFile4(HttpServletRequest request) throws IOException {
		File file = diaryFileService.get("2.txt");
		// String dfileName = new String(fileName.getBytes("gb2312"),
		// "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		// headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.FORBIDDEN);
	}

	@RequestMapping("/try_download_nothing")
	public @ResponseBody ResponseEntity<byte[]> getFile2(HttpServletRequest request) throws IOException {
		// String dfileName = new String(fileName.getBytes("gb2312"),
		// "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		// headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity<byte[]>(new byte[0], headers, HttpStatus.CREATED);
	}

	@RequestMapping("/try_download_null")
	public @ResponseBody ResponseEntity<byte[]> getFile3(HttpServletRequest request) throws IOException {
		return null;
	}
	
}
