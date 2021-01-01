package com.sutpc.transpaas.algoserver.config;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/** Mvc配置. */
@Slf4j
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

  private static final SerializerFeature[] features = {
    // 输出空置字段
    SerializerFeature.WriteMapNullValue,
    // list字段如果为null，输出为[]，而不是null
    SerializerFeature.WriteNullListAsEmpty,
    // 数值字段如果为null，输出为0，而不是null
    // SerializerFeature.WriteNullNumberAsZero,
    // Boolean字段如果为null，输出为false，而不是null
    SerializerFeature.WriteNullBooleanAsFalse,
    // 字符类型字段如果为null，输出为""，而不是null
    SerializerFeature.WriteNullStringAsEmpty,
    SerializerFeature.DisableCircularReferenceDetect,
    SerializerFeature.MapSortField
  };

  /**
   * session.
   *
   * @return LocaleResolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.CHINESE);
    return slr;
  }

  /**
   * 配置.
   *
   * @return CorsConfiguration对象
   */
  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOrigin("*"); // 1
    corsConfiguration.addAllowedHeader("*"); // 2
    corsConfiguration.addAllowedMethod("*"); // 3
    corsConfiguration.addExposedHeader(
        "Authorization,Server,"
        + "Date,Content-Type,Transfer-Encoding,Connection,"
        + "Content-Disposition,Vary");
    return corsConfiguration;
  }

  /**
   * 配置.
   *
   * @return CorsFilter
   */
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
  }

  /**
   * requestHandler.
   *
   * @return RequestMappingHandlerAdapter
   */
  @Bean
  public RequestMappingHandlerAdapter requestHandler() {
    RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
    ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
    FormattingConversionService conversionService = new DefaultFormattingConversionService();
    conversionService.addConverter(addDateConvert());
    initializer.setConversionService(conversionService);
    adapter.setWebBindingInitializer(initializer);
    adapter.getMessageConverters().add(buildMessageConvertor());
    return adapter;
  }

  @Bean
  public HttpMessageConverters fastJsonHttpMessageConverters() {

    return new HttpMessageConverters(buildMessageConvertor());
  }

  /**
   * buildMessageConvertor.
   *
   * @return FastJsonHttpMessageConverter
   */
  public FastJsonHttpMessageConverter buildMessageConvertor() {

    // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

    SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
    serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
    serializeConfig.put(Long.class, ToStringSerializer.instance);
    serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
    ObjectSerializer doubleSerializer =
        new ContextObjectSerializer() {
          @Override
          public void write(JSONSerializer serializer, Object object, BeanContext context)
              throws IOException {

            SerializeWriter out = serializer.out;
            if (object == null) {
              out.writeString("");
              return;
            }
            String format = null;
            if (context != null) {
              format = context.getFormat();
            }
            if (StringUtils.isBlank(format)) {
              format = "0.0000";
            }
            if (object == null) {
              out.writeNull();
              return;
            }

            DecimalFormat decimalFormat = new DecimalFormat(format);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            out.writeString(decimalFormat.format(object));
          }

          @Override
          public void write(
              JSONSerializer serializer,
              Object object,
              Object fieldName,
              java.lang.reflect.Type fieldType,
              int features)
              throws IOException {
            SerializeWriter out = serializer.getWriter();
            DecimalFormat df = new DecimalFormat("0.0000"); // 四舍五入
            df.setRoundingMode(RoundingMode.FLOOR);
            if (object == null) {
              out.writeNull();
              return;
            }
            out.writeString(df.format(object));
          }
        };

    serializeConfig.put(Double.TYPE, doubleSerializer);
    serializeConfig.put(Double.class, doubleSerializer);

    serializeConfig.put(Float.TYPE, doubleSerializer);
    serializeConfig.put(Float.class, doubleSerializer);
    serializeConfig.put(BigDecimal.class, doubleSerializer);

    fastJsonConfig.setSerializeConfig(serializeConfig);
    fastJsonConfig.setSerializerFeatures(features);
    //定义一个converters转换消息的对象
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    //在converter中添加配置信息
    fastConverter.setFastJsonConfig(fastJsonConfig);
    List<MediaType> list = new ArrayList<MediaType>();
    list.add(MediaType.APPLICATION_JSON_UTF8);
    fastConverter.setSupportedMediaTypes(list);
    return fastConverter;
  }

  /**
   * 日期格式.
   *
   * @return 转换结果
   */
  @Bean
  public Converter<String, Date> addDateConvert() {
    return new Converter<String, Date>() {
      @Override
      public Date convert(String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (StringUtils.trimToNull(source) == null) {
          return date;
        }
        try {
          date = sdf.parse((String) source);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        return date;
      }
    };
  }

  /**
   * restTemplate.
   *
   * @param factory 工厂
   * @return RestTemplate对象
   */
  @Bean
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    return new RestTemplate(factory);
  }

  /**
   * ClientHttpRequestFactory.
   *
   * @return ClientHttpRequestFactory
   */
  @Bean
  public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setReadTimeout(5000); // 单位为ms
    factory.setConnectTimeout(5000); // 单位为ms
    return factory;
  }
}
