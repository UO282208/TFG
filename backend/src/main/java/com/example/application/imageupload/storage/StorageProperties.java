package com.example.application.imageupload.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageProperties {

	private String location = "storefiles/";
	private String resultsLocation = "storefiles/results";

}