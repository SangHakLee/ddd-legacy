package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import java.util.stream.Collectors;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    List<Product> findAllByIdIn(List<UUID> ids);
}

interface JpaProductRepository extends ProductRepository, JpaRepository<Product, UUID> {

}

// NOTE: 가짜 객체 만들기
// 단점은 그냥 ProductRepository 재정의하면 메소드 재정의가 너무 많다.
//
class InMemoryProductRepository implements ProductRepository {
    private final Map<UUID, Product> products = new HashMap<>();

    @Override
    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Product> findAllByIdIn(List<UUID> ids) {
        return products.values()
                .stream()
                .filter(product -> ids.contains(product.getId()))
                .collect(Collectors.toList());
    }
}
