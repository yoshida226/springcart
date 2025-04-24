package com.example.springcart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.springcart.form.OrderConfirmForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
public class StripeController {
	@Value("${stripe.secret-key}")
	    private String secretKey;

    @PostConstruct
    	public void init() {
        Stripe.apiKey = secretKey;
    }
    
    @Value("${stripe.public-key}")
    private String publicKey;

    @PostMapping("/create-checkout-session")
    @ResponseBody
    public Map<String, String> createSession(HttpSession session) throws StripeException {
    	
    	List<OrderConfirmForm> orderConfirmForms = (List<OrderConfirmForm>) session.getAttribute("orderConfirmForms");
    	
    	List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (OrderConfirmForm item : orderConfirmForms) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.getQuantity())
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("jpy")
                        .setUnitAmount(item.getProductId().getPrice().longValue())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProductId().getName())
                                .build()
                        )
                        .build()
                )
                .build();

            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/order/complete?status=success")
            .setCancelUrl("http://localhost:8080/order/complete?status=cancel")
            .addAllLineItem(lineItems)
            .build();

        Session stripeSession = Session.create(params);

        return  Map.of("id", stripeSession.getId());
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "cancel";
    }
}
