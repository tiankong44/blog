package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {
    private Long id;
    private String imgName;
    private String path;
    private Long userId;
    private Date uploadDate;


}
