package com.trantien.demo.payload;

public enum RoleEnum {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_MODERATOR("mod");

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
