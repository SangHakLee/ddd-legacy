package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.DefaultPurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // NOTE: 강의 중 언급
//@SpringBootTest

@ExtendWith(MockitoExtension.class) // NOTE: mockito
class ProductServiceTest {

//    @Autowired
//    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuRepository menuRepository;
//    @Mock
//    private DefaultPurgomalumClient purgomalumClient;
//    @InjectMocks
    private ProductService productService;

    private DefaultPurgomalumClient purgomalumClient = new FakePurgomalumClient();

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, menuRepository, purgomalumClient);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
////        productService.create();
//        // given
//        given(productRepository.save(any())).willReturn(new Product());
//
//        // when
//        productService.create(new Product());
//
//        // then
//        verify(productRepository).save(any());


        final Product expected = new Product();
        expected.setName("후라이드");
        expected.setPrice(BigDecimal.valueOf(16_000L));

        final Product actual =  productService.create(expected);
        assertThat(actual).isNotNull();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
        );
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @ValueSource(strings = "-1000")
    @NullSource
    @ParameterizedTest
    void create(final BigDecimal price) {
        final Product expected = createProductRequest("후라이드", price);
        expected.setName("후라이드");
        expected.setPrice(BigDecimal.valueOf(16_000L));

        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @ValueSource(strings = {"비속어", "욕설이 포함된 이"})
    @NullSource
    @ParameterizedTest
    void create(final String name) {
        final Product expected = new Product();
        expected.setName(name);
        expected.setPrice(BigDecimal.valueOf(16_000L));

        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    public void findAll() {
        final List<Product> actual = productService.findAll();
        productRepository.save(product("후라이드", 16_000L));
        productRepository.save(product("양념치킨", 16_000L));
        assertThat(actual).hasSize(2);
    }

    private Product product(final String name, final long price) {
        final Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    private Product createProductRequest(final String name, final long price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice( BigDecimal.valueOf(price));
        return createProductRequest(name, BigDecimal.valueOf(price));
    }

    private Product createProductRequest(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}