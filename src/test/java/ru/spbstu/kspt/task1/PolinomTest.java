package ru.spbstu.kspt.task1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolinomTest {

    @Test
    void plus() {
        assertEquals(new Polinom(4,"7;3;-3;-3;-8"),
                new Polinom(4, "7;3;-6;-1;-8").plus(new Polinom(2, "3;-2;0")));
    }




}