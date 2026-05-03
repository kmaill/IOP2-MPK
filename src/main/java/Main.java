import org.mpk.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // singleton i fasada
        SystemMpk system = SystemMpk.getInstance();
        UserFactory userFactory = new UserFactory();
        
        System.out.println("--- logowanie przez baze danych ---");
        User authenticatedUser = system.authenticateUser("admin_db", "haslo123");

        Administrator admin = null;
        if (authenticatedUser instanceof Administrator) {
            admin = (Administrator) authenticatedUser;

            AdministratorProxy adminProxy = new AdministratorProxy(admin);
            System.out.println("\n--- Test Proxy przed zalogowaniem ---");
            adminProxy.manageSchedules();

            admin.login();
            System.out.println("\n--- Test Proxy po zalogowaniu ---");
            adminProxy.manageSchedules();
        } else {
            System.out.println("nie dziala");
            return;
        }

        Passenger passenger = (Passenger) userFactory.createUser("PASSENGER", 2, "jan_kowalski", "haslo123");

        // trasa
        GPSCoordinates pos1 = new GPSCoordinates(52.2297, 21.0122);
        GPSCoordinates pos2 = new GPSCoordinates(52.2300, 21.0150);

        BusStop stop1 = new BusStop(101, "Centrum", pos1);
        BusStop stop2 = new BusStop(102, "Dworzec Centralny", pos2);

        Route route135 = new Route.Builder()
                .id(1)
                .name("Linia 135 - Kierunek Centrum")
                .addStop(stop1)
                .addStop(stop2)
                .build();

        // system powiadomień
        BusLine line135 = new BusLine("135", "Trasa ekspresowa");
        line135.attach(passenger);

        System.out.println("\n--- Test Obserwatora ---");
        line135.broadcastMessage("Uwaga: Opóźnienie na linii 135 z powodu korków");

        // renderowanie mapy
        Map map = new Map(new SchematicMapStrategy());
        Bus busOnRoute = new Bus(pos1, "W drodze");

        System.out.println("\n--- Widok Schematyczny ---");
        map.render(route135);

        System.out.println("\n--- Widok Real-Time ---");
        map.setStrategy(new RealTimeMapStrategy());
        map.render(busOnRoute);

        // klonowanie rozkładu
        Schedule baseSchedule = new Schedule(1, LocalDate.now(), "Poniedziałek");
        Schedule holidaySchedule = baseSchedule.clone();
        holidaySchedule.setDayOfWeek("Święto");

        System.out.println("\n--- Test Prototypu ---");
        System.out.println("Oryginał: " + baseSchedule.getDayOfWeek());
        System.out.println("Klon: " + holidaySchedule.getDayOfWeek());

        // fasada
        System.out.println("\n--- Usługi Systemowe ---");
        system.monitorTraffic();
        system.sendNotificationToUsers(new Notification(1, "System zostanie wyłączony o 2:00", "INFO", LocalDateTime.now()));
    }
}