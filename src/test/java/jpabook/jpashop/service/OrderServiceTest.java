package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    private Member createMember(){
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    @Test
    @Transactional
    public void 상품주문(){
        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount); // 주문 실행

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus()); // "주문 상태" 동일한지 확인
        Assertions.assertEquals(1, getOrder.getOrderItems().size()); // "주문 상품 개수" 동일한지 확인
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice()); // "주문 가격" 동일한지 확인
        Assertions.assertEquals(8, item.getStockQuantity()); // 기존 item 10개에서 2개 주문해서 8개 남았는지 확인
    }

    /*@Test
    @Transactional
    public void 상품주문_재고수량초과() throws Exception{
        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;

        // When
        try {
            orderService.order(member.getId(), item.getId(), orderCount);
        } catch (IllegalStateException e){
            return;
        }

        // Then
        Assertions.fail("예외가 발생해야 한다");
    }*/

    @Test
    @Transactional
    public void 주문취소() throws Exception{
        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount =2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //When
        orderService.cancelOrder(orderId); // 주문 취소 실행 -> 상태가 CANCEL 로 바뀌어야 하고, Item 재고가 원상복구되어야 함

        //Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus()); // 주문 상태 취소되었는지 확인
        Assertions.assertEquals(10, item.getStockQuantity()); //
    }
}
