package me.seokju.secondjavafinal.dto.cart;

import lombok.Getter;
import me.seokju.secondjavafinal.entity.cart.CartStatus;

@Getter
public class CartResponseDto {

    private Long cartId;

    private Long lectureId;

    private String lectureName;

    private CartStatus status;

    private int credit;

    public CartResponseDto(Long cartId, Long lectureId, String lectureName, CartStatus status, int credit) {
        this.cartId = cartId;
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.status = status;
        this.credit = credit;
    }
}
