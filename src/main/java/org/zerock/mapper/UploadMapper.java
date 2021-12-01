package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.UploadVO;

public interface UploadMapper {
	public void create(UploadVO vo);// ���� ���ε��� �������� tbl_attach���̺� insert��
	public List<UploadVO> findBno(int bno);// ���� ���ε��Ѱ͵��� ����ڰ� �� �� �ֵ��� ȭ�鿡 ��ȸ�� �� �ֵ��� ��.
	public int delete(String uuid);// ���� ���ε��� �������� tbl_attach���̺� delete��
	public void deleteAll(int bno);
}
