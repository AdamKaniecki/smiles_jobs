package pl.zajavka.infrastructure.security;


import org.springframework.security.core.GrantedAuthority;

public enum  Role
        implements GrantedAuthority
{
    CANDIDATE,
    ADMIN,
    COMPANY;


    @Override
    public String getAuthority() {
        return name();
    }
}
