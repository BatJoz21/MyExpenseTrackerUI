package joz.javapractice.myexpensetrackerui.models;

import lombok.Data;

@Data
public class AuthRequest {
    private String fullName;
    private String username;
    private String password;
}
