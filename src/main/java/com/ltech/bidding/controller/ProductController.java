package com.ltech.bidding.controller;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.Product;
import com.ltech.bidding.model.UserPrincipal;
import com.ltech.bidding.repository.ProductTypeRepository;
import com.ltech.bidding.service.ContentService;
import com.ltech.bidding.service.dto.PriceDto;
import com.ltech.bidding.service.dto.product.ProductRequestDto;
import com.ltech.bidding.service.dto.product.UpdateProductDto;
import com.ltech.bidding.model.enumeration.ProductStatus;
import com.ltech.bidding.repository.ProductRepository;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/product")
@RestController
public class ProductController {
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ContentService contentService;

    @GetMapping
    public ResponseEntity<?> getProducts(Pageable pageable) {
        return ResponseEntity.ok().body(productService.get(pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok().body(productService.get(productId));
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok().body(productService.createProduct(productRequestDto, userPrincipal.user()));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody UpdateProductDto productDto ,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        AppUser user = new AppUser();

        user.setId(userPrincipal.getId());
        productDto.setProductId(productId);

        return ResponseEntity.ok().body(productService.updateProduct(productDto, user));
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/price")
    public ResponseEntity<?> createPrice(@RequestBody PriceDto priceDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok().body(productService.createPrice(priceDto, userPrincipal.user()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/detail/{productId}")
    public ResponseEntity<?> createDetail(@RequestBody String data, @PathVariable String productId) {
        Product product = productRepository.getReferenceById(productId);

        String additionalDataId = product.getAdditionalData().getId();

        contentService.create(data,additionalDataId,0);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/detail/{productId}")
    public ResponseEntity<?> getDetail(@PathVariable String productId) {
        Product product = productRepository.getReferenceById(productId);

        String additionalDataId = product.getAdditionalData().getId();

        return ResponseEntity.ok().body(contentService.get(additionalDataId));
    }

    @GetMapping("/type")
    public ResponseEntity<?> getTypes() {
        return ResponseEntity.ok().body(productTypeRepository.findByDisplay(true));
    }
}
