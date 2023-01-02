package me.seokju.secondjavafinal;

import java.time.LocalTime;

public class AppTest {

    public static void main(String[] args) {
        LocalTime wishLectureStartTime = LocalTime.of(12, 0);
        LocalTime wishLectureEndTime = LocalTime.of(14, 0);

        LocalTime lectureStartTime = LocalTime.of(12, 0);
        LocalTime lectureEndTime = LocalTime.of(14, 0);

        System.out.println(wishLectureStartTime.plusSeconds(1).isAfter(lectureStartTime));
        System.out.println(wishLectureStartTime.isBefore(lectureEndTime));

        System.out.println(wishLectureEndTime.isAfter(lectureStartTime));
        System.out.println(wishLectureEndTime.minusSeconds(1).isBefore(lectureEndTime));

//        return (wishLectureStartTime.isAfter(lectureStartTime) && wishLectureStartTime.isBefore(lectureEndTime))
//                || (wishLectureEndTime.isAfter(lectureStartTime) && wishLectureEndTime.isBefore(lectureEndTime));
    }
}
