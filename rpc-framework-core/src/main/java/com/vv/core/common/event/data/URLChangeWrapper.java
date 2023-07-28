package com.vv.core.common.event.data;

import lombok.Data;

import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/23-16:07
 */
@Data
public class URLChangeWrapper {

    private String serviceName;

    private List<String> providerUrl;

}
