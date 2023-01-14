package com.bootcamp.java.product.service.product;

import com.bootcamp.java.product.config.RedisConfig;
import com.bootcamp.java.product.dto.ProductDTO;
import com.bootcamp.java.product.dto.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
//@AllArgsConstructor
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class ProductServiceImplWithRedisCache extends  ProductServiceImpl{

    //private static final String KEY = "productresponse";
    public static String key;

    @Value("${KEY_REDIS_NAME:productresponse}")
    public void setKey(String KEY_REDIS_NAME) {
        ProductServiceImplWithRedisCache.key = KEY_REDIS_NAME;
    }

    @Autowired
    private ReactiveHashOperations<String, Integer, ProductResponseDTO> hashOperations;

    @Override
    public Mono<ProductResponseDTO> findById(Integer idProduct) {
        log.info("ProductServiceImplWithRedisCache: {}","findById");
        return hashOperations.get(key, idProduct)
                .switchIfEmpty(this.getFromDatabaseAndCache(idProduct));
    }

    private Mono<ProductResponseDTO> getFromDatabaseAndCache(Integer id) {
        return super.findById(id)
                .flatMap(dto -> {
                    log.info("getFromDatabaseAndCache: {}",dto.toString());
                    return this.hashOperations.put(key, id, dto).thenReturn(dto);
                });
    }

    @Override
    public Mono<ProductDTO> delete(Integer idProduct) {
        return super.delete(idProduct)
                .flatMap(dto -> {
                    return this.hashOperations.remove(key, idProduct).thenReturn(dto);
                });
    }

    @Override
    public Mono<ProductResponseDTO> update(ProductDTO productDTO) {
        return super.update(productDTO)
                .flatMap(dto -> {
                    return this.hashOperations.remove(key, dto.getIdProduct()).thenReturn(dto);
                });
    }

}
