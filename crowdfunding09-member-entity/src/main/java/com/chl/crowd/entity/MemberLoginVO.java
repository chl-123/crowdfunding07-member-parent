package com.chl.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.javassist.SerialVersionUID;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO implements Serializable {
    private static final long serialVersionUID=1l;
    private Integer id;
    private String username;
    private String email;


}
