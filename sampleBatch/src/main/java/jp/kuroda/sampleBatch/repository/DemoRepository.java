package jp.kuroda.sampleBatch.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;

import jp.kuroda.sampleBatch.model.DemoDTO;

@Mapper
public interface DemoRepository {

		Cursor<DemoDTO> select();
}
