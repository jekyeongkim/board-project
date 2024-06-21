package com.example.board_project.security;

import com.example.board_project.entity.SiteUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
public class CustomOAuth2User extends User implements OAuth2User {

    private final Long id;
    private final String email;
    private final String profileImgUrl;
    private Map<String, Object> attributes;
    private String userNameAttributeName;


    public CustomOAuth2User(SiteUser siteUser, List<GrantedAuthority> authorities) {
        super(siteUser.getUsername(), siteUser.getPassword(), authorities);
        this.id = siteUser.getId();
        this.email = siteUser.getEmail();
        this.profileImgUrl = getProfileImgUrl();
    }


    public CustomOAuth2User(SiteUser siteUser, List<GrantedAuthority> authorities, Map<String, Object> paramMap, String userNameAttributeName) {
        this(siteUser, authorities);
        this.attributes = paramMap;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return this.attributes.get(this.userNameAttributeName).toString();
    }
}
