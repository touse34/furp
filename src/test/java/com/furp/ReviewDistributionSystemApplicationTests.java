package com.furp;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;


class ReviewDistributionSystemApplicationTests {

    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("123456", BCrypt.gensalt()));
    }

}
