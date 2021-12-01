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
							//두개 이상을 선택하기 위해 배열에다가 저장
		String uploadFolder = "C:\\upload";
						//어디다가 파일 업로드 할껀지의 경로
		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			//C:\\upload에 실제파일명 문자열을 saveFile변수에 저장
			log.info("saveFile : "+saveFile);
			try { 
				multipartFile.transferTo(saveFile);
							//transferTo(파일명) : 파일명으로 저장
			} catch (Exception e) {
				log.error(e.getMessage());
			}  //end catch
		}  //end for

	}

	
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {

		log.info("upload ajax");
	}

	
	
	
	
	private String getFolder() {//이해함 ㅇㅇㅇ

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//날짜형태(format)를 년/월/일 형태로 sdf변수에 저장 
		Date date = new Date();
		//오늘날짜를 저장하는 date변수에 저장 
		String str = sdf.format(date);
		//오늘날짜를  년/월/일 형태(format)로 str변수에 저장

		return str.replace("-", File.separator);
				 // 치환함수 // -를   \\로 변경시켜줌(ex.2021-10-27 ->2021\\10\\27
	}

	
	
	
	
	
	private boolean checkImageType(File file) {//

		try {
			String contentType = Files.probeContentType(file.toPath());
					//받은 타입이                     파일형식을 보는 메소드                   = file의 형식을 가져온다.
			return contentType.startsWith("image");
					//이미지타입이면(true) 리턴
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
								//배열0번쨰 사진의 진짜 이름
		//여러개의파일을 선택(ArrayList)해서 파일정보들을 AttachFileDTO에 저장
		List<AttachFileDTO> list = new ArrayList<>();
		//실제 파일이 저장되는 경로
		String uploadFolder = "C:\\upload\\";
							  //어느곳에 파일을 올릴껀지.
		String uploadFolderPath = getFolder();// == \\2021\\10\\27
							
		log.info("getFolder : "+uploadFolderPath);
		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath);//File은 매개변수가 2개인 생성자 만약 뒤에 폴더를 더 만들고 싶으면 2개의 매개변수 안에다 추가하면 됨
								//  부모                   , 자식 
// 		C:\\upload\\2021\\10\\27   C:\\upload\\, 2021\\10\\27
		if (uploadPath.exists() == false) { //exists()는 폴더 존재하는지를 확인하는 함수 
		// C:\\upload\\2021\\10\\27 폴더가 존재하지 않으면
			uploadPath.mkdirs(); //mkdirs() 메소드는 폴더생성기능을 하는 메소드
		}
		// make yyyy/MM/dd folder
		
		
		
		//   multipartFile 일반변수(명이 와야함):uploadFile(배열/컬렉션타입이 와야함)  uploadFile의 배열을 가진 MultipartFile에 변수명을 multipartFile
		for (MultipartFile multipartFile : uploadFile) {
		//			uploadFile[i]값을 multipartFile 저장함
			AttachFileDTO attachDTO = new AttachFileDTO();
			String uploadFileName = multipartFile.getOriginalFilename();
			// IE 제외한 나머지 브라우저   ??

			// 브라우저가 IE이면   ??
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
													//마지막 \\뒤에 uploadFileName
			log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);

			
			
			UUID uuid = UUID.randomUUID();
			// 파일명 중복 방지를 위한 랜덤UUID문자열을 uuid 변수에 저장
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				//폴더+파일이름				// 폴더(부모) , 파일이름(자식)
				multipartFile.transferTo(saveFile);// page 499, transferTo:파일을 저장하는 메소드
				//	saveFile(폴더안에 파일 이름만 있으므로)에 multipartFile(파일정보)를 저장 시킴 
				
				attachDTO.setUuid(uuid.toString());
								//파일이름 앞에 붙을 uuid값
				attachDTO.setUploadPath(uploadFolderPath);
										//파일 업로드 경로
				// check image type file
				if (checkImageType(saveFile)) { //이미지 파일 이면

					attachDTO.setImage(true);
					//이것은 이미지다라고 말해줌                                                                                                                      생성자
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
		  //FileOutputStream=uploadPath, "s_" + uploadFileName 이렇게 파일을 만드는 함수          폴더      ,   s_  + 파일이름
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 400, 400);
				  //섬네일을 관련 클래스.섬네일파일 형태로 만드는 메소드                섬네일을 만들기 위해 연결해주는 메서드
					thumbnail.close();
					//섬네일을 만들었으므로 연결을 끊기 위해(안끊어 주면 원본파일까지 같이 나오기 때문에)
				}

				// add to List
				list.add(attachDTO);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end for
		return new ResponseEntity<>(list, HttpStatus.OK);
								//파일 정보를 저장
	}

	
	
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName);
							//경로안에있는 섬네일 이미지파일이름
		File file = new File("C:\\upload\\" + fileName);
							//C:\\upload\\+경로안에있는 섬네일 이미지파일이름
		log.info("file: " + file);

		ResponseEntity<byte[]> result = null;//???

		try {
			HttpHeaders header = new HttpHeaders();
			//HttpHeaders는 클라이언트와 서버가 요청 또는 응답으로 부가적인 정보를 전송을 할 수 있게 한다.
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			//헤더에. 추가		받은타입                     	파일형식을 보는 메소드                   = file의 형식을 가져온다.
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
											//지정한 파일의 정보를새로운 byte[]로 저장함
			//									 body부분 값을 전송                                  헤더값을 전송 , 상태		           
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody					 //다운로드 할 수 있는 환경을 header에 추가
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
//마임을 쓰기 위해서 produces = MediaType.APPLICATION_OCTET_STREAM_VALUE , Resource , @RequestHeader("User-Agent") String userAgent들이 꼭 있어야함 
		Resource resource = new FileSystemResource("C:\\upload\\" + fileName);
//다운로드를 할수 있는 클래스=Resource
		if (resource.exists() == false) { // 다운로드 할 파일이 존재하지 않으며
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); //파일이 없다라는 메세지 웹브라우저에게 전달
		}
		// 다운로드할 파일을 가져와서 resourceName에 전환
		String resourceName = resource.getFilename();

		// remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
												//substring특성상 + 1을 하지 않으면 _부터 나옴 
		HttpHeaders headers = new HttpHeaders();
		try {

			boolean checkIE = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);
																				//익스플로어를 의미																	
			String downloadName = null;

			if (checkIE) { //checkIE가 트루이면 지금 현재 사용자의 브라우저가 인터넷 익스플로어라는 의미
				//인터넷 익스플로어 브라우저 전용
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF8").replaceAll("\\+", " ");
								//이것을 하지 않으면 다운로드 할 때 한글이 깨짐
			} else {
				//인터넷 익스플로어를 제외하고 처리하는 방식
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}

			headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			//			  다운로드 할수 있도록 처리(resource),HttpHeaders객체를 이용해서(headers)
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