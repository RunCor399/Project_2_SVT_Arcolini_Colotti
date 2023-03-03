package com.sparta.week02.integration;


import com.sparta.week02.dto.ProductRequestDto;
import com.sparta.week02.dto.SignupRequestDto;
import com.sparta.week02.model.Product;
import com.sparta.week02.model.User;
import com.sparta.week02.model.UserRole;
import com.sparta.week02.repository.UserRepository;
import com.sparta.week02.service.ProductService;
import com.sparta.week02.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(readOnly = true)
public class UserProductIntegrationTest {

    //사용자
    private Long userId;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private Long kakaoId;
    private User createUser = null;


    //상품
    private String title;
    private String image;
    private String link;
    private int lprice;
    private Product createdProduct = null;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductService productService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @Order(1)
    @DisplayName("회원 가입이 되어있지 않을 때")
    void fail() {
        // given
        userId = null;
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String image = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String link = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lprice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                image,
                link,
                lprice
        );

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            new Product(requestDto, userId);
            productService.createProduct(requestDto,userId);
        });

        // then
        assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("회원 가입")
    void signup() {
        username = "jang";
        password = "1234";
        email = "epfvkdlxj@naver.com";

        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        User user = userService.registerUser(signupRequestDto);
        createUser = user;

        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(password,user.getPassword()));
        assertEquals(email, user.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("가입된 회원으로 관심상품 등록")
    void addProduct() {
        // given
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

        // when
//        System.out.println("====================");
//        System.out.println(this.createUser.getId());
//        System.out.println("====================");
        Product product = productService.createProduct(requestDto, this.createUser.getId());
        // then
        assertNotNull(product.getId());
        assertEquals(this.createUser.getId(), product.getUserId());
        assertEquals(title, product.getTitle());
        assertEquals(imageUrl, product.getImage());
        assertEquals(linkUrl, product.getLink());
        assertEquals(lPrice, product.getLprice());
        assertEquals(0, product.getMyprice());
        createdProduct = product;
    }

}
