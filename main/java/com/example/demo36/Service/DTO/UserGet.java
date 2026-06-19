package com.example.demo36.Service.DTO;

import vn.saolasoft.base.service.dto.DtoGet;
import com.example.demo36.Entity.User;

import java.util.Date;

public class UserGet extends DtoGet<User, Long>  {
    private String username;
    private Date dateCreated;

    public UserGet() {
    }

    public UserGet(User u) {
        super(u);
    }

    @Override
    public void parse(User u) {
        this.username = u.getUsername();
        this.dateCreated = u.getDateCreated();
    }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
}
