package com.itheima.service;

import java.util.Map;

/**
 * @author pc
 *报表业务层接口
 */
public interface ReportService {
    /**
     * excel报表
     * @return
     */
    public Map<String, Object> exportBusinessReport() throws Exception;

}
