package com.softeng.dingtalk.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class TestFileUtil {
    private static final String Path="C://Users//PC5//Desktop//";

    public static String getPath() {


        return Path;
    }
}
