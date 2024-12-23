package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        // 상품 주문 시 상태는 ORDER
        assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());

        // 주문한 상품 종류 수가 정확해야 한다.
        assertThat(1).isEqualTo(getOrder.getOrderItems().size());

        // 주문 가격은 가격 * 수량이다.
        assertThat(10000 * orderCount) .isEqualTo(getOrder.getTotalPrice());

        // 주문 수량만큼 재고가 줄어야 한다.
        assertThat(8).isEqualTo(book.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;

        // when
        // then
        assertThatThrownBy(() -> {
            Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        }).isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        // 주문 취소 시 상태는 CANCEL
        assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());

        // 주문이 취소된 상품은 그만큼 재고가 증가해야 한다.
        assertThat(10).isEqualTo(book.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "관악", "123-123"));
        entityManager.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = Book.of(name, price, stockQuantity);
        entityManager.persist(book);
        return book;
    }
}