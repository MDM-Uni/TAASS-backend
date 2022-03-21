package com.petlife.utentemicroservizio.models;


import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Date;

public interface Evento {
   public Long getId();
   public Date getData();
}
