package org.example.cafemanager.controllers;

import org.example.cafemanager.dto.product.ProductCreate;
import org.example.cafemanager.dto.product.CreateProductRequest;
import org.example.cafemanager.dto.ResponseModel;
import org.example.cafemanager.services.product.ProductService;
import org.example.cafemanager.utilities.ValidationMessagesCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/list";
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateProductRequest requestBody, Errors errors) {
        ResponseModel result = new ResponseModel();
        if (errors.hasErrors()) {
            result.setMessage(ValidationMessagesCollector.collectErrorMessages(errors));
            return ResponseEntity.unprocessableEntity().body(result);
        }
        ProductCreate createDto = new ProductCreate(requestBody.getName());
        productService.create(createDto);
        result.setMessage("Product has been successfully created");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> update(
            @Valid @RequestBody CreateProductRequest requestBody,
            @PathVariable Long productId,
            Errors errors) {
        ResponseModel result = new ResponseModel();
        if (errors.hasErrors()) {
            result.setMessage(ValidationMessagesCollector.collectErrorMessages(errors));
            return ResponseEntity.unprocessableEntity().body(result);
        }
        productService.update(productId, requestBody);
        result.setMessage("Product has been successfully updated");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable("productId") Long productId) {
        ResponseModel result = new ResponseModel();
        productService.delete(productId);
        result.setMessage("Product has been successfully updated");
        return ResponseEntity.ok(result);
    }
}
