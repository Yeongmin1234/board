package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {
	private String uuid;//pk

	private String uploadpath;//���Ͼ��ε� ���
	
	private String filename;//�����̸�	
	
	private String filetype;//�̹��� ���� ����
	
	private int bno;//�Խ��� ��ȣ
	
}
