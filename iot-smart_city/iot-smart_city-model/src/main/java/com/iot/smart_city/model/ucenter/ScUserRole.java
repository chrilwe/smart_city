package com.iot.smart_city.model.ucenter;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ScUserRole implements Serializable {

    private String id;
    private String userId;
    private String roleId;
    private String creator;
    private Date createTime;
	
}
