package com.restaurantapp.ndnhuy.customerservice;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GrafanaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    @SneakyThrows
    public void testCreateCustomer_shouldSuccess() {
//        var executors = Executors.newFixedThreadPool(1);
//        var n = 100;
//        var startGate = new CountDownLatch(1);
//        var endGate = new CountDownLatch(n);
//        for (int i = 0; i < n; i++) {
//            executors.submit(() -> {
//                try {
//                    startGate.await();
//                    var counter = Counter.builder("api_order_create")
//                            .tag("status", OrderStatus.INIT.toString())
//                            .description("order status")
//                            .register(meterRegistry);
//                    counter.increment();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    endGate.countDown();
//                }
//            });
//        }
//
//        startGate.countDown();
//        endGate.await();
//        executors.close();
    }

}
