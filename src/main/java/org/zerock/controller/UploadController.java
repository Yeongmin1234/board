package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	
	
	@GetMapping("/uploadForm")  
	public void uploadForm() {

		log.info("upload form");
	}

	
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
							//�ΰ� �̻��� �����ϱ� ���� �迭���ٰ� ����
		String uploadFolder = "C:\\upload";
						//���ٰ� ���� ���ε� �Ҳ����� ���
		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			//C:\\upload�� �������ϸ� ���ڿ��� saveFile������ ����
			log.info("saveFile : "+saveFile);
			try { 
				multipartFile.transferTo(saveFile);
							//transferTo(���ϸ�) : ���ϸ����� ����
			} catch (Exception e) {
				log.error(e.getMessage());
			}  //end catch
		}  //end for

	}

	
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {

		log.info("upload ajax");
	}

	
	
	
	
	private String getFolder() {//������ ������

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//��¥����(format)�� ��/��/�� ���·� sdf������ ���� 
		Date date = new Date();
		//���ó�¥�� �����ϴ� date������ ���� 
		String str = sdf.format(date);
		//���ó�¥��  ��/��/�� ����(format)�� str������ ����

		return str.replace("-", File.separator);
				 // ġȯ�Լ� // -��   \\�� ���������(ex.2021-10-27 ->2021\\10\\27
	}

	
	
	
	
	
	private boolean checkImageType(File file) {//

		try {
			String contentType = Files.probeContentType(file.toPath());
					//���� Ÿ����                     ���������� ���� �޼ҵ�                   = file�� ������ �����´�.
			return contentType.startsWith("image");
					//�̹���Ÿ���̸�(true) ����
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	
	
	
	
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("uploadFile : " +uploadFile[0].getOriginalFilename());
								//�迭0���� ������ ��¥ �̸�
		//�������������� ����(ArrayList)�ؼ� ������������ AttachFileDTO�� ����
		List<AttachFileDTO> list = new ArrayList<>();
		//���� ������ ����Ǵ� ���
		String uploadFolder = "C:\\upload\\";
							  //������� ������ �ø�����.
		String uploadFolderPath = getFolder();// == \\2021\\10\\27
							
		log.info("getFolder : "+uploadFolderPath);
		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath);//File�� �Ű������� 2���� ������ ���� �ڿ� ������ �� ����� ������ 2���� �Ű����� �ȿ��� �߰��ϸ� ��
								//  �θ�                   , �ڽ� 
// 		C:\\upload\\2021\\10\\27   C:\\upload\\, 2021\\10\\27
		if (uploadPath.exists() == false) { //exists()�� ���� �����ϴ����� Ȯ���ϴ� �Լ� 
		// C:\\upload\\2021\\10\\27 ������ �������� ������
			uploadPath.mkdirs(); //mkdirs() �޼ҵ�� ������������� �ϴ� �޼ҵ�
		}
		// make yyyy/MM/dd folder
		
		
		
		//   multipartFile �Ϲݺ���(���� �;���):uploadFile(�迭/�÷���Ÿ���� �;���)  uploadFile�� �迭�� ���� MultipartFile�� �������� multipartFile
		for (MultipartFile multipartFile : uploadFile) {
		//			uploadFile[i]���� multipartFile ������
			AttachFileDTO attachDTO = new AttachFileDTO();
			String uploadFileName = multipartFile.getOriginalFilename();
			// IE ������ ������ ������   ??

			// �������� IE�̸�   ??
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
													//������ \\�ڿ� uploadFileName
			log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);

			
			
			UUID uuid = UUID.randomUUID();
			// ���ϸ� �ߺ� ������ ���� ����UUID���ڿ��� uuid ������ ����
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				//����+�����̸�				// ����(�θ�) , �����̸�(�ڽ�)
				multipartFile.transferTo(saveFile);// page 499, transferTo:������ �����ϴ� �޼ҵ�
				//	saveFile(�����ȿ� ���� �̸��� �����Ƿ�)�� multipartFile(��������)�� ���� ��Ŵ 
				
				attachDTO.setUuid(uuid.toString());
								//�����̸� �տ� ���� uuid��
				attachDTO.setUploadPath(uploadFolderPath);
										//���� ���ε� ���
				// check image type file
				if (checkImageType(saveFile)) { //�̹��� ���� �̸�

					attachDTO.setImage(true);
					//�̰��� �̹����ٶ�� ������                                                                                                                      ������
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
		  //FileOutputStream=uploadPath, "s_" + uploadFileName �̷��� ������ ����� �Լ�          ����      ,   s_  + �����̸�
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 400, 400);
				  //�������� ���� Ŭ����.���������� ���·� ����� �޼ҵ�                �������� ����� ���� �������ִ� �޼���
					thumbnail.close();
					//�������� ��������Ƿ� ������ ���� ����(�Ȳ��� �ָ� �������ϱ��� ���� ������ ������)
				}

				// add to List
				list.add(attachDTO);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end for
		return new ResponseEntity<>(list, HttpStatus.OK);
								//���� ������ ����
	}

	
	
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName);
							//��ξȿ��ִ� ������ �̹��������̸�
		File file = new File("C:\\upload\\" + fileName);
							//C:\\upload\\+��ξȿ��ִ� ������ �̹��������̸�
		log.info("file: " + file);

		ResponseEntity<byte[]> result = null;//???

		try {
			HttpHeaders header = new HttpHeaders();
			//HttpHeaders�� Ŭ���̾�Ʈ�� ������ ��û �Ǵ� �������� �ΰ����� ������ ������ �� �� �ְ� �Ѵ�.
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			//�����. �߰�		����Ÿ��                     	���������� ���� �޼ҵ�                   = file�� ������ �����´�.
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
											//������ ������ ���������ο� byte[]�� ������
			//									 body�κ� ���� ����                                  ������� ���� , ����		           
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody					 //�ٿ�ε� �� �� �ִ� ȯ���� header�� �߰�
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
//������ ���� ���ؼ� produces = MediaType.APPLICATION_OCTET_STREAM_VALUE , Resource , @RequestHeader("User-Agent") String userAgent���� �� �־���� 
		Resource resource = new FileSystemResource("C:\\upload\\" + fileName);
//�ٿ�ε带 �Ҽ� �ִ� Ŭ����=Resource
		if (resource.exists() == false) { // �ٿ�ε� �� ������ �������� ������
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); //������ ���ٶ�� �޼��� ������������ ����
		}
		// �ٿ�ε��� ������ �����ͼ� resourceName�� ��ȯ
		String resourceName = resource.getFilename();

		// remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
												//substringƯ���� + 1�� ���� ������ _���� ���� 
		HttpHeaders headers = new HttpHeaders();
		try {

			boolean checkIE = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);
																				//�ͽ��÷ξ �ǹ�																	
			String downloadName = null;

			if (checkIE) { //checkIE�� Ʈ���̸� ���� ���� ������� �������� ���ͳ� �ͽ��÷ξ��� �ǹ�
				//���ͳ� �ͽ��÷ξ� ������ ����
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF8").replaceAll("\\+", " ");
								//�̰��� ���� ������ �ٿ�ε� �� �� �ѱ��� ����
			} else {
				//���ͳ� �ͽ��÷ξ �����ϰ� ó���ϴ� ���
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}

			headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			//			  �ٿ�ε� �Ҽ� �ֵ��� ó��(resource),HttpHeaders��ü�� �̿��ؼ�(headers)
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	

	
	
	
	
//	@PostMapping("/deleteFile")
//	@ResponseBody
//	public ResponseEntity<String> deleteFile(String fileName, String type) {
//
//		log.info("deleteFile: " + fileName);
//
//		File file;
//
//		try {
//			file = new File("C:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
//
//			file.delete();
//
//			if (type.equals("image")) {
//
//				String largeFileName = file.getAbsolutePath().replace("s_", "");
//
//				log.info("largeFileName: " + largeFileName);
//
//				file = new File(largeFileName);
//
//				file.delete();
//			}
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity<String>("deleted", HttpStatus.OK);
//
//	}




}