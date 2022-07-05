package jp.kuroda.sampleBatch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="demo")
@Data
public class DemoContext {
	private int writeSize;
}
