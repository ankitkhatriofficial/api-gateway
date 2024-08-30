package org.khatri.sto.ambassador.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ankit Khatri
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenEndPointConfig {

    private final ApplicationContext applicationContext;

    @Bean("allOpenEndPointsBean")
    public String[] scanAllOpenEndPoints(){
        List<String> allOpenEndPoints = new LinkedList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents("org.khatri.sto.ambassador.controller");
        beanDefinitionSet.stream().forEach(beanDefinition -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                String[] classLevelEndPoints = this.extractClassLevelEndPoint(clazz);
                String[] methodLevelEndPoints = this.extractMethodLevelEndPoint(clazz);
                allOpenEndPoints.addAll(this.mergeClassAndMethodEndPoints(classLevelEndPoints, methodLevelEndPoints));
            } catch (Exception ex) {
                log.error("[ERROR][OpenEndPointConfig] Error while scanning :{}, ex:{}", OpenEndPoint.class.getCanonicalName(), ex.getStackTrace());
                SpringApplication.exit(this.applicationContext);
                System.exit(1);
            }
        });
        return allOpenEndPoints.toArray(new String[0]);
    }

    private List<String> mergeClassAndMethodEndPoints(String[] classLevelEndPoints, String[] methodLevelEndPoints) {
        List<String> allOpenEndPoints = new LinkedList<>();
        if(classLevelEndPoints.length == 0) return Arrays.stream(methodLevelEndPoints).collect(Collectors.toList());
        else if(methodLevelEndPoints.length == 0) return Arrays.stream(classLevelEndPoints).collect(Collectors.toList());
        Arrays.stream(classLevelEndPoints).forEach(classLevelEndPoint -> {
            Arrays.stream(methodLevelEndPoints).forEach(methodLevelEndPoint -> {
                allOpenEndPoints.add(classLevelEndPoint.concat(methodLevelEndPoint));
            });
        });
        return allOpenEndPoints;
    }

    private String[] extractClassLevelEndPoint(Class<?> clazz) {
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if(requestMapping != null){
            return requestMapping.value();
        }
        return new String[0];
    }

    private String[] extractMethodLevelEndPoint(Class<?> clazz) {
        List<String> openEndPoints = new LinkedList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (clazz.getAnnotation(OpenEndPoint.class) != null || AnnotationUtils.findAnnotation(method, OpenEndPoint.class) != null) {
                String[] endPoints = this.extractEndPoints(method);
                openEndPoints.addAll(Arrays.stream(endPoints).collect(Collectors.toList()));
            }
        }
        return openEndPoints.toArray(new String[0]);
    }

    private String[] extractEndPoints(Method method){
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if(requestMapping != null){
            return requestMapping.value();
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if(postMapping != null){
            return postMapping.value();
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if(getMapping != null){
            return getMapping.value();
        }
        PutMapping putMapping  = method.getAnnotation(PutMapping.class);
        if(putMapping != null){
            return putMapping.value();
        }
        PatchMapping patchMapping  = method.getAnnotation(PatchMapping.class);
        if(patchMapping != null){
            return patchMapping.value();
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if(deleteMapping != null){
            return deleteMapping.value();
        }
        return new String[0];
    }

}
