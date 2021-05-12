package chapters;

import objects.PersonCarInsurance;
import objects.Properties;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ChapterEleven {
    public static String getCarInsuranceName(PersonCarInsurance.Person person) {
        // if there is a person doesn't have car, NPE occurred!
        return person.getCar().getInsurance().getName();
    }

    // resolve NPE with checking null - it makes code has bad readability
    public static String getCarInsuranceNameWithoutNPE(PersonCarInsurance.Person person) {
        if (person != null) {
            PersonCarInsurance.Car car = person.getCar();
            if (car != null) {
                PersonCarInsurance.Insurance insurance = car.getInsurance();
                if (insurance != null) {
                    return insurance.getName();
                }
            }
        }
        return "Unknown";
    }

    // Code similar to this can invoke human error forgetting that the variable can be null.
    public static String getCarInsuranceNameWithoutNPEWithEarlyReturn(PersonCarInsurance.Person person) {
        if (person == null) {
            return PersonCarInsurance.UNKNOWN_NAME;
        }
        PersonCarInsurance.Car car = person.getCar();
        if (car == null) {
            return PersonCarInsurance.UNKNOWN_NAME;
        }
        PersonCarInsurance.Insurance insurance = car.getInsurance();
        if (insurance == null) {
            return PersonCarInsurance.UNKNOWN_NAME;
        }
        return insurance.getName();
    }

    public static String getUpgradedCarInsuranceNameWithOptional(Optional<PersonCarInsurance.UpgradedPerson> person) {
        return person.flatMap(PersonCarInsurance.UpgradedPerson::getCar)
                .flatMap(PersonCarInsurance.UpgradedCar::getInsurance)
                .map(PersonCarInsurance.UpgradedInsurance::getName)
                .orElse("Unknown");
    }

    public static Set<String> getCarInsuranceNames(List<PersonCarInsurance.UpgradedPerson> persons) {
        return persons.stream()
                .map(PersonCarInsurance.UpgradedPerson::getCar)
                .map(optCar -> optCar.flatMap(PersonCarInsurance.UpgradedCar::getInsurance))
                .map(optIns -> optIns.map(PersonCarInsurance.UpgradedInsurance::getName))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    public static boolean checkNotEmpty(
            Optional<PersonCarInsurance.UpgradedPerson> person,
            Optional<PersonCarInsurance.UpgradedCar> car) {
        return person.flatMap(p -> car.map(c -> true)).orElse(false);
    }

    public static boolean checkInsuranceIsPrudential(Optional<PersonCarInsurance.UpgradedInsurance> insurance) {
        return insurance.filter(i -> i.equals("Prudential"))
                .map(i -> true)
                .orElse(false);
    }

    public static Optional<Integer> stringToInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static int readDuration(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value != null) {
            try {
                int i = Integer.parseInt(value);
                if (i > 0) {
                    return i;
                }
            } catch (NumberFormatException e) {}
        }
        return 0;
    }

    public static int readDurationWithOptional(Properties properties, String name) {
        return Optional.ofNullable(properties.getProperty(name))
                .flatMap(ChapterEleven::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);

    }

    public static void run() {
        PersonCarInsurance.UpgradedInsurance insurance = new PersonCarInsurance.UpgradedInsurance("hello");
        PersonCarInsurance.UpgradedCar car = new PersonCarInsurance.UpgradedCar(Optional.ofNullable(insurance));
        PersonCarInsurance.UpgradedCar noInsuranceCarWithNoArgsConstructor = new PersonCarInsurance.UpgradedCar(); // it invoke NPE
        PersonCarInsurance.UpgradedCar noInsuranceCar = new PersonCarInsurance.UpgradedCar(Optional.empty());
        Optional<PersonCarInsurance.UpgradedPerson> person = Optional.ofNullable(new PersonCarInsurance.UpgradedPerson(Optional.ofNullable(car)));
        Optional<PersonCarInsurance.UpgradedPerson> noInsuranceCarPerson = Optional.ofNullable(new PersonCarInsurance.UpgradedPerson(Optional.ofNullable(noInsuranceCar)));
        Optional<PersonCarInsurance.UpgradedPerson> noCarPerson = Optional.ofNullable(new PersonCarInsurance.UpgradedPerson(Optional.empty()));

        System.out.println(getUpgradedCarInsuranceNameWithOptional(person));
        System.out.println(getUpgradedCarInsuranceNameWithOptional(noInsuranceCarPerson));
        System.out.println(getUpgradedCarInsuranceNameWithOptional(noCarPerson));

        System.out.println(getCarInsuranceNames(List.of(person.get(), noInsuranceCarPerson.get(), noCarPerson.get())));
        System.out.println(checkNotEmpty(person, Optional.empty()));
        System.out.println(checkInsuranceIsPrudential(Optional.of(insurance)));

        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-5");

        System.out.println(readDuration(props, "a"));
        System.out.println(readDuration(props, "b"));
        System.out.println(readDuration(props, "c"));

        System.out.println(readDurationWithOptional(props, "a"));
        System.out.println(readDurationWithOptional(props, "b"));
        System.out.println(readDurationWithOptional(props, "c"));

    }
}
