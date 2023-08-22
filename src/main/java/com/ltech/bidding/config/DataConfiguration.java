package com.ltech.bidding.config;

import com.ltech.bidding.model.AppRole;
import com.ltech.bidding.model.Content;
import com.ltech.bidding.model.ContentKey;
import com.ltech.bidding.model.ProductType;
import com.ltech.bidding.model.enumeration.ContentType;
import com.ltech.bidding.model.enumeration.EmailTemplate;
import com.ltech.bidding.repository.*;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.model.enumeration.UserRole;
import com.ltech.bidding.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Deprecated
public class DataConfiguration {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ContentKeyRepository contentKeyRepository;
    private final ContentRepository contentRepository;

    public DataConfiguration(RoleRepository roleRepository, @Qualifier("AdminAuthService") AuthService authService, UserRepository userRepository,
                             ProductRepository productRepository,
                             ProductTypeRepository productTypeRepository,
                             ContentKeyRepository contentKeyRepository,
                             ContentRepository contentRepository) {
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.contentKeyRepository = contentKeyRepository;
        this.contentRepository = contentRepository;
    }


    public void createDefaultData() {
        try {
            createRoles();
            createDefaultUsers();
            createDefaultProductTypes();

        } catch (Exception ex) {
            log.error(ex.toString());

        }

        try{
            createDefaultEmailTemplate();
        }catch (Exception ex) {}

    }

    private void createDefaultEmailTemplate() {
        ContentKey contentKey = contentKeyRepository.saveAndFlush(new ContentKey(EmailTemplate.ACCOUNT_ACTIVATION.name(), ContentType.TEMPLATE, "Account activation"));

        Content content = new Content();
        content.setContentKeyId(contentKey.getId());
        content.setContent("{{activateToken}}");
        content.setPriority(0);
        contentRepository.save(content);
    }

    private void createRoles() {
        Set<AppRole> defaultRoles = new HashSet<>();

        for (UserRole r : UserRole.values()) {
            if(roleRepository.findByName(r.name()).isPresent()) {
                continue;
            }
            AppRole role = new AppRole();
            role.setName(r.name());

            if(r.name().equals(UserRole.ADMIN.name())) {
                role.setAccess("PRIVATE");
            } else {
                role.setAccess("PUBLIC");
            }

            defaultRoles.add(role);
        }


        roleRepository.saveAll(defaultRoles);
    }

    private void createDefaultUsers() {
        if(userRepository.findByEmail("admin@localhost.com").isEmpty()) {
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto("admin", "admin@localhost.com","Admin@1234","","");
            authService.register(signUpRequestDto);
        }
    }

    private void createDefaultProductTypes() {
        List<String> types = new ArrayList<>();
        types.add("Snow Removal");
        types.add("Landscaping");
        types.add("Lawn Service");

        types.forEach((type) -> {
            ProductType productType = new ProductType();
            productType.setName(type);
            productType.setDescription("");
            productTypeRepository.save(productType);
        });
    }
}
