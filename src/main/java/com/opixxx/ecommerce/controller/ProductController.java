package com.opixxx.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opixxx.ecommerce.controller.dto.ProductCreateRequest;
import com.opixxx.ecommerce.controller.dto.ProductImageRequest;
import com.opixxx.ecommerce.controller.dto.ProductListRequest;
import com.opixxx.ecommerce.controller.dto.ProductListResponse;
import com.opixxx.ecommerce.controller.dto.ProductOptionRequest;
import com.opixxx.ecommerce.controller.dto.ProductUpdateRequest;
import com.opixxx.ecommerce.controller.mapper.ProductControllerMapper;
import com.opixxx.ecommerce.service.ProductService;
import com.opixxx.ecommerce.service.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	private final ProductControllerMapper mapper;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductDto.CreatedProduct>> createProduct(
		@RequestBody ProductCreateRequest request
	) {
		ProductDto.CreateRequest createRequest = mapper.toProductDtoCreateRequest(request);
		ProductDto.CreatedProduct createdProduct = productService.createProduct(createRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(createdProduct, "Success Created Product"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductDto.Product>> getProduct(@PathVariable Long id) {
		ProductDto.Product product = productService.getProduct(id);
		return ResponseEntity.ok(ApiResponse.success(product, "상품 상세 정보를 성공적으로 조회했습니다."));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(@RequestBody ProductListRequest request) {
		ProductListResponse products = productService.getProducts(mapper.toProductDtoListRequest(request));
		return ResponseEntity.ok(ApiResponse.success(products, "상품 목록을 성공적으로 조회했습니다."));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductDto.Product>> updateProduct(
		@PathVariable Long id,
		@RequestBody ProductUpdateRequest request
	) {
		ProductDto.UpdateRequest updateRequest = mapper.toServiceUpdateDto(request);
		ProductDto.Product updateProduct = productService.updateProduct(id, updateRequest);
		return ResponseEntity.ok(ApiResponse.success(updateProduct, "상품이 성공적으로 수정되었습니다."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok(ApiResponse.success(null, "상품이 성공적으로 삭제되었습니다."));
	}

	@PostMapping("/{id}/options")
	public ResponseEntity<ApiResponse<ProductDto.Option>> addProductOption(
		@PathVariable Long id,
		@RequestParam Long optionGroupId,
		@RequestBody ProductOptionRequest request
	) {
		ProductDto.Option createRequest = mapper.toProductDtoOptionWithOptionGroupId(optionGroupId, request);
		ProductDto.Option createdOption = productService.addProductOption(id, createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdOption, "상품 옵션이 성공적으로 추가되었습니다."));
	}


	@PutMapping("/{id}/options/{optionId}")
	public ResponseEntity<ApiResponse<ProductDto.Option>> updateProductOption(
		@PathVariable Long id,
		@PathVariable Long optionId,
		@RequestBody ProductOptionRequest request
	) {
		ProductDto.Option updatedRequest = mapper.toProductDtoOptionOptionId(optionId, request);
		ProductDto.Option updatedOption = productService.updateProductOption(id, updatedRequest);
		return ResponseEntity.ok(ApiResponse.success(updatedOption, "상품 옵션이 성공적으로 수정되었습니다."));
	}

	@DeleteMapping("/{id}/options/{optionId}")
	public ResponseEntity<ApiResponse<Void>> deleteProductOption(
		@PathVariable Long id,
		@PathVariable Long optionId
	) {
		productService.deleteProductOption(id, optionId);
		return ResponseEntity.ok(ApiResponse.success(null, "상품 옵션이 성공적으로 삭제되었습니다."));
	}

	@PostMapping("/{id}/images")
	public ResponseEntity<ApiResponse<ProductDto.Image>> addProductImage(
		@PathVariable Long id,
		@RequestBody ProductImageRequest request
	) {
		ProductDto.Image createRequest = mapper.toProductDtoImage(request);
		ProductDto.Image createdImage = productService.addProductImage(id, createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdImage, "상품 이미지가 성공적으로 추가되었습니다."));
	}

}
