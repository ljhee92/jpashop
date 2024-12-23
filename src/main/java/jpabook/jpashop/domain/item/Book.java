package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jpabook.jpashop.controller.BookForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String author;
    private String isbn;

    private Book(Long id, String name, int price, int quantity, String author, String isbn) {
        super.setId(id);
        super.setName(name);
        super.setPrice(price);
        super.setStockQuantity(quantity);
        this.setAuthor(author);
        this.setIsbn(isbn);
    }

    public static Book of(BookForm bookForm) {
        return new Book(bookForm.getId(),
                bookForm.getName(),
                bookForm.getPrice(),
                bookForm.getStockQuantity(),
                bookForm.getAuthor(),
                bookForm.getIsbn());
    }

    public static Book of(String name) {
        return new Book(0L, name, 0, 0, null, null);
    }

    public static Book of(String name, int price, int quantity) {
        return new Book(0L, name, price, quantity, null, null);
    }
}
