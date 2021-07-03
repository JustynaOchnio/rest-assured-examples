package com.testinglaboratory.restassured.primer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String username;
    String password;
}
