package cn.fuyoushuo.domain.entity;

import java.io.Serializable;

/**
 * Created by QA on 2017/3/2.
 */

public class NewType implements Serializable {


    private String typeCode;

    private String typeName;

    private boolean red = false;

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public NewType(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public NewType bindRed(boolean isRed){
        this.red = isRed;
        return this;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
