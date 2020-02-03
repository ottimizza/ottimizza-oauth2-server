package br.com.ottimizza.application.repositories.products;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.application.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, BigInteger> {

    @Query("SELECT p FROM Product p WHERE p.group = :group")
    public List<Product> findAllByGroup(@Param("group") String group);

}