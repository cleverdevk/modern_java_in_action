package objects.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code discoundCode;

    public static Quote parse(String s) {
        String[] split = s.split(":");
        return new Quote(split[0],
                Double.parseDouble(split[1]),
                Discount.Code.valueOf(split[2]));
    }
}
