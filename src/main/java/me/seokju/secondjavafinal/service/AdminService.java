package me.seokju.secondjavafinal.service;

import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.entity.cart.Cart;
import me.seokju.secondjavafinal.entity.cart.CartStatus;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.timetable.TimeTableStatus;
import me.seokju.secondjavafinal.repository.CartRepository;
import me.seokju.secondjavafinal.repository.TimeTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AdminService {

    private final CartRepository cartRepository;
    private final TimeTableRepository timeTableRepository;

    public AdminService(CartRepository cartRepository, TimeTableRepository timeTableRepository) {
        this.cartRepository = cartRepository;
        this.timeTableRepository = timeTableRepository;
    }

    public void moveCartToTimeTable() {
        List<Cart> cartList = cartRepository.findAllByStatus(CartStatus.APPLY_DONE);

        for (Cart cart : cartList) {
            Member member = cart.getMember();
            Lecture lecture = cart.getLecture();

            TimeTable timeTable = new TimeTable(member, lecture, lecture.getCredit(), TimeTableStatus.CART);
            timeTableRepository.save(timeTable);
        }
    }
}
