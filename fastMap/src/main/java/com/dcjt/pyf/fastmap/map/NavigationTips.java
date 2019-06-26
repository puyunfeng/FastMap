package com.dcjt.pyf.fastmap.map;

/**
 * Created by cj on 2019/6/20.
 * desc:
 */
public class NavigationTips {


    private int type ;

    private String label;


    public NavigationTips(int type, String label) {
        this.type = type;
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
