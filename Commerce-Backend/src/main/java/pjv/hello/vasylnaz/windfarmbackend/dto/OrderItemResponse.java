package pjv.hello.vasylnaz.windfarmbackend.dto;

public record OrderItemResponse(
   Long orderItemId,
   Long instanceId,
   Long orderId,
   Long productId,
   double priceAtPurchase
) {}
