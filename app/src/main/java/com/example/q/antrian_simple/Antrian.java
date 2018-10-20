package com.example.q.antrian_simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Antrian {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("no")
    @Expose
    private Integer no;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

}
