package jp.kuroda.sampleBatch;

import java.util.Arrays;

import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.util.StringUtils;

public class CsvLineAggregator<T> extends ExtractorLineAggregator<T> {
	private String enclose="\"";
	private String deliminater=",";
	
	public void setEnclose(String enclose) {
		this.enclose=enclose;
	}
	public void setDeliminater(String deliminater) {
		this.deliminater=deliminater;
	}
	@Override
	protected String doAggregate(Object[] fields) {
		return StringUtils.collectionToDelimitedString(Arrays.asList(fields), this.deliminater,this.enclose,this.enclose);
	}
	
}
