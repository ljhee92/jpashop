package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    // merge 대신 변경감지(dirty checking) 사용 권장
    @Transactional
    public void updateItem(Long itemId, UpdateItemDto updateItemDto) {
        Book findBook = (Book) itemRepository.findOne(itemId);

        findBook.change(updateItemDto.name(), updateItemDto.price(), updateItemDto.stockQuantity(),
                updateItemDto.author(), updateItemDto.isbn());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
