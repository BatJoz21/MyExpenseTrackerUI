package joz.javapractice.myexpensetrackerui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AddingExpenseDTO {
    private int expenseType;
    private LocalDate date;
    private double amount;
    private String category;
    private String account;
    private String note;
}
