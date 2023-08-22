package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.ContentKey;
import com.ltech.bidding.model.FileInfo;
import com.ltech.bidding.model.enumeration.ContentType;
import com.ltech.bidding.model.enumeration.EmailTemplate;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.dto.contact.SendEmailDto;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.model.enumeration.UserRole;
import com.ltech.bidding.repository.ProfileRepository;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.AbstractAuthService;
import com.ltech.bidding.service.ContentService;
import com.ltech.bidding.service.EmailService;
import com.ltech.bidding.service.RoleService;
import com.ltech.bidding.service.enumeration.ProcessUserType;
import com.ltech.bidding.util.JwtTokenProvider;
import com.ltech.bidding.util.TokenGenerator;
import com.ltech.bidding.util.VerifyUserChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Slf4j
@Transactional
@Service("UserAuthService")
public class UserAuthServiceImpl extends AbstractAuthService {
    private final  EmailService emailService;
    private final VerifyUserChain verifyUserChain;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ContentService contentService;
    private final ProfileRepository profileRepository;
    private final FileInfoRepository fileInfoRepository;
    private final TokenGenerator tokenGenerator;

    public UserAuthServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, TokenGenerator tokenGenerator, EmailService emailService, VerifyUserChain verifyUserChain, RoleService roleService, ContentService contentService, ProfileRepository profileRepository, FileInfoRepository fileInfoRepository, TokenGenerator tokenGenerator1) {
        super(passwordEncoder, authenticationManager, tokenProvider, userRepository);
        this.emailService = emailService;
        this.verifyUserChain = verifyUserChain;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.contentService = contentService;
        this.profileRepository = profileRepository;
        this.fileInfoRepository = fileInfoRepository;
        this.tokenGenerator = tokenGenerator1;
    }

    @Override
    public AppUser register(SignUpRequestDto signUpRequest) {
        verifyUserChain
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .verifyPassword()
                .verifyEmail()
                .isValid();

        AppUser user = super.register(signUpRequest);

        // Set user default avatar
        FileInfo avatar = fileInfoRepository.findByContentKeyIdOrderByPriorityAsc(ContentType.AVATAR.name()).get(0);
        user.setAvatar(avatar);

        roleService.assignUserNewRole(user, UserRole.USER.name());

        String activateToken = UUID.randomUUID().toString();
        user.setActivateToken(activateToken);

        // 30 mins
        user.setActivateTokenExpiredAt(new Date(new Date().getTime() + 1800000));

        // Send activation email
        Map<String, String> emailParams = new HashMap<>();
        emailParams.put("activateToken", activateToken);
        emailParams.put("username", user.getUsername());
        String emailTemplate = contentService.get(EmailTemplate.ACCOUNT_ACTIVATION.name());
        String emailContent  = StringSubstitutor.replace(emailTemplate, emailParams, "{{", "}}" );
        SendEmailDto sendEmailDto = new SendEmailDto(signUpRequest.getEmail(), "Email Confirmation", emailContent);

        emailService.send(sendEmailDto);
        userRepository.save(user);

        return user;
    }

    @Override
    public boolean resetPassword(String newPassword, String token) {
        try {
            verifyUserChain.password(newPassword).verifyPassword();

            super.resetPassword(newPassword, token);

            return true;
        } catch (Exception ex) {
            log.error(ex.toString());
            return false;
        }

    }


    public Map<String, String> processToken(String email, ProcessUserType processUserType) {
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        Map<String,String > paramsMap;
        String subject;

        switch (processUserType) {
            case RESET_PASSWORD -> {
                paramsMap = attemptResetPassword(user);
                subject = "Reset your password";
            }
            case ACCOUNT_ACTIVATION -> {
                paramsMap= attemptActivateAccount(user);
                subject = "Activate your account";
            }
            default -> {
                paramsMap = new HashMap<>();
                subject = "";
            }
        }

        String emailTemplateContent = contentService.get(processUserType.name());
        String emailContent  = StringSubstitutor.replace(emailTemplateContent, paramsMap, "{{", "}}" );

        SendEmailDto sendEmailDto = new SendEmailDto(email, subject, emailContent);

        if(emailService.send(sendEmailDto)) {
            return paramsMap;
        };

        return null;

    }

    private Map<String,String> attemptResetPassword(AppUser user) {
        String resetToken = tokenGenerator.generateDigitsToken();

        user.setPwdResetToken(resetToken);
        // 30 mins
        user.setPwdResetTokenExpiredAt(new Date(new Date().getTime() + 1800000));

        userRepository.save(user);

        Map<String,String > valueMap = new HashMap<>();
        valueMap.put("resetToken", resetToken);

        return valueMap;
    }

    private Map<String,String> attemptActivateAccount(AppUser user) {
        String activateToken = UUID.randomUUID().toString();
        user.setActivateToken(activateToken);
        // 30 mins
        user.setActivateTokenExpiredAt(new Date(new Date().getTime() + 1800000));

        userRepository.save(user);

        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("activateToken", activateToken);
        valueMap.put("username", user.getUsername());

        return valueMap;
    }
}
