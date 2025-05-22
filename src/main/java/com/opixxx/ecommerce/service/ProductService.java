package com.opixxx.ecommerce.service;

import com.opixxx.ecommerce.controller.dto.ProductListResponse;
import com.opixxx.ecommerce.service.dto.ProductDto;

public interface ProductService {

	ProductDto.CreatedProduct createProduct(ProductDto.CreateRequest request);

	ProductDto.Product getProduct(Long productId);

	ProductListResponse getProducts(ProductDto.ListRequest request);

	ProductDto.Product updateProduct(Long productId, ProductDto.UpdateRequest request);

	void deleteProduct(Long productId);

	ProductDto.Option addProductOption(Long productId, ProductDto.Option option);

	ProductDto.Option updateProductOption(Long productId, ProductDto.Option option);

	void deleteProductOption(Long productId, Long optionId);

	// 이미지 관리
	ProductDto.Image addProductImage(Long productId, ProductDto.Image image);
}
