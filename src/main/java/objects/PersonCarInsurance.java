package objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

public class PersonCarInsurance {
    public static final String UNKNOWN_NAME = "unknown";
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Insurance {
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Car {
        private  Insurance insurance;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private  Car car;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpgradedInsurance {
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpgradedCar {
        private  Optional<UpgradedInsurance> insurance;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpgradedPerson {
        private Optional<UpgradedCar> car;
    }

}
