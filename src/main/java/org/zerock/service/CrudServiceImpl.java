package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.domain.CrudVO;
import org.zerock.domain.UploadVO;
import org.zerock.mapper.CrudMapper;
import org.zerock.mapper.UploadMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class CrudServiceImpl implements CrudService {
	public CrudMapper mapper;
	public UploadMapper umapper;
	
	
	@Override
	public List<CrudVO> list() {
		return mapper.list();
	}
	
	@Override
	public void create(CrudVO vo) {
		
		mapper.create(vo);
		
		if(vo.getUploadList()==null || vo.getUploadList().size()<=0 ) {
			return;
		}
			
		vo.getUploadList().forEach(attach->{
				//파일정보           =UploadVO
		attach.setBno(vo.getBno());
		umapper.create(attach);
			});
	}
	
	@Override
	public CrudVO read(int bno) {
		System.out.println("이상없음");
		return mapper.read(bno);
	}

	@Override
	public int update(CrudVO vo) {
		return mapper.update(vo);
	}

	@Override
	public int delete(int bno) {
		return mapper.delete(bno);
	}

	public List<UploadVO> getUploadList(int bno) {
		return umapper.findBno(bno);
	}
}
