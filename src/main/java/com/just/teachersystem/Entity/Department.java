package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Department implements Serializable {

  private long id;
  private String dptId;//部门号
  private String dptname;//部门名称

}
