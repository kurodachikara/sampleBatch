package jp.kuroda.sampleBatch.config;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import jp.kuroda.sampleBatch.CsvLineAggregator;
import jp.kuroda.sampleBatch.DemoContext;
import jp.kuroda.sampleBatch.DemoTasklet;
import jp.kuroda.sampleBatch.model.DemoDTO;


@Configuration
@EnableBatchProcessing
public class BathcConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private DemoTasklet demoTasklet;
	@Autowired
	public void BatchConfig(@Lazy DemoTasklet demoTasklet) {
		this.demoTasklet=demoTasklet;
	}
	private Resource outputResource=new FileSystemResource("output/data.csv");
	
	@Bean
	public DemoContext createDemoContext() {
		return new DemoContext();
	}
	
	@Bean
	public FlatFileItemWriter<DemoDTO> writer(){
		FlatFileItemWriter<DemoDTO> writer=new FlatFileItemWriter<>();
		writer.setResource(outputResource);
		writer.setEncoding("UTF-8");
		writer.setLineSeparator("\r\n");
		writer.setAppendAllowed(false);
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			public void writeHeader(Writer arg0) throws IOException{
				arg0.append("\"ID\",\"名前\",\"メールアドレス\"");
			}
		});
		writer.setLineAggregator(new CsvLineAggregator<DemoDTO>() {
			{
				setFieldExtractor(new BeanWrapperFieldExtractor<DemoDTO>() {
					{
						setNames(new String[] {"id","name","mailAddress"});
					}
				});
			}
		});
		return writer;
	}
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("firstStep").tasklet(demoTasklet).build();
	}
	@Bean
	public Job job(Step step1) {
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).start(step1).build();
	}
}
