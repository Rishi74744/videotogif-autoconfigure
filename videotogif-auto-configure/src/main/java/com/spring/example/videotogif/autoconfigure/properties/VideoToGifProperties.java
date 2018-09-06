package com.spring.example.videotogif.autoconfigure.properties;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.videotogif")
public class VideoToGifProperties {

	private File gifLocation;

	private boolean optimize;

	public File getGifLocation() {
		return gifLocation;
	}

	public void setGifLocation(File gifLocation) {
		this.gifLocation = gifLocation;
	}

	public boolean isOptimize() {
		return optimize;
	}

	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}

}