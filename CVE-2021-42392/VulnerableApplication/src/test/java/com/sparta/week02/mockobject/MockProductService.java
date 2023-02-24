package com.sparta.week02.mockobject;

import com.sparta.week02.dto.ProductMypriceRequestDto;
import com.sparta.week02.dto.ProductRequestDto;
import com.sparta.week02.model.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MockProductService {
    // 멤버 변수 선언
    private final MockProductRepository mockProductRepository;
    private static final int MIN_PRICE = 100;

    // 생성자: ProductService() 가 생성될 때 호출됨
    public MockProductService() {
        // 멤버 변수 생성
        this.mockProductRepository = new MockProductRepository();
    }

    // 회원 ID 로 등록된 모든 상품 조회
    public List<Product> getProducts(Long userId) {
        return mockProductRepository.findAllByUsername(userId);
    }

    // 모든 상품 조회 (관리자용)
    public List<Product> getAllProducts() {
        return mockProductRepository.findAll();
    }

    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Product createProduct(ProductRequestDto requestDto, Long userId) {
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId);
        mockProductRepository.save(product);
        return product;
    }

    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        Product product = mockProductRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

        // 변경될 관심 가격이 유효한지 확인합니다.
        int myPrice = requestDto.getMyprice();
        if (myPrice < MIN_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_PRICE + " 원 이상으로 설정해 주세요.");
        }

        product.updateMyPrice(myPrice);
        return product;
    }
}
