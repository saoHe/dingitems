package com.ding.dingitems.util.dto.schedule;

import com.dingtalk.api.request.OapiCalendarV2EventCreateRequest;
import lombok.Data;

@Data
public class InParameter {

    private String userId;

    private OapiCalendarV2EventCreateRequest.Event event;

}
