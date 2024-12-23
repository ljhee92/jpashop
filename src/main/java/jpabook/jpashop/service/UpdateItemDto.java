package jpabook.jpashop.service;

public record UpdateItemDto(String name, int price, int stockQuantity, String author, String isbn) {
}
