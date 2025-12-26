package com.sfl.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/***
 * Make custom user details class for security configuration adaptor
 * By this way we can set/capture own custom user claims form token
 */
public class ApplicationUserDetails extends User {
    private Integer id; // User ID
    public ApplicationUserDetails(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
