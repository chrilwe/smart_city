package com.iot.smart_city.model.ucenter;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ScPermission implements Serializable {

    private String id;
    private String role_id;
    private String menu_id;
    private Date create_time;
	
}
