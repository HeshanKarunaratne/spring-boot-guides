package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Optional<Inventory> optional1 = inventoryRepository.findBySkuCode("Iphone13");
            if (!optional1.isPresent()) {
                Inventory inventory = new Inventory();
                inventory.setSkuCode("Iphone13");
                inventory.setQuantity(100);
                inventoryRepository.save(inventory);
            }

            Optional<Inventory> optional2 = inventoryRepository.findBySkuCode("Iphone13Red");

            if (!optional2.isPresent()) {
                Inventory inventory1 = new Inventory();
                inventory1.setSkuCode("Iphone13Red");
                inventory1.setQuantity(0);
                inventoryRepository.save(inventory1);
            }

        };
    }
}
