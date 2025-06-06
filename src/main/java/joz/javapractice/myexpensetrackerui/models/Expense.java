package joz.javapractice.myexpensetrackerui.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Expense {
    private Integer id;
    private int expenseType;
    private LocalDate date;
    private double amount;
    private String category;
    private String account;
    private String note;
}
