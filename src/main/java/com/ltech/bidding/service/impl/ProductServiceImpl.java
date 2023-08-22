package com.ltech.bidding.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ltech.bidding.model.*;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.ContentService;
import com.ltech.bidding.service.dto.PriceDto;
import com.ltech.bidding.service.dto.auth.UserInfoShortResponseDto;
import com.ltech.bidding.service.dto.product.*;
import com.ltech.bidding.model.enumeration.ContactMethodType;
import com.ltech.bidding.model.enumeration.ContentType;
import com.ltech.bidding.model.enumeration.PriceStatus;
import com.ltech.bidding.model.enumeration.ProductStatus;
import com.ltech.bidding.repository.PriceRepository;
import com.ltech.bidding.repository.ProductRepository;
import com.ltech.bidding.repository.ProductTypeRepository;
import com.ltech.bidding.service.ProductService;
import com.ltech.bidding.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final ProductTypeRepository productTypeRepository;
    private final FileInfoRepository fileInfoRepository;

    private ProductResponseDto mapProductToResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setProductTypes(product.getProductTypes());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription((product.getDescription()));
        productResponseDto.setStatus(product.getStatus());
        productResponseDto.setCurrentPrice(product.getCurrentPrice());
        productResponseDto.setContactMethodSet(product.getContactMethodSet());
        productResponseDto.setLocation(product.getLocation());
        productResponseDto.setCreated(product.getCreated());
        productResponseDto.setCommentSetId(product.getCommentSet().getId());



        productResponseDto.setAdditionalDataKey(product.getAdditionalData().getId());


        // Assign user
        UserInfoShortResponseDto userInfoShortResponseDto = new UserInfoShortResponseDto();
        userInfoShortResponseDto.setName(product.getUser().getUsername());
        userInfoShortResponseDto.setAvatarUrl(product.getUser().getAvatar().getUrl());
        productResponseDto.setUser(userInfoShortResponseDto);

        // Assign images
        List<FileInfo> images = fileInfoRepository.findByContentKeyIdOrderByPriorityAsc(product.getImages().getId());
        productResponseDto.setImages(images.stream().map(FileInfo::getUrl).collect(Collectors.toSet()));

        return productResponseDto;
    }

    @Override
    public Page<ProductResponseDto> get(Pageable pageable) {
        Page<Product> productPage = productRepository.findByStatus(ProductStatus.AVAILABLE, pageable);;

        return productPage.map((this::mapProductToResponseDto));
    }

    @Override
    public ProductResponseDto get(String id) {
        Product product = productRepository.findById(id).orElseThrow();

        return mapProductToResponseDto(product);
    }

    @Override
    public Product createProduct(ProductRequestDto productRequestDto, AppUser user) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setLocation(productRequestDto.getLocation());
        product.setStatus(ProductStatus.AVAILABLE);
        product.setUser(user);

        // Create images key
        ContentKey imagesContentKey = new ContentKey();
        imagesContentKey.setContentType(ContentType.PRODUCT_IMAGE);
        product.setImages(imagesContentKey);

        // Create additional data key
        ContentKey additionalDataKey = new ContentKey();
        additionalDataKey.setContentType(ContentType.ADDITIONAL_DATA);
        product.setAdditionalData(additionalDataKey);

        // Create contact methods
        ContactMethodSet contactMethodSet = new ContactMethodSet();
        productRequestDto.getContacts().forEach((contact -> {
            ContactMethod contactMethod = new ContactMethod();
            contactMethod.setEnabled(true);
            contactMethod.setType(ContactMethodType.valueOf(contact.getType()));
            contactMethod.setContact(contact.getContact());
            contactMethodSet.getContactMethods().add(contactMethod);
        }));
        product.setContactMethodSet(contactMethodSet);

        // Add Product types
        Set<ProductType> productTypes = productTypeRepository.findByIdIn(productRequestDto.getProductTypeIds());
        product.setProductTypes(productTypes);

        // Add comment set to enable comment
        CommentSet commentSet = new CommentSet();
        product.setCommentSet(commentSet);

        Product savedProduct = productRepository.saveAndFlush(product);

        // Assign initial price
        if(productRequestDto.getPrice() != null) {
            PriceDto priceDto = new PriceDto(savedProduct.getId(), productRequestDto.getPrice(),"Current Price", "ca",productRequestDto.getPriceType());
            createPrice(priceDto,user);
        }

        return savedProduct;
    }

    @Override
    public Product updateProduct(UpdateProductDto productDto, AppUser user) {
        Product product = productRepository.findByIdAndUserId(productDto.getProductId(), user.getId()).orElseThrow();

        if(productDto.getName() != null) product.setName(productDto.getName());
        if(productDto.getDescription() !=null) product.setDescription(productDto.getDescription());
        if(productDto.getStatus() !=null) product.setStatus(productDto.getStatus());

        return productRepository.save(product);
    }

    @SneakyThrows
    @Override
    public Price createPrice(PriceDto priceDto, AppUser user) {
        Product product = productRepository.findByIdAndUserId(priceDto.getProductId(), user.getId()).orElseThrow();

        if(!product.getStatus().equals(ProductStatus.AVAILABLE)) {
            throw new Exception("Not available to create price");
        }

        Price price = new Price();
        price.setPrice(priceDto.getPrice());
        price.setDescription(priceDto.getDescription());
        price.setCurrency(priceDto.getCurrency());
        price.setUser(user);
        price.setStatus(PriceStatus.OPEN);
        price.setProduct(product);
        price.setPriceType(priceDto.getPriceType());

        product.setCurrentPrice(price);

        return priceRepository.save(price);
    }
}
