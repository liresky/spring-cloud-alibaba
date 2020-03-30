/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.custom.SentinelBeanPostProcessor;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;

import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class SentinelRestTemplateTests {

    @Test(expected = BeanCreationException.class)
    public void testFbkMethod() {
        new AnnotationConfigApplicationContext(TestConfig1.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testFbkClass() {
        new AnnotationConfigApplicationContext(TestConfig2.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testblkMethod() {
        new AnnotationConfigApplicationContext(TestConfig3.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testblkClass() {
        new AnnotationConfigApplicationContext(TestConfig4.class);
    }

    @Test
    public void testNormal() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TestConfig5.class);
        assertEquals("RestTemplate size was wrong", 1,
                context.getBeansOfType(RestTemplate.class).size());
    }

    @Test(expected = BeanCreationException.class)
    public void testBlkMethodExists() {
        new AnnotationConfigApplicationContext(TestConfig6.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testFbkMethodExists() {
        new AnnotationConfigApplicationContext(TestConfig7.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testBlkReturnValue() {
        new AnnotationConfigApplicationContext(TestConfig8.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testFbkReturnValue() {
        new AnnotationConfigApplicationContext(TestConfig9.class);
    }

    @Test
    public void testNormalWithoutParam() {
        new AnnotationConfigApplicationContext(TestConfig10.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testUrlClnMethod() {
        new AnnotationConfigApplicationContext(TestConfig11.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testUrlClnClass() {
        new AnnotationConfigApplicationContext(TestConfig12.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testUrlClnMethodExists() {
        new AnnotationConfigApplicationContext(TestConfig13.class);
    }

    @Test(expected = BeanCreationException.class)
    public void testUrlClnReturnValue() {
        new AnnotationConfigApplicationContext(TestConfig14.class);
    }

    @Test
    public void testExceptionHandler4Fallback() {
        new AnnotationConfigApplicationContext(TestConfig15.class);
    }


    @Configuration
    public static class TestConfig1 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(fallback = "fbk")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig2 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(fallbackClass = ExceptionUtil.class)
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig3 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(blockHandler = "blk")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig4 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(blockHandlerClass = ExceptionUtil.class)
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig5 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(blockHandlerClass = SentinelRestTemplateTests.ExceptionUtil.class, blockHandler = "handleException", fallbackClass = SentinelRestTemplateTests.ExceptionUtil.class, fallback = "fallbackException", urlCleanerClass = SentinelRestTemplateTests.UrlCleanUtil.class, urlCleaner = "clean")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig6 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(blockHandlerClass = SentinelRestTemplateTests.ExceptionUtil.class, blockHandler = "handleException1")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig7 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(fallbackClass = SentinelRestTemplateTests.ExceptionUtil.class, fallback = "fallbackException1")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig8 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(blockHandlerClass = SentinelRestTemplateTests.ExceptionUtil.class, blockHandler = "handleException2")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig9 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(fallbackClass = SentinelRestTemplateTests.ExceptionUtil.class, fallback = "fallbackException2")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig10 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate
        RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        @SentinelRestTemplate
        RestTemplate restTemplate2() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig11 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(urlCleaner = "cln")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig12 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(urlCleanerClass = UrlCleanUtil.class)
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig13 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(urlCleanerClass = SentinelRestTemplateTests.UrlCleanUtil.class, urlCleaner = "clean1")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig14 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(urlCleanerClass = SentinelRestTemplateTests.UrlCleanUtil.class, urlCleaner = "clean2")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Configuration
    public static class TestConfig15 {
        @Bean
        SentinelBeanPostProcessor sentinelBeanPostProcessor(
                ApplicationContext applicationContext) {
            return new SentinelBeanPostProcessor(applicationContext);
        }

        @Bean
        @SentinelRestTemplate(fallbackClass = TestConfig15.class,
                fallback = "fallbackHandler",
                blockHandlerClass = TestConfig15.class,
                blockHandler = "flowHandler")
        RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public SentinelRestTemplateExceptionHandler globalExceptionHandler() {
            return new SentinelRestTemplateExceptionHandler();
        }

        public static SentinelClientHttpResponse flowHandler(HttpRequest request,
                                                             byte[] body,
                                                             ClientHttpRequestExecution execution,
                                                             BlockException exception) {
            if (exception instanceof FlowException) {
                throw new SentinelRestTemplateFlowException(exception.getMessage(), exception);
            }
            return new SentinelClientHttpResponse("Oops flowHandler");
        }

        public static SentinelClientHttpResponse fallbackHandler(HttpRequest request,
                                                                 byte[] body,
                                                                 ClientHttpRequestExecution execution,
                                                                 BlockException exception) {
            if (exception instanceof DegradeException) {
                throw new SentinelRestTemplateDegradeException(exception.getMessage(), exception);
            }
            return new SentinelClientHttpResponse("Oops fallback");
        }

        public static class SentinelRestTemplateFlowException extends RuntimeException {
            SentinelRestTemplateFlowException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        public static class SentinelRestTemplateDegradeException extends RuntimeException {
            SentinelRestTemplateDegradeException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        @ControllerAdvice
        public class SentinelRestTemplateExceptionHandler {

            @ExceptionHandler(SentinelRestTemplateFlowException.class)
            public ResponseEntity<String> restTemplateFlowExceptionHandler(SentinelRestTemplateFlowException exception) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("PS: have a rest.");
            }

            @ExceptionHandler(SentinelRestTemplateDegradeException.class)
            public ResponseEntity<String> restTemplateDegradeExceptionHandler(SentinelRestTemplateDegradeException exception) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("PS: " + HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
            }
        }
    }

    public static class ExceptionUtil {
        public static SentinelClientHttpResponse handleException(HttpRequest request,
                                                                 byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
            System.out.println("Oops: " + ex.getClass().getCanonicalName());
            return new SentinelClientHttpResponse("Oops");
        }

        public static void handleException2(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution, BlockException ex) {
            System.out.println("Oops: " + ex.getClass().getCanonicalName());
        }

        public static SentinelClientHttpResponse fallbackException(HttpRequest request,
                                                                   byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
            System.out.println("Oops: " + ex.getClass().getCanonicalName());
            return new SentinelClientHttpResponse("Oops fallback");
        }

        public static void fallbackException2(HttpRequest request, byte[] body,
                                              ClientHttpRequestExecution execution, BlockException ex) {
            System.out.println("Oops: " + ex.getClass().getCanonicalName());
        }

    }

    public static class UrlCleanUtil {
        public static String clean(String url) {
            return url;
        }

        public static void clean2(String url) {
        }
    }


}
