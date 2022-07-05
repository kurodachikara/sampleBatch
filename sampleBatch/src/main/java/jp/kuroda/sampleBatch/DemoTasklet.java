package jp.kuroda.sampleBatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.kuroda.sampleBatch.model.DemoDTO;
import jp.kuroda.sampleBatch.repository.DemoRepository;

@Component()
public class DemoTasklet implements Tasklet {
	private Logger logger=LoggerFactory.getLogger(DemoTasklet.class);
	
	@Autowired
	private DemoRepository demoRepository;
	@Autowired
	private ItemStreamWriter<DemoDTO> writer;
	@Autowired
	private DemoContext demoContext;
	
	public RepeatStatus execute(StepContribution stepContribution,ChunkContext chunkContext) throws Exception {
		logger.debug("DemoTasklet execute 開始");
		writer.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
		try(Cursor<DemoDTO> result=demoRepository.select()){
			List<DemoDTO> data=new ArrayList<>();
			for(DemoDTO dto:result) {
				data.add(dto);
				if(data.size()>=demoContext.getWriteSize()) {
					writer.write(data);
					data.clear();
				}
			}
			if(data.size()>0)writer.write(data);
		}
		writer.close();
		logger.debug("DemoTasklet excute 修了");
		return RepeatStatus.FINISHED;
	}

}
