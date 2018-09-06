package com.spring.example.videotogif.autoconfigure;

import javax.inject.Inject;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.spring.example.videotogif.autoconfigure.properties.VideoToGifProperties;
import com.spring.example.videotogif.autoconfigure.service.GifEncoderService;
import com.spring.example.videotogif.autoconfigure.service.VideoConvertorService;
import com.spring.example.videotogif.autoconfigure.service.VideoDecoderService;

@Configuration
@ConditionalOnClass({ FFmpegFrameGrabber.class, AnimatedGifEncoder.class })
@EnableConfigurationProperties(VideoToGifProperties.class)
public class VideotogifAutoConfiguration {

	@Inject
	private VideoToGifProperties videoToGifProperties;

	@ConditionalOnProperty(prefix = "com.videotogif", name = "creaete-result-dir")
	public void createResultDirectory() {
		if (!videoToGifProperties.getGifLocation().exists()) {
			videoToGifProperties.getGifLocation().mkdirs();
		}
	}

	@Bean
	@ConditionalOnMissingBean(VideoDecoderService.class)
	public VideoDecoderService videoDecoderService() {
		return new VideoDecoderService();
	}

	@Bean
	@ConditionalOnMissingBean(GifEncoderService.class)
	public GifEncoderService gifEncoderService() {
		return new GifEncoderService();
	}

	@Bean
	@ConditionalOnMissingBean(VideoConvertorService.class)
	public VideoConvertorService videoConvertorService() {
		return new VideoConvertorService();
	}

	@Configuration
	@ConditionalOnWebApplication
	public static class WebConfiguration implements WebMvcConfigurer {

		@Value("${multipart.location}/gif/")
		private String videoGifLocation;

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/gif/**").addResourceLocations("file:" + videoGifLocation);

		}

		@Bean
		@ConditionalOnProperty(prefix = "com.videotogif", name = "optimize")
		public FilterRegistrationBean<HiddenHttpMethodFilter> deRegisterHiddenHttpMethodFilter(
				HiddenHttpMethodFilter hiddenHttpMethodFilter) {
			FilterRegistrationBean<HiddenHttpMethodFilter> filterRegistrationBean = new FilterRegistrationBean<>(
					hiddenHttpMethodFilter);
			filterRegistrationBean.setEnabled(false);
			return filterRegistrationBean;
		}

		@Bean
		@ConditionalOnProperty(prefix = "com.videotogif", name = "optimize")
		public FilterRegistrationBean<HttpPutFormContentFilter> deRegisterHttpPutFormContentFilter(
				HttpPutFormContentFilter httpPutFormContentFilter) {
			FilterRegistrationBean<HttpPutFormContentFilter> filterRegistrationBean = new FilterRegistrationBean<>(
					httpPutFormContentFilter);
			filterRegistrationBean.setEnabled(false);
			return filterRegistrationBean;
		}

		@Bean
		@ConditionalOnProperty(prefix = "com.videotogif", name = "optimize")
		public FilterRegistrationBean<RequestContextFilter> deRegisterRequestContextFilter(
				RequestContextFilter requestContextFilter) {
			FilterRegistrationBean<RequestContextFilter> filterRegistrationBean = new FilterRegistrationBean<>(
					requestContextFilter);
			filterRegistrationBean.setEnabled(false);
			return filterRegistrationBean;
		}
	}

}