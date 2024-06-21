package com.example.board_project.security;

import com.example.board_project.auth.UserRole;
import com.example.board_project.entity.SiteUser;
import com.example.board_project.repository.UserRepository;
import com.example.board_project.util.DataNotFoundException;
import com.example.board_project.util.OAuth2TypeMatchNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    // loadUser 메소드 오버라이드
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info(":::::::::::::::userRequest::::::::::::::::::");
        log.info(userRequest.toString());

        log.info(":::::::::::::::OAuth2 user::::::::::::::::::");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String oauthType = clientRegistration.getClientName().toUpperCase();

        log.info("Type : " + oauthType);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((key, value) -> {
            log.info("-----------------------------------------");
            log.info(key + " : " + value);
        });

        String oauthId = oAuth2User.getName();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName : " + userNameAttributeName); // id
        SiteUser siteUser = null;

        if (!"KAKAO".equals(oauthType)) {
            throw new OAuth2TypeMatchNotFoundException();
        }

        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    Map attributesProperties = (Map) paramMap.get("properties");
                    Map attributesKakaoAccount = (Map) paramMap.get("kakao_account");
                    String nickname = (String) paramMap.get("nickname");
                    String email = "%s@kakao.com".formatted(oauthId);
                    String username = "KAKAO_%s".formatted(oauthId);
                    if ((boolean) attributesKakaoAccount.get("has_email")) {
                        email = (String) attributesKakaoAccount.get("email");
                    }
                    siteUser = SiteUser.builder()
                            .email(email)
                            .username(username)
                            .password("")
                            .build();

                    userRepository.save(siteUser);
                }
            }
        }

        else {
            siteUser = userRepository.findByUsername("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(() -> new DataNotFoundException("회원을 찾을 수 없습니다."));
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        return new CustomOAuth2User(siteUser, authorities, paramMap, userNameAttributeName);
    }

    private boolean isNew(String oauthType, String oauthId) {
        return userRepository.findByUsername("%s_%s".formatted(oauthType, oauthId)).isEmpty();
    }
}
