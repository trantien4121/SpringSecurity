package com.trantien.demo.payload;

public enum RoleEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MODERATOR("ROLE_MODERATOR"),;

    private String role;

    RoleEnum(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
