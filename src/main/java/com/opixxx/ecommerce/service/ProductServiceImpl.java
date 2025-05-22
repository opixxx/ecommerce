package com.opixxx.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opixxx.ecommerce.controller.dto.ProductListResponse;
import com.opixxx.ecommerce.domain.Brand;
import com.opixxx.ecommerce.domain.Category;
import com.opixxx.ecommerce.domain.Product;
import com.opixxx.ecommerce.domain.ProductDetail;
import com.opixxx.ecommerce.domain.ProductImage;
import com.opixxx.ecommerce.domain.ProductOption;
import com.opixxx.ecommerce.domain.ProductOptionGroup;
import com.opixxx.ecommerce.domain.ProductPrice;
import com.opixxx.ecommerce.domain.ProductStatus;
import com.opixxx.ecommerce.domain.Seller;
import com.opixxx.ecommerce.domain.Tag;
import com.opixxx.ecommerce.exception.DuplicateSlugException;
import com.opixxx.ecommerce.exception.NotFoundException;
import com.opixxx.ecommerce.exception.OptionProductMismatchException;
import com.opixxx.ecommerce.repository.BrandRepository;
import com.opixxx.ecommerce.repository.CategoryRepository;
import com.opixxx.ecommerce.repository.ProductImageRepository;
import com.opixxx.ecommerce.repository.ProductOptionGroupRepository;
import com.opixxx.ecommerce.repository.ProductOptionRepository;
import com.opixxx.ecommerce.repository.ProductRepository;
import com.opixxx.ecommerce.repository.ProductSpecification;
import com.opixxx.ecommerce.repository.SellerRepository;
import com.opixxx.ecommerce.repository.TagRepository;
import com.opixxx.ecommerce.service.dto.PaginationDto;
import com.opixxx.ecommerce.service.dto.ProductDto;
import com.opixxx.ecommerce.service.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final BrandRepository brandRepository;
	private final SellerRepository sellerRepository;
	private final TagRepository tagRepository;
	private final ProductOptionGroupRepository optionGroupRepository;
	private final ProductOptionRepository optionRepository;
	private final ProductImageRepository imageRepository;
	private final ProductMapper productMapper;

	@Override
	@Transactional
	public ProductDto.CreatedProduct createProduct(ProductDto.CreateRequest request) {

		if (productRepository.existsBySlug(request.getSlug())) {
			throw new DuplicateSlugException();
		}

		Product product = productMapper.toProductEntity(request);

		if (request.getSellerId() != null) {
			Seller seller = sellerRepository.findById(request.getSellerId()).orElseThrow(NotFoundException::new);
			product.setSeller(seller);
		}

		if (request.getBrandId() != null) {
			Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(NotFoundException::new);
			product.setBrand(brand);
		}

		// 3. 저장 및 ID 획득
		product = productRepository.save(product);

		// 4. 연관 관계 설정 및 저장
		// ProductDetail 생성 및 저장
		if (request.getDetail() != null) {
			ProductDetail detail = productMapper.toProductDetailEntity(request.getDetail(), product);
			product.setDetail(detail);
		}

		// ProductPrice 생성 및 저장
		if (request.getPrice() != null) {
			ProductPrice price = productMapper.toProductPriceEntity(request.getPrice(), product);
			product.setPrice(price);
		}

		// 카테고리 연결
		if (request.getCategories() != null && !request.getCategories().isEmpty()) {
			List<Long> categoryIds = request.getCategories().stream()
				.map(ProductDto.ProductCategory::getCategoryId)
				.toList();
			List<Category> categories = categoryRepository.findAllById(categoryIds);
			for (Category category : categories) {
				product.addCategory(category);
			}
		}

		// 태그 연결
		if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
			List<Tag> tags = tagRepository.findAllById(request.getTagIds());
			for (Tag tag : tags) {
				product.addTag(tag);
			}
		}

		// 옵션 그룹 및 옵션 생성
		if (request.getOptionGroups() != null) {
			for (ProductDto.OptionGroup groupDto : request.getOptionGroups()) {
				ProductOptionGroup group = productMapper.toProductOptionGroupEntity(groupDto, product);
				product.addOptionGroup(group);

				// 옵션 생성
				if (groupDto.getOptions() != null) {
					for (ProductDto.Option optionDto : groupDto.getOptions()) {
						ProductOption option = productMapper.toProductOptionEntity(optionDto, group);
						group.addOption(option);
					}
				}
			}
		}

		// 이미지 생성
		if (request.getImages() != null) {
			for (ProductDto.Image imageDto : request.getImages()) {
				ProductOption option = null;
				if (imageDto.getOptionId() != null) {
					option = optionRepository.findById(imageDto.getOptionId())
						.orElse(null);
				}
				ProductImage image = productMapper.toProductImageEntity(imageDto, product, option);
				product.addImage(image);
			}
		}

		// 최종 저장 및 응답 생성
		product = productRepository.save(product);
		return productMapper.toCreatedProductDto(product);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDto.Product getProduct(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(NotFoundException::new);
		return productMapper.toProductDto(product);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductListResponse getProducts(ProductDto.ListRequest request) {
		// Specification 생성 및 조합
		Specification<Product> spec = Specification.where(null);

		// 상태 필터
		if (request.getStatus() != null) {
			spec = spec.and(ProductSpecification.withStatus(request.getStatus()));
		}

		// 가격 범위 필터
		if (request.getMinPrice() != null) {
			spec = spec.and(ProductSpecification.withMinPrice(request.getMinPrice()));
		}

		if (request.getMaxPrice() != null) {
			spec = spec.and(ProductSpecification.withMaxPrice(request.getMaxPrice()));
		}

		// 카테고리 필터
		if (request.getCategory() != null && !request.getCategory().isEmpty()) {
			spec = spec.and(ProductSpecification.withCategoryId(request.getCategory()));
		}

		// 판매자 필터
		if (request.getSeller() != null) {
			spec = spec.and(ProductSpecification.withSellerId(request.getSeller()));
		}

		// 브랜드 필터
		if (request.getBrand() != null) {
			spec = spec.and(ProductSpecification.withBrandId(request.getBrand()));
		}

		// 태그 필터
		if (request.getTag() != null && !request.getTag().isEmpty()) {
			spec = spec.and(ProductSpecification.withTagIds(request.getTag()));
		}

		// 재고 여부 필터
		if (request.getInStock() != null) {
			spec = spec.and(ProductSpecification.inStock(request.getInStock()));
		}

		// 검색어 필터
		if (request.getSearch() != null && !request.getSearch().isEmpty()) {
			spec = spec.and(ProductSpecification.withSearch(request.getSearch()));
		}

		// 등록일 범위 필터
		if (request.getCreatedFrom() != null) {
			LocalDateTime fromDate = request.getCreatedFrom().atStartOfDay();
			spec = spec.and(ProductSpecification.withCreatedDateAfter(fromDate));
		}

		if (request.getCreatedTo() != null) {
			// 날짜의 끝(23:59:59)으로 설정
			LocalDateTime toDate = request.getCreatedTo().plusDays(1).atStartOfDay().minusSeconds(1);
			spec = spec.and(ProductSpecification.withCreatedDateBefore(toDate));
		}

		// 조회 실행
		Page<Product> productPage = productRepository.findAll(spec, request.getPagination().toPageable());

		// 결과 변환
		List<ProductDto.ProductSummary> productSummaries = productPage.stream()
			.map(productMapper::toProductSummaryDto)
			.toList();

		// 페이지네이션 정보 생성
		PaginationDto.PaginationInfo paginationInfo = PaginationDto.PaginationInfo.builder()
			.totalItems((int) productPage.getTotalElements())
			.totalPages(productPage.getTotalPages())
			.currentPage(request.getPagination().getPage())
			.perPage(request.getPagination().getSize())
			.build();

		// 응답 생성
		return ProductListResponse.builder()
			.items(productSummaries)
			.pagination(paginationInfo)
			.build();
	}

	@Override
	@Transactional
	public ProductDto.Product updateProduct(Long productId, ProductDto.UpdateRequest request) {
		Product product = productRepository.findById(productId)
			.map(entity -> productMapper.updateProductEntity(request, entity))
			.orElseThrow(NotFoundException::new);

		// 연관 엔티티 업데이트
		if (request.getSellerId() != null) {
			Seller seller = sellerRepository.findById(request.getSellerId()).orElseThrow(NotFoundException::new);
			product.setSeller(seller);
		}

		if (request.getBrandId() != null) {
			Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(NotFoundException::new);
			product.setBrand(brand);
		}

		// ProductDetail 업데이트
		if (request.getDetail() != null && product.getDetail() != null) {
			productMapper.updateProductDetailEntity(request.getDetail(), product.getDetail());
		}

		// ProductPrice 업데이트
		if (request.getPrice() != null && product.getPrice() != null) {
			productMapper.updateProductPriceEntity(request.getPrice(), product.getPrice());
		}

		// 카테고리 업데이트
		if (request.getCategories() != null) {
			product.getCategories().clear();
			List<Long> categoryIds = request.getCategories().stream()
				.map(ProductDto.ProductCategory::getCategoryId)
				.toList();
			List<Category> categories = categoryRepository.findAllById(categoryIds);
			for (Category category : categories) {
				product.addCategory(category);
			}
		}

		// 태그 업데이트
		if (request.getTagIds() != null) {
			product.getTags().clear();
			List<Tag> tags = tagRepository.findAllById(request.getTagIds());
			for (Tag tag : tags) {
				product.addTag(tag);
			}
		}
		// 저장 및 응답 생성
		product = productRepository.save(product);

		return productMapper.toProductDto(product);
	}

	@Override
	@Transactional
	public void deleteProduct(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);

		product.setStatus(ProductStatus.DELETED);
		productRepository.save(product);
	}

	@Override
	public ProductDto.Option addProductOption(Long productId, ProductDto.Option request) {
		Long optionGroupId = request.getOptionGroupId();

		ProductOptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
			.orElseThrow(NotFoundException::new);

		// 해당 옵션 그룹이 요청된 상품에 속하는지 확인
		validateOption(optionGroup, productId);

		// OptionDto 생성 및 변환
		ProductDto.Option optionDto = ProductDto.Option.builder()
			.optionGroupId(optionGroupId)
			.name(request.getName())
			.additionalPrice(request.getAdditionalPrice())
			.sku(request.getSku())
			.stock(request.getStock())
			.displayOrder(request.getDisplayOrder())
			.build();

		// 옵션 엔티티 생성 및 저장
		ProductOption option = productMapper.toProductOptionEntity(optionDto, optionGroup);
		option = optionRepository.save(option);

		return productMapper.toOptionDto(option);
	}

	@Override
	@Transactional
	public ProductDto.Option updateProductOption(Long productId, ProductDto.Option request) {
		Long optionId = request.getId();

		ProductOption option = optionRepository.findById(optionId)
			.orElseThrow(NotFoundException::new);

		validateOption(option.getOptionGroup(), productId);

		// 옵션 업데이트
		if (request.getName() != null) {
			option.setName(request.getName());
		}

		if (request.getAdditionalPrice() != null) {
			option.setAdditionalPrice(request.getAdditionalPrice());
		}

		if (request.getSku() != null) {
			option.setSku(request.getSku());
		}

		if (request.getStock() != null) {
			option.setStock(request.getStock());
		}

		if (request.getDisplayOrder() != null) {
			option.setDisplayOrder(request.getDisplayOrder());
		}

		option = optionRepository.save(option);
		return productMapper.toOptionDto(option);
	}


	@Override
	@Transactional
	public void deleteProductOption(Long productId, Long optionId) {
		ProductOption option = optionRepository.findById(optionId)
			.orElseThrow(NotFoundException::new);

		validateOption(option.getOptionGroup(), productId);

		optionRepository.delete(option);
	}

	@Override
	@Transactional
	public ProductDto.Image addProductImage(Long productId, ProductDto.Image request) {
		Product product = productRepository.findById(productId)
			.orElseThrow(NotFoundException::new);

		ProductOption option = null;
		if (request.getOptionId() != null) {
			option = optionRepository.findById(request.getOptionId())
				.orElseThrow(NotFoundException::new);

			validateOption(option.getOptionGroup(), productId);
		}

		// 이미지 엔티티 생성 및 저장
		ProductImage image = productMapper.toProductImageEntity(request, product, option);
		image = imageRepository.save(image);

		return productMapper.toImageDto(image);
	}

	private void validateOption(ProductOptionGroup option, Long productId) {
		if (!option.getProduct().getId().equals(productId)) {
			throw new OptionProductMismatchException();
		}
	}

}
