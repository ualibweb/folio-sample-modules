package org.folio.hello;

import java.util.Random;

import org.folio.hello.api.RollApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public final class RollController implements RollApi{

    private Random random = new Random();

    /** {@inheritDoc}} */
    @Override
    public ResponseEntity<Integer> rollGet() {
        Integer response = random.nextInt(6) + 1;

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /** {@inheritDoc}} */
    @Override
    public ResponseEntity<List<Integer>> rollPost(Integer n) {
        List<Integer> response = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            response.add(random.nextInt(6) + 1);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
