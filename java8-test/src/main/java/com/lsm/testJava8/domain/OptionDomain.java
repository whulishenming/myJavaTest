package com.lsm.testJava8.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDomain {

    private User user;

    private String stringDomain;

    private Long longDomain;

}
