import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}

class UserData {
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthDate;
    private String phoneNumber;
    private char gender;

    public UserData(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, char gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public char getGender() {
        return gender;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите данные в формате:");
        System.out.println("Фамилия Имя Отчество датарождения номертелефона пол");
        System.out.println("Пример ввода:");
        System.out.println("Иванов Иван Иванович 01.01.2000 1234567890 m");

        Scanner scanner = new Scanner(System.in);
        String userDataString = scanner.nextLine();
        //userDataString = "Иванов Иван Иванович 01.01.2000 1234567890 m";

        try {
            UserData userData = parseUserData(userDataString);
            writeUserDataToFile(userData);
            System.out.println("Данные успешно записаны в файл.");
        } catch (InvalidDataException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл:");
            e.printStackTrace();
        }
    }

    private static UserData parseUserData(String userDataString) throws InvalidDataException {
        String[] userDataParts = userDataString.split("\\s+");

        if (userDataParts.length != 6) {
            throw new InvalidDataException("Неверное количество данных.");
        }

        String lastName = userDataParts[0];
        String firstName = userDataParts[1];
        String middleName = userDataParts[2];
        String birthDate = userDataParts[3];
        String phoneNumber = userDataParts[4];
        char gender = userDataParts[5].charAt(0);

        // Проверка формата даты рождения
        Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        Matcher dateMatcher = datePattern.matcher(birthDate);
        if (!dateMatcher.matches()) {
            throw new InvalidDataException("Неверный формат даты рождения.");
        }

        // Проверка формата номера телефона
        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Неверный формат номера телефона.");
        }

        // Проверка пола
        if (gender != 'm' && gender != 'f') {
            throw new InvalidDataException("Неверный формат пола.");
        }

        return new UserData(lastName, firstName, middleName, birthDate, phoneNumber, gender);
    }

    private static void writeUserDataToFile(UserData userData) throws IOException {
        String fileName = userData.getLastName() + ".txt";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(userData.getLastName() + userData.getFirstName() + userData.getMiddleName() +
                    userData.getBirthDate() + " " + userData.getPhoneNumber() + userData.getGender() + "\n");
        }
    }
}
