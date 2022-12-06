package org.example.service;

import org.example.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    EmailService emailService;

    @InjectMocks
    OrderService orderService;

    @Test
    public void testPlaceOrder() {
        Order order = new Order(1, "book", 150);

        orderService.placeOrder(order);

        // check the expected method is invoked by email service
        verify(emailService, times(1)).sendEmail(order);

        // check that price with tax is populated
        assertEquals(180d, order.getPriceWithTax(), 0.001);

        // check that order notified property is true
        assertTrue(order.isCustomerNotified());
    }

    @Test
    public void testPlaceOrderReturnsTrue() {

        Order order = new Order(1, "book", 150);
        when(emailService.sendEmail(order, "cc")).thenReturn(true);

        boolean response = orderService.placeOrder(order, "cc");

        // check that the method returns true
        assertTrue(response);

        // check the expected method is invoked by email service
        verify(emailService, times(1)).sendEmail(order, "cc");

        // check that price with tax is populated
        assertEquals(180d, order.getPriceWithTax(), 0.001);

        // check that customer notify property is true
        assertTrue(order.isCustomerNotified());
    }

}
