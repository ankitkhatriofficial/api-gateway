package org.khatri.sto.ambassador.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author Ankit Khatri
 */
@Slf4j
public class ControllerUtil {

    public static <T> T jsonToObject(String jsonString, Class<T> returnType){
        if(StringUtils.hasText(jsonString)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(jsonString, returnType);
            } catch (Exception e) {
                log.error("[ControllerUtil] Exception while deserialization:{}", e.getStackTrace());
                log.error("[ControllerUtil] Unable to deserialize object for json:{}", jsonString);
            }
        }
        return null;
    }

}