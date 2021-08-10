package kitchenpos.application;

import kitchenpos.infra.PurgomalumClient;

import java.util.Arrays;
import java.util.List;

public class FakePurgomalumClient implements PurgomalumClient {
    private static final List<String> profanities;

    static {
        profanities = Arrays.asList("욕");
    }


    @Override
    public boolean containsProfanity(String text) {
        return profanities.
    }
}
