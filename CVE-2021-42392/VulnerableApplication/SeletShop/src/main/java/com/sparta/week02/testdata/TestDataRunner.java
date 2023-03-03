package com.sparta.week02.testdata;

import com.sparta.week02.dto.ItemDto;
import com.sparta.week02.model.Product;
import com.sparta.week02.model.User;
import com.sparta.week02.model.UserRole;
import com.sparta.week02.repository.ProductRepository;
import com.sparta.week02.repository.UserRepository;
import com.sparta.week02.service.UserService;
import com.sparta.week02.util.NaverShopSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NaverShopSearch naverShopSearch;

    private static final int MIN_PRICE = 100;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 테스트 User 생성
        User testUser = new User("르탄이", passwordEncoder.encode("123"), "user1@spartacodingclub.kr", UserRole.USER);
        testUser = userRepository.save(testUser);

        // 테스트 User 의 관심상품 등록
        // 검색어 당 관심상품 10개 등록
        createTestData(testUser, "신발");
        createTestData(testUser, "과자");
        createTestData(testUser, "키보드");
        createTestData(testUser, "휴지");
        createTestData(testUser, "휴대폰");
        createTestData(testUser, "앨범");
        createTestData(testUser, "헤드폰");
        createTestData(testUser, "이어폰");
        createTestData(testUser, "노트북");
        createTestData(testUser, "무선 이어폰");
        createTestData(testUser, "모니터");
    }

    private void createTestData(User user, String searchWord) {
        // 네이버 쇼핑 API 통해 상품 검색
        String jsonString = naverShopSearch.search(searchWord);
        List<ItemDto> itemDtoList = naverShopSearch.fromJSONtoItems(jsonString);

        List<Product> productList = new ArrayList<>();

        for (ItemDto itemDto : itemDtoList) {
            Product product = new Product();
            // 관심상품 저장 사용자
            product.setUserId(user.getId());
            // 관심상품 정보
            product.setTitle(itemDto.getTitle());
            product.setLink(itemDto.getLink());
            product.setImage(itemDto.getImage());
            product.setLprice(itemDto.getLprice());

            // 희망 최저가 랜덤값 생성
            // 최저 (100원) ~ 최대 (상품의 현재 최저가 + 10000원)
            int myPrice = getRandomNumber(MIN_PRICE, itemDto.getLprice() + 10000);
            product.setMyprice(myPrice);

            productList.add(product);
        }

        productRepository.saveAll(productList);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
