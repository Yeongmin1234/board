package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {
	private String uuid;//pk

	private String uploadpath;//파일업로드 경로
	
	private String filename;//파일이름	
	
	private String filetype;//이미지 파일 여부
	
	private boolean image;//이미지 파일 여부
	
	private int bno;//게시판 번호
	
}
